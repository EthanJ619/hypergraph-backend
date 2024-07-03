import numpy as np
import pandas as pd
import cvxpy as cp
import scipy.sparse as sparse
from sklearn.metrics import pairwise_distances
from sklearn.cluster import KMeans
from hyperg.hyperg import HyperG
from hyperg.utils import print_log
from cvxpy.error import SolverError
import psycopg2
import sys
import ast
import re
import json

table_id = sys.argv[1]
table_name = sys.argv[2]
algorithm = sys.argv[3]

# 保证对象字符串中的布尔型和数值型正确转换
algor_params = sys.argv[4].replace('=', ':').replace('false', 'False').replace('true', 'True').replace('null','None')
algor_params = re.sub(r'(\w+)\s*:', r'"\1":', algor_params)
algor_params = ast.literal_eval(algor_params)

def get_table(table_name):
    conn = psycopg2.connect(
        dbname="hypergraph",
        user="postgres",
        password="ethanj960916",
        host="localhost",
        port="5432"
    )
    query = "SELECT * FROM " + table_name
    df = pd.read_sql(query, conn)
    conn.close()
    return df


def gen_knn_hg(X, n_neighbors, is_prob=True):
    assert n_neighbors > 0

    X = np.array(X)
    n_nodes = X.shape[0]
    n_edges = n_nodes

    m_dist = pairwise_distances(X)

    # top n_neighbors+1
    m_neighbors = np.argpartition(m_dist, kth=n_neighbors + 1, axis=1)
    m_neighbors_val = np.take_along_axis(m_dist, m_neighbors, axis=1)

    m_neighbors = m_neighbors[:, :n_neighbors + 1]
    m_neighbors_val = m_neighbors_val[:, :n_neighbors + 1]

    for i in range(n_nodes):
        if not np.any(m_neighbors[i, :] == i):
            m_neighbors[i, -1] = i
            m_neighbors_val[i, -1] = 0.

    node_idx = m_neighbors.reshape(-1)
    edge_idx = np.tile(np.arange(n_edges).reshape(-1, 1), (1, n_neighbors + 1)).reshape(-1)

    if not is_prob:
        values = np.ones(node_idx.shape[0])
    else:
        avg_dist = np.mean(m_dist)
        m_neighbors_val = m_neighbors_val.reshape(-1)
        values = np.exp(-np.power(m_neighbors_val, 2.) / np.power(avg_dist, 2.))

    H = sparse.coo_matrix((values, (node_idx, edge_idx)), shape=(n_nodes, n_edges))

    return H


def gen_clustering_hg(X, n_clusters, random_state=None):
    cluster = KMeans(n_clusters=n_clusters, random_state=random_state).fit(X).labels_  # 聚类
#     print(cluster)

    assert n_clusters >= 1

    n_edges = n_clusters  # 一个簇对应一个边
    n_nodes = X.shape[0]  # 特征数量

    node_idx = np.arange(n_nodes)  # 节点索引
    edge_idx = cluster  # 簇标签

    values = np.ones(node_idx.shape[0])

    H = sparse.coo_matrix((values, (node_idx, edge_idx)), shape=(n_nodes, n_edges))  # 构建超图的稀疏邻接矩阵

    return H  # 返回邻接矩阵


def gen_l1_hg(X, gamma, n_neighbors, log=False):
    assert n_neighbors >= 1.
    assert isinstance(X, np.ndarray)
    assert X.ndim == 2

    n_nodes = X.shape[0]
    n_edges = n_nodes

    m_dist = pairwise_distances(X)
    m_neighbors = np.argsort(m_dist)[:, 0:n_neighbors + 1]

    edge_idx = np.tile(np.arange(n_edges).reshape(-1, 1), (1, n_neighbors + 1)).reshape(-1)
    node_idx = []
    values = []

    for i_edge in range(n_edges):
        if log:
            print_log("processing edge {} ".format(i_edge))

        neighbors = m_neighbors[i_edge].tolist()
        if i_edge in neighbors:
            neighbors.remove(i_edge)
        else:
            neighbors = neighbors[:-1]

        P = X[neighbors, :]
        v = X[i_edge, :]

        # cvxpy
        x = cp.Variable(P.shape[0], nonneg=True)
        objective = cp.Minimize(cp.norm((P.T @ x).T - v, 2) + gamma * cp.norm(x, 1))
        # objective = cp.Minimize(cp.norm(x@P-v, 2) + gamma * cp.norm(x, 1))
        prob = cp.Problem(objective)
        try:
            prob.solve()
        except SolverError:
            prob.solve(solver='SCS', verbose=False)

        node_idx.extend([i_edge] + neighbors)
        values.extend([1.] + x.value.tolist())

    node_idx = np.array(node_idx)
    values = np.array(values)

    H = sparse.coo_matrix((values,(node_idx, edge_idx)),shape=(n_nodes, n_edges))

    return H

def convert_to_js_format(H, feas):
    rows, cols = H.row, H.col
    n_edges = H.shape[1]  # 超边数

    # 构建超边数组
    edge_list = []
    for i in range(n_edges):
        nodes = list(rows[cols == i])
        edge_list.append({
            "id": str(i + 1),
            "nodes": [feas[node] for node in nodes]
        })
    # 构建节点数组
    node_list = []
    for i, fea in enumerate(feas):
        links = [str(col + 1) for row, col in zip(rows, cols) if row == i]
        node_list.append({
            "id": fea,
            "links": links
        })
    return {"edges": edge_list, "nodes": node_list}


if __name__ == '__main__':
    df = get_table(table_name)
    df = df.drop(columns=[col for col in ['index', 'label', 'id'] if col in df.columns])
    feas = df.columns.tolist()
    df_transposed = df.T

    factorHg = None
    if algorithm == "kmeans":
        factorHg = gen_clustering_hg(df_transposed, algor_params['k_clusters'], algor_params['random_state'])
    elif algorithm == "knn":
        factorHg = gen_knn_hg(df_transposed, algor_params['k_neighbors'], algor_params['is_prob'])
    elif algorithm == "l1_reg":
        factorHg = gen_l1_hg(df_transposed.values, algor_params['gamma'], algor_params['k_neighbors'])

    json_Result = json.dumps(convert_to_js_format(factorHg, feas)) # json字符串
    print(json_Result)

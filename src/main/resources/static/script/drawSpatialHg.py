import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import networkx as nx
import matplotlib.patheffects as path_effects
from mpl_toolkits.mplot3d.art3d import Line3DCollection
import random
import sys
import os

import psycopg2

pathSep = os.path.sep
localStorage_path = sys.argv[1]  # 由于是在java中使用processBuilder进行脚本执行，所以在当前脚本中获取相对路径时会获取到母进程(java程序)的所在目录(项目根目录)，所以只能通过命令参数将存储路径传递过来
table_id = sys.argv[2]
table_name = sys.argv[3]
task_name = sys.argv[4]
# csv_file = localStorage_path + "table" + pathSep + table_name

G_spatial = {'population': [],
             'physiology': [],
             'examine': []}
# 模拟字段管理表中的空间层次分类
# G_spatial = {'population': ['Air Pollution', 'OccuPational Hazards', 'Genetic Risk'],
#           'physiology': ['Alcohol Use', 'Smoking', 'Weight Loss', 'Balanced Diet', 'Passive Smoker'],
#           'examine': ['Age', 'Gender', 'Dust Allergy', 'chronic Lung Disease', 'Obesity', 'Chest Pain',
#                        'Coughing of Blood', 'Fatigue', 'Shortness of Breath', 'Wheezing',
#                        'Swallowing Difficulty', 'Clubbing of Finger Nails', 'Frequent Cold', 'Dry Cough',
#                        'Snoring']}

def get_table(table_name):
    conn = psycopg2.connect(
        dbname = "hypergraph",
        user = "postgres",
        password = "ethanj960916",
        host = "localhost",
        port = "5432"
    )
    query = "SELECT * FROM " + table_name
    df = pd.read_sql(query, conn)
    conn.close()
    return df

def draw_original():
    fig, ax = plt.subplots(1, 1, figsize=(w, h), dpi=200, subplot_kw={'projection': '3d'})
    for gi, G in enumerate(graphs):
        # 节点位置
        xs = list(list(zip(*list(G.pos.values())))[0])
        ys = list(list(zip(*list(G.pos.values())))[1])
        zs = [gi] * len(xs)  # set a common z-position of the nodes
        # 节点颜色node
        cs = [cols[gi]] * len(xs)

        # 添加层内连线
        lines3d = [(list(G.pos[i]) + [gi], list(G.pos[j]) + [gi]) for i, j in G.edges()]
        line_collection = Line3DCollection(lines3d, zorder=gi - 1, color=cols[gi], alpha=0.8)
        ax.add_collection3d(line_collection)

        # 添加节点
        ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=1, zorder=gi + 1)
        # 添加节点标签
        for li, lab in enumerate(G.nodes):
            ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7,
                    ha='center', va='center')

        # 添加层平面
        xdiff = max(xs) - min(xs)
        ydiff = max(ys) - min(ys)
        ymin = min(ys) - ydiff * 0.1
        ymax = max(ys) + ydiff * 0.1
        xmin = min(xs) - xdiff * 0.1 * (w / h)
        xmax = max(xs) + xdiff * 0.1 * (w / h)
        xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
        zz = np.zeros(xx.shape) + gi
        ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.3, zorder=gi)
        # 添加平面标签
        layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                            color='.95', fontsize=15, zorder=1e5, ha='left', va='center',
                            path_effects=[path_effects.Stroke(linewidth=2, foreground=cols[gi]),
                                          path_effects.Normal()])
    # 层间连线
    for gi in range(3):
        if gi != 2:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi + 1].pos[i[1]]) + [gi + 1]) for i in
                               line_between_layers[gi]]
        else:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi - 2].pos[i[1]]) + [gi - 2]) for i in
                               line_between_layers[gi]]
        between_lines = Line3DCollection(lines3d_between, zorder=gi, color='.3',
                                         alpha=0.5, linestyle='--', linewidth=1)
        ax.add_collection3d(between_lines)

    # 把图放在坐标系上
    ax.set_ylim(min(ys) - ydiff * 0.1, max(ys) + ydiff * 0.1)
    ax.set_xlim(min(xs) - xdiff * 0.1, max(xs) + xdiff * 0.1)
    ax.set_zlim(-0.1, len(graphs) - 1 + 0.1)

    # 选择显示视角
    angle = 30
    height_angle = 12
    ax.view_init(height_angle, angle)

    # 选择显示距离
    ax.dist = 10.0

    ax.set_axis_off()

    plt.savefig(localStorage_path + 'plot' + pathSep + task_name + pathSep + 'spatial_ori.png', dpi=425, bbox_inches='tight')


def draw_examine():
    fig, ax = plt.subplots(1, 1, figsize=(w, h), dpi=200, subplot_kw={'projection': '3d'})

    for gi, G in enumerate(graphs):
        # 节点位置
        xs = list(list(zip(*list(G.pos.values())))[0])
        ys = list(list(zip(*list(G.pos.values())))[1])
        zs = [gi] * len(xs)  # set a common z-position of the nodes
        # 节点颜色node colors
        cs = [cols[gi]] * len(xs)

        if gi == 0:
            # 添加层内连线
            lines3d = [(list(G.pos[i]) + [gi], list(G.pos[j]) + [gi]) for i, j in G.edges()]
            line_collection = Line3DCollection(lines3d, zorder=gi - 1, color=cols[gi], alpha=0.8)
            ax.add_collection3d(line_collection)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7,
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.3, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.95', fontsize=15, zorder=1e5, ha='left', va='center',
                                path_effects=[path_effects.Stroke(linewidth=2, foreground=cols[gi]),
                                              path_effects.Normal()])
        else:
            # 添加层内连线
            lines3d = [(list(G.pos[i]) + [gi], list(G.pos[j]) + [gi]) for i, j in G.edges()]
            line_collection = Line3DCollection(lines3d, zorder=gi - 1, color=cols[gi], alpha=0.1)
            ax.add_collection3d(line_collection)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=0.1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7, color='.9',
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.1, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.9', fontsize=15, zorder=1e5, ha='left', va='center')

    # 层间连线
    for gi in range(3):
        if gi != 2:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi + 1].pos[i[1]]) + [gi + 1]) for i in
                               line_between_layers[gi]]
        else:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi - 2].pos[i[1]]) + [gi - 2]) for i in
                               line_between_layers[gi]]
        between_lines = Line3DCollection(lines3d_between, zorder=gi, color='.3',
                                         alpha=0.1, linestyle='--', linewidth=1)
        ax.add_collection3d(between_lines)

    # 把图放在坐标系上
    ax.set_ylim(min(ys) - ydiff * 0.1, max(ys) + ydiff * 0.1)
    ax.set_xlim(min(xs) - xdiff * 0.1, max(xs) + xdiff * 0.1)
    ax.set_zlim(-0.1, len(graphs) - 1 + 0.1)

    # 选择显示视角
    angle = 30
    height_angle = 12
    ax.view_init(height_angle, angle)

    # 选择显示距离
    ax.dist = 10.0

    ax.set_axis_off()

    plt.savefig(localStorage_path + 'plot' + pathSep + task_name + pathSep + 'spatial_exam.png', dpi=425, bbox_inches='tight')


def draw_physiology():
    fig, ax = plt.subplots(1, 1, figsize=(w, h), dpi=200, subplot_kw={'projection': '3d'})

    for gi, G in enumerate(graphs):
        # 节点位置
        xs = list(list(zip(*list(G.pos.values())))[0])
        ys = list(list(zip(*list(G.pos.values())))[1])
        zs = [gi] * len(xs)  # set a common z-position of the nodes
        # 节点颜色node colors
        cs = [cols[gi]] * len(xs)

        if gi == 1:
            # 添加层内连线
            lines3d = [(list(G.pos[i]) + [gi], list(G.pos[j]) + [gi]) for i, j in G.edges()]
            line_collection = Line3DCollection(lines3d, zorder=gi - 1, color=cols[gi], alpha=0.8)
            ax.add_collection3d(line_collection)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7,
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.3, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.95', fontsize=15, zorder=1e5, ha='left', va='center',
                                path_effects=[path_effects.Stroke(linewidth=2, foreground=cols[gi]),
                                              path_effects.Normal()])
        else:
            # 添加层内连线
            lines3d = [(list(G.pos[i]) + [gi], list(G.pos[j]) + [gi]) for i, j in G.edges()]
            line_collection = Line3DCollection(lines3d, zorder=gi - 1, color=cols[gi], alpha=0.1)
            ax.add_collection3d(line_collection)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=0.1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7, color='.9',
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.1, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.9', fontsize=15, zorder=1e5, ha='left', va='center')

    # 层间连线
    for gi in range(3):
        if gi != 2:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi + 1].pos[i[1]]) + [gi + 1]) for i in
                               line_between_layers[gi]]
        else:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi - 2].pos[i[1]]) + [gi - 2]) for i in
                               line_between_layers[gi]]
        between_lines = Line3DCollection(lines3d_between, zorder=gi, color='.3',
                                         alpha=0.1, linestyle='--', linewidth=1)
        ax.add_collection3d(between_lines)

    # 把图放在坐标系上
    ax.set_ylim(min(ys) - ydiff * 0.1, max(ys) + ydiff * 0.1)
    ax.set_xlim(min(xs) - xdiff * 0.1, max(xs) + xdiff * 0.1)
    ax.set_zlim(-0.1, len(graphs) - 1 + 0.1)

    # 选择显示视角
    angle = 30
    height_angle = 12
    ax.view_init(height_angle, angle)

    # 选择显示距离
    ax.dist = 10.0

    ax.set_axis_off()

    plt.savefig(localStorage_path + 'plot' + pathSep + task_name + pathSep + 'spatial_physio.png', dpi=425, bbox_inches='tight')


def draw_population():
    fig, ax = plt.subplots(1, 1, figsize=(w, h), dpi=200, subplot_kw={'projection': '3d'})

    for gi, G in enumerate(graphs):
        # 节点位置
        xs = list(list(zip(*list(G.pos.values())))[0])
        ys = list(list(zip(*list(G.pos.values())))[1])
        zs = [gi] * len(xs)  # set a common z-position of the nodes
        # 节点颜色node colors
        cs = [cols[gi]] * len(xs)

        if gi == 2:
            # 添加层内连线
            lines3d = [(list(G.pos[i]) + [gi], list(G.pos[j]) + [gi]) for i, j in G.edges()]
            line_collection = Line3DCollection(lines3d, zorder=gi - 1, color=cols[gi], alpha=0.8)
            ax.add_collection3d(line_collection)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7,
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.3, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.95', fontsize=15, zorder=1e5, ha='left', va='center',
                                path_effects=[path_effects.Stroke(linewidth=2, foreground=cols[gi]),
                                              path_effects.Normal()])
        else:
            # 添加层内连线
            lines3d = [(list(G.pos[i]) + [gi], list(G.pos[j]) + [gi]) for i, j in G.edges()]
            line_collection = Line3DCollection(lines3d, zorder=gi - 1, color=cols[gi], alpha=0.1)
            ax.add_collection3d(line_collection)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=0.1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7, color='.9',
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.1, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.9', fontsize=15, zorder=1e5, ha='left', va='center')

    # 层间连线
    for gi in range(3):
        if gi != 2:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi + 1].pos[i[1]]) + [gi + 1]) for i in
                               line_between_layers[gi]]
        else:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi - 2].pos[i[1]]) + [gi - 2]) for i in
                               line_between_layers[gi]]
        between_lines = Line3DCollection(lines3d_between, zorder=gi, color='.3',
                                         alpha=0.1, linestyle='--', linewidth=1)
        ax.add_collection3d(between_lines)

    # 把图放在坐标系上
    ax.set_ylim(min(ys) - ydiff * 0.1, max(ys) + ydiff * 0.1)
    ax.set_xlim(min(xs) - xdiff * 0.1, max(xs) + xdiff * 0.1)
    ax.set_zlim(-0.1, len(graphs) - 1 + 0.1)

    # 选择显示视角
    angle = 30
    height_angle = 12
    ax.view_init(height_angle, angle)

    # 选择显示距离
    ax.dist = 10.0

    ax.set_axis_off()

    plt.savefig(localStorage_path + 'plot' + pathSep + task_name + pathSep + 'spatial_pop.png', dpi=425, bbox_inches='tight')


def draw_1and2():
    fig, ax = plt.subplots(1, 1, figsize=(w, h), dpi=200, subplot_kw={'projection': '3d'})

    for gi, G in enumerate(graphs):
        # 节点位置
        xs = list(list(zip(*list(G.pos.values())))[0])
        ys = list(list(zip(*list(G.pos.values())))[1])
        zs = [gi] * len(xs)  # set a common z-position of the nodes
        # 节点颜色node colors
        cs = [cols[gi]] * len(xs)
        if gi != 2:
            # 节点位置
            xs = list(list(zip(*list(G.pos.values())))[0])
            ys = list(list(zip(*list(G.pos.values())))[1])
            zs = [gi] * len(xs)  # set a common z-position of the nodes
            # 节点颜色node colors
            cs = [cols[gi]] * len(xs)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7,
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.3, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.95', fontsize=15, zorder=1e5, ha='left', va='center',
                                path_effects=[path_effects.Stroke(linewidth=2, foreground=cols[gi]),
                                              path_effects.Normal()])
        else:
            # 添加层内连线
            lines3d = [(list(G.pos[i]) + [gi], list(G.pos[j]) + [gi]) for i, j in G.edges()]
            line_collection = Line3DCollection(lines3d, zorder=gi - 1, color=cols[gi], alpha=0.1)
            ax.add_collection3d(line_collection)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=0.1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7, color='.9',
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.1, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.9', fontsize=15, zorder=1e5, ha='left', va='center')

    # 层间连线
    for gi in range(3):
        if gi != 2:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi + 1].pos[i[1]]) + [gi + 1]) for i in
                               line_between_layers[gi]]
        else:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi - 2].pos[i[1]]) + [gi - 2]) for i in
                               line_between_layers[gi]]
        if gi == 0:
            between_lines = Line3DCollection(lines3d_between, zorder=gi, color='.3',
                                             alpha=0.5, linestyle='--', linewidth=1)
        else:
            between_lines = Line3DCollection(lines3d_between, zorder=gi, color='.3',
                                             alpha=0.05, linestyle='--', linewidth=1)
        ax.add_collection3d(between_lines)

    # 把图放在坐标系上
    ax.set_ylim(min(ys) - ydiff * 0.1, max(ys) + ydiff * 0.1)
    ax.set_xlim(min(xs) - xdiff * 0.1, max(xs) + xdiff * 0.1)
    ax.set_zlim(-0.1, len(graphs) - 1 + 0.1)

    # 选择显示视角
    angle = 30
    height_angle = 12
    ax.view_init(height_angle, angle)

    # 选择显示距离
    ax.dist = 10.0

    ax.set_axis_off()

    plt.savefig(localStorage_path + 'plot' + pathSep + task_name + pathSep + 'spatial_1and2.png', dpi=425, bbox_inches='tight')


def draw_2and3():
    fig, ax = plt.subplots(1, 1, figsize=(w, h), dpi=200, subplot_kw={'projection': '3d'})

    for gi, G in enumerate(graphs):
        # 节点位置
        xs = list(list(zip(*list(G.pos.values())))[0])
        ys = list(list(zip(*list(G.pos.values())))[1])
        zs = [gi] * len(xs)  # set a common z-position of the nodes
        # 节点颜色node colors
        cs = [cols[gi]] * len(xs)
        if gi != 0:
            # 节点位置
            xs = list(list(zip(*list(G.pos.values())))[0])
            ys = list(list(zip(*list(G.pos.values())))[1])
            zs = [gi] * len(xs)  # set a common z-position of the nodes
            # 节点颜色node colors
            cs = [cols[gi]] * len(xs)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7,
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.3, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.95', fontsize=15, zorder=1e5, ha='left', va='center',
                                path_effects=[path_effects.Stroke(linewidth=2, foreground=cols[gi]),
                                              path_effects.Normal()])
        else:
            # 添加层内连线
            lines3d = [(list(G.pos[i]) + [gi], list(G.pos[j]) + [gi]) for i, j in G.edges()]
            line_collection = Line3DCollection(lines3d, zorder=gi - 1, color=cols[gi], alpha=0.1)
            ax.add_collection3d(line_collection)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=0.1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7, color='.9',
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.1, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.9', fontsize=15, zorder=1e5, ha='left', va='center')

    # 层间连线
    for gi in range(3):
        if gi != 2:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi + 1].pos[i[1]]) + [gi + 1]) for i in
                               line_between_layers[gi]]
        else:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi - 2].pos[i[1]]) + [gi - 2]) for i in
                               line_between_layers[gi]]
        if gi == 1:
            between_lines = Line3DCollection(lines3d_between, zorder=gi, color='.3',
                                             alpha=0.5, linestyle='--', linewidth=1)
        else:
            between_lines = Line3DCollection(lines3d_between, zorder=gi, color='.3',
                                             alpha=0.05, linestyle='--', linewidth=1)
        ax.add_collection3d(between_lines)

    # 把图放在坐标系上
    ax.set_ylim(min(ys) - ydiff * 0.1, max(ys) + ydiff * 0.1)
    ax.set_xlim(min(xs) - xdiff * 0.1, max(xs) + xdiff * 0.1)
    ax.set_zlim(-0.1, len(graphs) - 1 + 0.1)

    # 选择显示视角
    angle = 30
    height_angle = 12
    ax.view_init(height_angle, angle)

    # 选择显示距离
    ax.dist = 10.0

    ax.set_axis_off()

    plt.savefig(localStorage_path + 'plot' + pathSep + task_name + pathSep + 'spatial_2and3.png', dpi=425, bbox_inches='tight')


def draw_1and3():
    fig, ax = plt.subplots(1, 1, figsize=(w, h), dpi=200, subplot_kw={'projection': '3d'})

    for gi, G in enumerate(graphs):
        # 节点位置
        xs = list(list(zip(*list(G.pos.values())))[0])
        ys = list(list(zip(*list(G.pos.values())))[1])
        zs = [gi] * len(xs)  # set a common z-position of the nodes
        # 节点颜色node colors
        cs = [cols[gi]] * len(xs)
        if gi != 1:
            # 节点位置
            xs = list(list(zip(*list(G.pos.values())))[0])
            ys = list(list(zip(*list(G.pos.values())))[1])
            zs = [gi] * len(xs)  # set a common z-position of the nodes
            # 节点颜色node colors
            cs = [cols[gi]] * len(xs)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7,
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.3, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.95', fontsize=15, zorder=1e5, ha='left', va='center',
                                path_effects=[path_effects.Stroke(linewidth=2, foreground=cols[gi]),
                                              path_effects.Normal()])
        else:
            # 添加层内连线
            lines3d = [(list(G.pos[i]) + [gi], list(G.pos[j]) + [gi]) for i, j in G.edges()]
            line_collection = Line3DCollection(lines3d, zorder=gi - 1, color=cols[gi], alpha=0.1)
            ax.add_collection3d(line_collection)

            # 添加节点
            ax.scatter(xs, ys, zs, c=cs, s=125, marker='o', alpha=0.1, zorder=gi + 1)
            # 添加节点标签
            for li, lab in enumerate(G.nodes):
                ax.text(xs[li], ys[li], zs[li] + 0.11, lab, zorder=gi + 200, fontsize=7, color='.9',
                        ha='center', va='center')

            # 添加层平面
            xdiff = max(xs) - min(xs)
            ydiff = max(ys) - min(ys)
            ymin = min(ys) - ydiff * 0.1
            ymax = max(ys) + ydiff * 0.1
            xmin = min(xs) - xdiff * 0.1 * (w / h)
            xmax = max(xs) + xdiff * 0.1 * (w / h)
            xx, yy = np.meshgrid([xmin, xmax], [ymin, ymax])
            zz = np.zeros(xx.shape) + gi
            ax.plot_surface(xx, yy, zz, color=cols[gi], alpha=0.1, zorder=gi)
            # 添加平面标签
            layertext = ax.text(1.2, -1.2, gi * 0.95 + 0.5, G.name,
                                color='.9', fontsize=15, zorder=1e5, ha='left', va='center')

    # 层间连线
    for gi in range(3):
        if gi != 2:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi + 1].pos[i[1]]) + [gi + 1]) for i in
                               line_between_layers[gi]]
        else:
            lines3d_between = [(list(graphs[gi].pos[i[0]]) + [gi], list(graphs[gi - 2].pos[i[1]]) + [gi - 2]) for i in
                               line_between_layers[gi]]
        if gi == 2:
            between_lines = Line3DCollection(lines3d_between, zorder=gi, color='.3',
                                             alpha=0.5, linestyle='--', linewidth=1)
        else:
            between_lines = Line3DCollection(lines3d_between, zorder=gi, color='.3',
                                             alpha=0.05, linestyle='--', linewidth=1)
        ax.add_collection3d(between_lines)

    # 把图放在坐标系上
    ax.set_ylim(min(ys) - ydiff * 0.1, max(ys) + ydiff * 0.1)
    ax.set_xlim(min(xs) - xdiff * 0.1, max(xs) + xdiff * 0.1)
    ax.set_zlim(-0.1, len(graphs) - 1 + 0.1)

    # 选择显示视角
    angle = 30
    height_angle = 12
    ax.view_init(height_angle, angle)

    # 选择显示距离
    ax.dist = 10.0

    ax.set_axis_off()

    plt.savefig(localStorage_path + 'plot' + pathSep + task_name + pathSep + 'spatial_1and3.png', dpi=425, bbox_inches='tight')


if __name__ == '__main__':
    data = get_table(table_name)

    data = data.drop(columns=[col for col in ['index', 'label', 'id'] if col in data.columns])  # 去掉索引列、标签列

    correlation_matrix = data.corr(method='pearson').applymap(lambda x: 1 if x > 0.7 else 0)

    for i in range(len(correlation_matrix)):
        correlation_matrix.iloc[i, i] = 0

    # 得到值为1的表项对应的特征名
    coordinates = [(correlation_matrix.index[i], correlation_matrix.columns[j]) for i in range(len(correlation_matrix))
                   for j in range(i, len(correlation_matrix)) if
                   correlation_matrix.iloc[i, j] == 1]

    # 绘图部分
    plt.rcParams["font.sans-serif"] = ["Microsoft YaHei"]  # 设置字体
    plt.rcParams["axes.unicode_minus"] = False  # 该语句解决图像中的“-”负号的乱码问题

    # 三层颜色
    cols = ['steelblue', 'darksalmon', 'mediumseagreen']

    G1 = nx.Graph(name="生理学")
    G2 = nx.Graph(name="行为学")
    G3 = nx.Graph(name="人口学")
    # print((feature_columns.corr(method="pearson") > 0.7).sum().sum())

    # 读取数据库查询字段所属空间层次

    # 将特征进行分类并添加节点
    dataTemp = data.columns.tolist()
    random.shuffle(dataTemp)  # 打乱特征列表顺序
    for fea in dataTemp:
        is_fea_exist = 0
        for key, value_list in G_spatial.items():
            if fea in value_list:
                is_fea_exist = 1
                (G1 if key == 'examine' else (G2 if key == 'physiology' else (G3 if key == 'population' else ''))).add_node(
                    fea)
        if not is_fea_exist:
            # G1.add_node(fea) # 如果没有字段分类信息，就统一划分到生理学
            random.choice([G1, G2, G3]).add_node(fea)  # 随机分配
    # 添加边
    line_between_layers = {0: [], 1: [], 2: []}  # 层间连线
    for ((node_1, node_2)) in coordinates:
        if node_1 in G1.nodes:
            if node_2 in G1.nodes:
                G1.add_edge(node_1, node_2)
            elif node_2 in G2.nodes:
                line_between_layers[0].append((node_1, node_2))
            elif node_2 in G3.nodes:
                line_between_layers[2].append((node_2, node_1))
        elif node_1 in G2.nodes:
            if node_2 in G2.nodes:
                G2.add_edge(node_1, node_2)
            elif node_2 in G3.nodes:
                line_between_layers[1].append((node_1, node_2))
            elif node_2 in G1.nodes:
                line_between_layers[0].append((node_2, node_1))
        elif node_1 in G3.nodes:
            if node_2 in G3.nodes:
                G3.add_edge(node_1, node_2)
            elif node_2 in G1.nodes:
                line_between_layers[2].append((node_1, node_2))
            elif node_2 in G2.nodes:
                line_between_layers[1].append((node_2, node_1))

    # 生成节点布局
    G1.pos = nx.circular_layout(G1)
    G2.pos = nx.circular_layout(G2)
    G3.pos = nx.circular_layout(G3)

    graphs = [G1, G2, G3]

    w = 10
    h = 8
    
    if not os.path.exists(localStorage_path + "plot" + pathSep + task_name):
        os.makedirs(localStorage_path + "plot" + pathSep + task_name)

    draw_original()
    draw_examine()
    draw_physiology()
    draw_population()
    draw_1and2()
    draw_2and3()
    draw_1and3()

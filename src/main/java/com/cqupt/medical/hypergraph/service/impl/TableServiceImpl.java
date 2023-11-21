package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.Table;
import com.cqupt.medical.hypergraph.mapper.TableMapper;
import com.cqupt.medical.hypergraph.service.ColumnManagerService;
import com.cqupt.medical.hypergraph.service.TableService;
import com.cqupt.medical.hypergraph.util.JsonUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.cqupt.medical.hypergraph.util.Constants.*;

/**
 * @Author EthanJ
 * @Date 2023/6/23 19:07
 * @Version 1.0
 */
@Service
public class TableServiceImpl extends ServiceImpl<TableMapper, Table> implements TableService {

    @Autowired
    private TableMapper tableMapper;

    @Autowired
    private ColumnManagerService columnManagerService;

    /**
     * 上传数据表
     *
     * @param tableFile
     * @return
     */
    @Override
    @Transactional
    public String storeTable(MultipartFile tableFile) {
        String fileName = tableFile.getOriginalFilename().replaceAll("\\s", "_");  //文件名中空格替换为下划线

        /*判断文件是否已经存在*/
        if (getOne(new QueryWrapper<Table>().eq("table_name", fileName)) != null)
            return new JsonUtil(FAIL_CODE, "存在同名文件，请重命名后重新上传", null).toJsonString();
//        String subName = fileName.substring(fileName.lastIndexOf("."));
//        String mainName = fileName.substring(0, fileName.lastIndexOf("."));
//        String uuid = UUID.randomUUID().toString();
        String filePath = LOCALSTORAGE_PATH + "table\\"+ fileName;

        List<String> tableHeaders = new ArrayList<>();
        /*读取文件特征列*/
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(tableFile.getInputStream()))) {

            String[] headerRows = csvReader.readNext();
            tableHeaders = getTableHeaders(headerRows); //读取第一行(表头特征列)

            List<String[]> rows = getTableData(csvReader, tableHeaders, headerRows);//读取数据行并删除空表头对应的整列数据

            createTable(fileName, tableHeaders, rows);  //在数据库中创建数据表

            tableMapper.insertData(fileName, tableHeaders, rows);
        } catch (CsvValidationException e) {
            log.error("文件格式非CSV", e);
            return new JsonUtil<>(FAIL_CODE, "文件格式非CSV", null).toJsonString();
        } catch (FileNotFoundException e) {
            log.error("数据表文件未找到", e);
            return new JsonUtil<>(FAIL_CODE, "数据表文件未找到", null).toJsonString();
        } catch (IOException e) {
            log.error("数据表特征查询失败", e);
            return new JsonUtil<>(FAIL_CODE, "数据表特征查询失败", null).toJsonString();
        }

        /* 上传数据表文件到服务器 */
        try {
            tableFile.transferTo(new File(filePath)); //上传文件到本地保存
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传出错了！", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("其他错误", e);
        }

        return new JsonUtil<>(SUCCESS_CODE, "数据表上传成功", tableHeaders).toJsonString();
    }

    /**
     * 保存数据表信息
     *
     * @param tableInfo
     * @return
     */
    @Override
    public String saveTableInfo(Table tableInfo) {
        try {
            save(tableInfo);
            return new JsonUtil(SUCCESS_CODE, "保存数据表信息成功", null).toJsonString();
        } catch (TransactionException e) {
            e.printStackTrace();
            return new JsonUtil(FAIL_CODE, "保存数据表信息失败", null).toJsonString();
        }
    }

    /**
     * 获取当前所有数据表
     * 如果文件不存在，则删除对应数据库表项
     * 如果数据表大小未知，则读取文件大小对数据库进行更新
     * 返回更新后的列表
     *
     * @return
     */
    @Override
    public List<Table> getAllTables() {
        List<Table> tableList = list();
        Iterator<Table> tableIterator = tableList.iterator();
        while (tableIterator.hasNext()) {
            Table table = tableIterator.next();
            String filePath = LOCALSTORAGE_PATH + "table\\" + table.getTableName();
            /*检查文件是否存在，不存在则从数据库中删除表项*/
            if (!(new File(filePath).exists())) {
                tableIterator.remove();
                remove(new QueryWrapper<Table>().eq("table_name", table.getTableName()));
                continue;
            }
            /*获取文件大小*/
            if (table.getTableSize() == null || table.getTableSize().equals("")) {
                File tableFile = new File(filePath);
                Long tableSize = tableFile.length();

                table.setTableSize(fileSizeConvert(tableSize));

                updateById(table);
            }
        }

        return tableList;
    }

    /**
     * 删除数据表信息、文件及数据库中的表
     *
     * @param tableName
     * @return
     */
    @Override
    public String deleteTable(String tableName) {
        String filePath = LOCALSTORAGE_PATH + "table\\"+ tableName;
        File file = new File(filePath);
        if (file.delete()) {
            columnManagerService.delFieldByTableName(tableName);     //删除数据库中字段管理表中对应该表的的字段信息
            tableMapper.delTable(tableName);                 //删除数据库中对应的数据表
            remove(new QueryWrapper<Table>().eq("table_name", tableName));  //删除数据库中数据表信息表项
            return new JsonUtil(SUCCESS_CODE, "删除成功", null).toJsonString();
        } else
            return new JsonUtil(FAIL_CODE, "文件删除失败", null).toJsonString();
    }


    /**
     * 查询数据表的特征列
     *
     * @param tableName
     * @return
     */
    @Override
    @Transactional
    public String queryTableFeatures(String tableName) {
        String filePath = LOCALSTORAGE_PATH + "table\\"+ tableName;

        List<String> tableHeaders = new ArrayList<>();
        /*读取文件特征列*/
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {

            String[] headerRows = csvReader.readNext();
            tableHeaders = getTableHeaders(headerRows); //读取第一行(表头特征列)
            return new JsonUtil<>(SUCCESS_CODE, "数据表特征查询成功", tableHeaders).toJsonString();
        } catch (CsvValidationException e) {
            log.error("文件格式非CSV", e);
            return new JsonUtil<>(FAIL_CODE, "文件格式非CSV", null).toJsonString();
        } catch (FileNotFoundException e) {
            log.error("数据表文件未找到", e);
            return new JsonUtil<>(FAIL_CODE, "数据表文件未找到", null).toJsonString();
        } catch (IOException e) {
            log.error("数据表特征查询失败", e);
            return new JsonUtil<>(FAIL_CODE, "数据表特征查询失败", null).toJsonString();
        }
    }

    /**
     * 在数据库中创建数据表
     *
     * @param tableName
     * @param tableHeaders
     * @param rows
     */
    public void createTable(String tableName, List<String> tableHeaders, List<String[]> rows) {
        List<String> columnTypes = new ArrayList<>();
        for (int i = 0; i < tableHeaders.size(); i++) {
            String columnType = determineColumnType(tableHeaders.get(i), tableHeaders, rows);  //获取该列数据类型
            columnTypes.add(columnType);
        }
        tableMapper.createTable(tableName, IntStream.range(0, tableHeaders.size())     //生成整数流作为链表索引
                .boxed()                                                               //将整数流转换为 Integer 对象流
                .collect(Collectors.toMap(tableHeaders::get, columnTypes::get,         //将表头和数据类型映射为键值对，并收集为一个LinkedHashMap对象
                        (value1, value2) -> value1,                                    //如果出现键冲突，就保留原来的值
                        LinkedHashMap::new)));                                         //构造器引用，创建一个LinkedHashMap实例
    }


    /**
     * 获取表头(去掉空表头)
     *
     * @param headerRows
     * @return
     * @throws CsvValidationException
     * @throws IOException
     */
    public List<String> getTableHeaders(String[] headerRows) throws CsvValidationException, IOException {
        List<String> tableHeaders = new ArrayList<>();
        for (int i = 0; i < headerRows.length; i++) {
            String columnName = headerRows[i].trim();
            if (!columnName.isEmpty()) {
                tableHeaders.add(columnName);
            }
        }
        return tableHeaders;
    }

    /**
     * 从第二行开始，获取数据行
     *
     * @param csvReader
     * @param tableHeaders
     * @param headerRows
     * @return
     * @throws IOException
     * @throws CsvValidationException
     */
    public List<String[]> getTableData(CSVReader csvReader, List<String> tableHeaders, String[] headerRows) throws
            IOException, CsvValidationException {
        List<String[]> rows = new ArrayList<>();  //存储处理后的数据行
        String[] row;
        while ((row = csvReader.readNext()) != null) {
            String[] newRow = new String[tableHeaders.size()];
            int j = 0;
            for (int i = 0; i < row.length; i++) {
                if (!headerRows[i].trim().isEmpty()) {
                    newRow[j++] = row[i];
                }
            }
            rows.add(newRow);
        }
        return rows;
    }

    /**
     * 生成特征类别信息
     *
     * @param columnName
     * @param rows
     * @param tableHeaders
     * @return
     */
    private String determineColumnType(String columnName, List<String> tableHeaders, List<String[]> rows) {

        // 判断数据是否为整数
        boolean isInt = true;
        // 判断数据是否为浮点数
        boolean isDouble = true;
        // 判断数据是否为日期，这里假设日期格式为"yyyy-MM-dd"，你可以根据实际情况修改格式
        boolean isDate = true;

        int columnIndex = tableHeaders.indexOf(columnName);

        for (String[] row : rows) {
            String data = row[columnIndex];
            // 判断是否为整数
            try {
                Integer.parseInt(data);
            } catch (NumberFormatException e) {
                isInt = false;
            }

            // 判断是否为浮点数
            try {
                Double.parseDouble(data);
            } catch (NumberFormatException e) {
                isDouble = false;
            }

            // 判断是否为日期
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate.parse(data, dateFormatter);
            } catch (DateTimeParseException e) {
                isDate = false;
            }

            // 如果有一行不符合整数、浮点数、或日期的格式，则退出循环
            if (!isInt && !isDouble && !isDate) {
                break;
            }
        }

        // 根据判断结果返回对应的列类型
        if (isInt) {
            return "INT";
        } else if (isDouble) {
            return "DOUBLE";
        } else if (isDate) {
            return "DATE";
        } else {
            // 如果以上条件都不满足，则返回默认的VARCHAR类型，长度为255
            return "VARCHAR(255)";
        }

    }

    /**
     * 文件大小单位换算
     *
     * @param tableSize
     * @return
     */
    private static String fileSizeConvert(Long tableSize) {
        if (tableSize < 1024.0)
            return tableSize + "B";
        else if (tableSize >= 1024.0 && tableSize < 1024.0 * 1024.0)
            return String.format("%.2f", tableSize / 1024.0) + "KB";
        else
            return String.format("%.2f", tableSize / (1024.0 * 1024.0)) + "MB";
    }
}

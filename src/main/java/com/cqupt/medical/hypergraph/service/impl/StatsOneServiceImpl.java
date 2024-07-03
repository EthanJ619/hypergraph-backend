package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.entity.StatsOne;
import com.cqupt.medical.hypergraph.mapper.StatsOneMapper;
import com.cqupt.medical.hypergraph.service.StatsOneService;
import com.cqupt.medical.hypergraph.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StatsOneServiceImpl extends ServiceImpl<StatsOneMapper, StatsOne>
        implements StatsOneService {

    @Autowired StatsOneMapper statsOneMapper;
    public StatsOne getStatsOne(){
        StatsOne statsOne = new StatsOne();
        //获得任务总量
        Integer numberOfDiease = statsOneMapper.getDiseaseCount();
        statsOne.setSpecialDiseaseNumber(numberOfDiease);


        //获得样本总量
        List<String> tableNames =  statsOneMapper.getTableNames();
        Integer totalItemNumber = 0;
        for (String tableName: tableNames) {
            Integer numberOfSample = statsOneMapper.getSampleCount(tableName);
            totalItemNumber+=numberOfSample;
        }
        statsOne.setItemNumber(totalItemNumber);

        //获得起始时间
        List<Date> earlyDates = new ArrayList<>();
        List<Date> lateDates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (String tableName: tableNames) {
            Date earlyDate = statsOneMapper.getEarlyDate(tableName);
            Date lastDate = statsOneMapper.getLastDate(tableName);
            earlyDates.add(earlyDate);
            lateDates.add(lastDate);
        }
        if (earlyDates != null && !earlyDates.isEmpty()) {
            Date earliestDate = earlyDates.get(0);
            for (Date date : earlyDates) {
                if (date.before(earliestDate)) {
                    earliestDate = date;
                }
            }
            String earlyFormattedDate = sdf.format(earliestDate);
            statsOne.setStartTime(earlyFormattedDate);
        }
        if (lateDates != null && !lateDates.isEmpty()) {
            Date latestDate  = lateDates.get(0);
            for (Date date : lateDates) {
                if (date.before(latestDate)) {
                    latestDate  = date;
                }
            }
            String lateFormattedDate = sdf.format(latestDate);
            statsOne.setEndTime(lateFormattedDate);
        }

        //获得任务总数
        statsOne.setTaskNumber(statsOneMapper.getTaskCount());
        return statsOne;
    }

    @Override
    public Result getDiseases() {
        Map<String,Integer>  diseases = new HashMap<>();
        List<String> tableNames =  statsOneMapper.getTableNames();
        for (String tableName: tableNames) {
            Integer numberOfSample = statsOneMapper.getSampleCount(tableName);
            diseases.put(tableName,numberOfSample);
        }
        return Result.success(200,"查询成功",diseases);
    }

    @Override
    public List<String> getAllTableNames() {
        List<String> allTableNames = statsOneMapper.getTableNames();
        allTableNames.addAll(statsOneMapper.getUserBuildTableNames());
        return allTableNames;
    }
}

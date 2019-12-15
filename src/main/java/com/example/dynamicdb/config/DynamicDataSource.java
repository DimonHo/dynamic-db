package com.example.dynamicdb.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Author: He Zhigang
 * @Date: 2019/12/10 12:44
 * @Description: 动态路由数据源
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String datasource = DataSourceContextHolder.getDbType();
        log.debug("使用数据源 {}", datasource);
        return datasource;
    }
}
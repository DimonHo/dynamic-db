package com.example.dynamicdb.config;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: He Zhigang
 * @Date: 2019/12/10 12:45
 * @Description: 数据源上线文
 */
public class DataSourceContextHolder {
    /**
     * 保存所有的数据源
     */
    public static final Map<String, DataSource> DATA_SOURCE_MAP = new HashMap<>();
    /**
     * 实际上就是开启多个线程，每个线程进行初始化一个数据源
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 取得当前数据源
     *
     * @return
     */
    public static String getDbType() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 设置数据源
     *
     * @param dbEnum
     */
    public static void setDbType(String dbEnum) {
        CONTEXT_HOLDER.set(dbEnum);
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        CONTEXT_HOLDER.remove();
    }
}

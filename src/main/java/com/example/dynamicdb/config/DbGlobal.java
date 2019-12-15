package com.example.dynamicdb.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.config.po.TableFill;

import java.util.List;
import java.util.Map;

/**
 * @Author: He Zhigang
 * @Date: 2019/12/15 12:14
 * @Description:
 */
public class DbGlobal {

    /**
     * 第一个数据源自动生成的包名名称
     */
    public static final String DB1 = "source";
    /**
     * 第二个数据源自动生成的包名名称
     */
    public static final String DB2 = "target";

    //TODO 跟多数据源继续添加DB3，DB4,...

    /**
     * 需要自动生成的表，为空表示全部生成，与STRATEGY_EXCLUDE不能同时设置
     */
    public static final Map<String, String[]> STRATEGY_INCLUDE = MapUtil.builder(DB1, new String[]{})
            .put(DB2, new String[]{})
            //TODO put DB3,...
            .build();

    /**
     * 排除自动生成的表，为空表示不排除，与STRATEGY_INCLUDE不能同时设置
     */
    public static final Map<String, String[]> STRATEGY_EXCLUDE = MapUtil.builder(DB1, new String[]{"scopus_journal", "scopus_journal2", "t_update_log", "t_journal_metadata"})
            .put(DB2, new String[]{})
            //TODO put DB3,...
            .build();

    /**
     * 数据源DB1中需要自动填充的字段
     */
    public static final List<TableFill> DB1_TABLE_FILL = CollectionUtil.newArrayList(
            new TableFill("gmt_create", FieldFill.INSERT),
            new TableFill("gmt_modified", FieldFill.INSERT_UPDATE)
            //TODO new TableFill("columnName",FieldFill),...
    );

    /**
     * 数据源DB2中需要填充的字段
     */
    public static final List<TableFill> DB2_TABLE_FILL = CollectionUtil.newArrayList(
            new TableFill("gmt_create", FieldFill.INSERT),
            new TableFill("gmt_modified", FieldFill.INSERT_UPDATE)
            //TODO new TableFill("columnName",FieldFill),...
    );

    public static final Map<String, List<TableFill>> STRATEGY_TABLE_FILL = MapUtil.builder(DB1, DB1_TABLE_FILL)
            //TODO put(DB2,DB2_TABLE_FILL),...
            .build();
}

package com.example.dynamicdb.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @Author: He Zhigang
 * @Date: 2019/12/15 14:15
 * @Description:
 */
@Slf4j
@Configuration
@ConditionalOnClass(value = AutoGenerator.class)
@ConditionalOnProperty(prefix = "mybatis-plus", name = "auto-generator", havingValue = "true")
public class AutoGeneratorBean implements ApplicationRunner {

    /**
     * auto_generator配置文件名称，位于resources目录下
     */
    private static final String AUTO_GENERATOR = "auto-generator";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //读取配置文件
        ResourceBundle rb = ResourceBundle.getBundle(AUTO_GENERATOR);
        DataSourceContextHolder.DATA_SOURCE_MAP.forEach((db, datasource) -> {
            log.info(db + "模块自动创建开始。。。");
            String projectPath = System.getProperty("user.dir");
            // 代码生成器
            AutoGenerator mpg = new AutoGenerator();
            mpg.setGlobalConfig(globalConfig(rb, projectPath))
                    .setDataSource(dataSourceConfig(datasource))
                    .setPackageInfo(packageConfig(rb, db))
                    .setCfg(InjectionConfig(rb, db, projectPath))
                    .setTemplate(templateConfig())
                    .setStrategy(strategyConfig(rb, db))
                    .setTemplateEngine(new FreemarkerTemplateEngine())
                    .execute();
            log.info(db + "模块自动创建完成！");
        });
    }

    private InjectionConfig InjectionConfig(ResourceBundle rb, String db, String projectPath) {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        //模板引擎默认是velocity,可选freemarker,beetl
        String templatePath = "/templates/mapper.xml";
        switch (rb.getString("injection.templateType")) {
            case "freemarker":
                templatePath = templatePath + ".ftl";
                break;
            case "beetl":
                templatePath = templatePath + ".btl";
                break;
            default:
                templatePath = templatePath + ".vm";
        }
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + db
                        + "/" + String.format(StrUtil.blankToDefault(rb.getString("global.xmlName"), "%sMapper"), tableInfo.getEntityName()) + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    /**
     * 配置模板
     *
     * @return
     */
    private TemplateConfig templateConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setXml(null);
        return templateConfig;
    }

    private StrategyConfig strategyConfig(ResourceBundle rb, String db) {
        StrategyConfig strategy = new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
                .setControllerMappingHyphenStyle(true)
                .setTablePrefix(db + "_");
        if (ArrayUtil.isNotEmpty(DbGlobal.STRATEGY_INCLUDE.get(db))) {
            strategy.setInclude(DbGlobal.STRATEGY_INCLUDE.get(db));
        } else if (ArrayUtil.isNotEmpty(DbGlobal.STRATEGY_EXCLUDE.get(db))) {
            strategy.setExclude(DbGlobal.STRATEGY_EXCLUDE.get(db));
        }
        if (CollectionUtil.isNotEmpty(DbGlobal.STRATEGY_TABLE_FILL.get(db))) {
            strategy.setTableFillList(DbGlobal.STRATEGY_TABLE_FILL.get(db));
        }
        if (StrUtil.isNotBlank(rb.getString("strategy.superEntityClass"))) {
            strategy.setSuperEntityClass(rb.getString("strategy.superEntityClass"));
            if (StrUtil.isNotBlank(rb.getString("strategy.superEntityColumns"))) {
                // 写于父类中的公共字段
                strategy.setSuperEntityColumns(StrSpliter.splitToArray(rb.getString("strategy.superEntityColumns"), ",", 0, true, true));
            }
        }
        strategy.setSuperControllerClass(rb.getString("strategy.superControllerClass"));
        return strategy;
    }

    private PackageConfig packageConfig(ResourceBundle rb, String db) {
        return new PackageConfig()
                .setParent(rb.getString("package.parent"))
                .setModuleName(db)
                .setController(StrUtil.blankToDefault(rb.getString("package.controller"), "controller"))
                .setEntity(StrUtil.blankToDefault(rb.getString("package.entity"), "entity"))
                .setMapper(StrUtil.blankToDefault(rb.getString("package.mapper"), "mapper"))
                .setService(StrUtil.blankToDefault(rb.getString("package.service"), "service"))
                .setServiceImpl(StrUtil.blankToDefault(rb.getString("package.serviceImpl"), "service.impl"));
    }

    private DataSourceConfig dataSourceConfig(DataSource datasource) {
        DruidDataSource ds = (DruidDataSource) datasource;
        return new DataSourceConfig()
                .setUrl(ds.getUrl())
                .setDriverName(ds.getDriverClassName())
                .setUsername(ds.getUsername())
                .setPassword(ds.getPassword());
    }

    private GlobalConfig globalConfig(ResourceBundle rb, String projectPath) {
        return new GlobalConfig()
                .setOutputDir(projectPath + rb.getString("global.outputDir"))
                .setFileOverride(Boolean.parseBoolean(rb.getString("global.fileOverride")))
                .setAuthor(rb.getString("global.author"))
                .setControllerName(rb.getString("global.controllerName"))
                .setEntityName(rb.getString("global.entityName"))
                .setMapperName(rb.getString("global.mapperName"))
                .setXmlName(rb.getString("global.xmlName"))
                .setServiceName(rb.getString("global.serviceName"))
                .setServiceImplName(rb.getString("global.serviceImplName"))
                .setEnableCache(Boolean.parseBoolean(rb.getString("global.enableCache")))
                .setSwagger2(Boolean.parseBoolean(rb.getString("global.swagger")))
                .setBaseColumnList(Boolean.parseBoolean(rb.getString("global.baseColumnList")))
                .setBaseResultMap(Boolean.parseBoolean(rb.getString("global.baseResultMap")))
                .setDateType(DateType.ONLY_DATE)
                .setOpen(Boolean.parseBoolean(rb.getString("global.open")));
    }
}
package com.example.dynamicdb.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author: He Zhigang
 * @Date: 2019/12/10 12:55
 * @Description:
 */
@Component
@Order(value = -100)
@Slf4j
@Aspect
public class DataSourceAspect {
    @Pointcut("execution(* com.example.dynamicdb." + DbGlobal.DB1 + ".mapper..*.*(..))")
    private void sourceAspect() {
    }

    @Pointcut("execution(* com.example.dynamicdb." + DbGlobal.DB2 + ".mapper..*.*(..))")
    private void targetAspect() {
    }

    @Before("sourceAspect()")
    public void source() {
        log.info("切换到{} 数据源...", DbGlobal.DB1);
        DataSourceContextHolder.setDbType(DbGlobal.DB1);
    }

    @Before("targetAspect()")
    public void target() {
        log.info("切换到{} 数据源...", DbGlobal.DB2);
        DataSourceContextHolder.setDbType(DbGlobal.DB2);
    }
}

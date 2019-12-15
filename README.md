项目依赖于mybatis-plus

连接池使用alibaba的druid

可以添加任意个数据源,通过AOP的方式进行动态切换

# 快速开始
### 动态切换数据源
1. `application.yml`数据源配置
```yaml
spring:
  aop:
    proxy-target-class: true
    auto: true
  datasource:
    source:
      driver-class-name: com.p6spy.engine.spy.P6SpyDriver
      url: jdbc:p6spy:mysql://localhost:3306/dynamic-source?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false
      username: ${dbUser:root}
      password: ${dbPass:wdkj@123#}
      db-type: mysql
      validation-query: "select 1"
      max-active: 16
      min-idle: 4
    target:
      driver-class-name: com.p6spy.engine.spy.P6SpyDriver
      url: jdbc:p6spy:mysql://localhost:3306/dynamic-target?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false
      username: root
      password: 123456
      db-type: mysql
      validation-query: "select 1"
    druid:
      filter:
        stat:
          enabled: true  #是否激活sql监控
        wall:
          enabled: true  #是否激活sql防火墙
      web-stat-filter:
        enabled: true
      stat-view-servlet:
        enabled: true
        url-pattern: "/druid/*"   #浏览器访问localhost:8080/druid，进入监控界面
        login-username: admin
        login-password: admin@123
```

2. 根据需求修改配置类`DBS`以及配置`DataSourceAspect`和`MybatisPlusConfig`类

### 自动生成代码
1.自动生成代码需要依赖
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>3.2.0</version>
</dependency>
```
2.在application.yml中打开开关
```yaml
mybatis-plus:
  auto-generator: true  ## 自动生成代码的开关，默认为false
```

以上准备好之后，just run!

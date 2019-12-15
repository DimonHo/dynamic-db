DROP TABLE IF EXISTS `dynamic-source`.user;

CREATE TABLE `dynamic-source`.user
(
  id      BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  gmt_create datetime null default now() comment '创建时间',
  gmt_modified datetime null default now() comment '修改时间',
  name    VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
  age     INT (11) NULL DEFAULT NULL COMMENT '年龄',
  email   VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
  operator VARCHAR(30) NULL DEFAULT NULL COMMENT '操作员',
  PRIMARY KEY (id)
);

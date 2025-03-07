drop table if exists route_mapping;
create table route_mapping
(
    id          bigint(20) not null auto_increment comment 'id',

    page_type   varchar(150) default '' comment '页面类型（如 "会议新闻"）',
    path varchar(255) default '' comment '跳转路径（如 "/news/latest"）',
    keywords varchar(255) default '' comment '关联关键词（如 "新闻,动态"）',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '路由映射表';
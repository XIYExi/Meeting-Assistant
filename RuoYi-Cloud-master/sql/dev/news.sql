drop table if exists news;
create table news
(
    id          bigint(20) not null auto_increment comment 'id',

    title       varchar(255) default '' comment '新闻标题',
    author      varchar(100) default '' comment '作者',
    url         varchar(255) default '' comment '封面',

    del_flag    char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '新闻表';
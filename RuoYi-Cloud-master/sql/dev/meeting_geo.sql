drop table if exists meeting_geo;
create table meeting_geo
(
    id          bigint(20) not null auto_increment comment 'id',

    country varchar(50) not null comment '国家',
    formatted_address varchar(255) not null comment '格式化地址',
    province varchar(50) default null comment '省份',
    city varchar(50) default null comment '城市',
    citycode varchar(50) default null comment '城市编码',
    district varchar(50) default null comment '区',
    street varchar(50) default null comment '街道',
    number varchar(50) default null comment '门牌',
    adcode varchar(50) default null comment '区域编码',
    location varchar(255) not null comment '经度，纬度',

    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '会议地图表';



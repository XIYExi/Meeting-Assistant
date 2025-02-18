drop table if exists living_room;
create table living_room
(
    id          bigint(20) not null auto_increment comment 'id',
    anchor_id   bigint(20)  default null comment '主播id',
    type        tinyint     default '0' comment '直播间类型',
    status      tinyint     default '0' comment '状态 0无效 1有效',
    room_name   varchar(60) default 0 comment '直播间名称',
    watch_name  int         default '0' comment '观看数量',
    good_num    int         default '0' comment '点赞数量',
    start_time  datetime    default current_timestamp comment '开播时间',

    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '在线直播间表';


drop table if exists living_room_record;
create table living_room_record
(
    id          bigint(20) not null auto_increment comment 'id',
    anchor_id   bigint(20)  default null comment '主播id',
    type        tinyint     default '0' comment '直播间类型',
    status      tinyint     default '0' comment '状态 0无效 1有效',
    room_name   varchar(60) default 0 comment '直播间名称',
    watch_name  int         default '0' comment '观看数量',
    good_num    int         default '0' comment '点赞数量',
    start_time  datetime    default current_timestamp comment '开播时间',
    end_time    datetime    default null comment '关播时间',

    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '直播间记录表';



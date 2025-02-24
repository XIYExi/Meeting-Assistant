drop table if exists points;
create table points
(
    id          bigint(20) not null auto_increment comment 'id',

    title   varchar(150) default '' comment '积分任务名称',
    description varchar(255) default '' comment '疾风任务描述',
    point integer default 0 comment '任务奖励积分数量',
    type integer default 1 comment '任务可获取次数 1-仅可完成一次 2-每日一次',

    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '积分表';


drop table if exists points_items;
create table points_items
(
    id          bigint(20) not null auto_increment comment 'id',

    title   varchar(150) default '' comment '物品名称',
    description varchar(255) default '' comment '物品描述',
    url varchar(200) default '' comment '图片',
    cost integer default 0 comment '对话花费',
    remaining integer default 0 comment '剩余存量',

    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '积分物品表';


drop table if exists points_record;
create table points_record
(
    id          bigint(20) not null auto_increment comment 'id',
    user_id  bigint(20) not null comment '用户id',
    point_id  bigint(20) not null comment '对应任务id',
    get_points integer default 0 comment '获得积分',

    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '积分记录表';


drop table if exists points_wallet;
create table points_wallet
(
    id          bigint(20) not null auto_increment comment 'id',
    user_id  bigint(20) not null comment '用户id',
    total integer default 0 comment '总积分',

    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '用户积分表';


drop table if exists points_exchange;
create table points_exchange
(
    id          bigint(20) not null auto_increment comment 'id',
    user_id  bigint(20) not null comment '用户id',
    item_id bigint(20) not null comment '物品id',
    cost integer default 0 comment '兑换花费',

    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '用户积分表';
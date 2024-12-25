--
drop table if exists meeting;
create table meeting
(
    id           bigint(20) not null auto_increment comment '会议表id',
    title        varchar(100) not null comment '会议名称',
    begin_time   datetime     not null comment '会议开始时间',
    end_time     datetime     not null comment '会议结束时间',
    location     varchar(100) not null comment '会议地点',
    url          varchar(100) not null comment '会议封面海报图',
    views        integer      default 0 comment '查看次数',
    type         integer      default 1 comment '会议类型',
    status       integer      default 1 comment '会议状态',
    meeting_type integer      default 1 comment '会议开展类型',

    del_flag     char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    remark       varchar(500) default null comment '备注',
    primary key (id)
) engine=innodb comment = '会议表';

--
drop table if exists meeting_agenda;
create table meeting_agenda
(
    id          bigint(20) not null auto_increment  comment '子议程id',
    meeting_id  bigint(20) not null comment '会议论坛id',
    begin_time  datetime     not null comment '子议程开始时间',
    end_time    datetime     not null comment '子议程结束时间',
    content     varchar(255) not null comment '子议程内容',
    meta        varchar(2000) comment '主讲人信息',

    del_flag    char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine=innodb comment = '会议议程表';


--
drop table if exists meeting_chat;
create table meeting_chat
(
    id          bigint(20) not null auto_increment  comment '聊天表id',
    meeting_id  bigint(20) not null comment '会议表id',
    content     varchar(255) not null comment '聊天信息',

    del_flag    char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine=innodb comment = '会议聊天表';


--
drop table if exists meeting_activity_sector;
create table meeting_activity_sector
(
    id          bigint(20) not null auto_increment  comment '活动板块id',
    title       varchar(100)  not null comment '板块标题',
    description varchar(2000) not null comment '板块内容',

    del_flag    char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
)engine=innodb comment = '会议活动板块表';

--
drop table if exists meeting_activity;
create table meeting_activity
(
    id          bigint(20) not null auto_increment  comment '板块活动id',
    sector_id   bigint(20) not null comment '板块id',
    title       varchar(100) not null comment '活动标题',
    time        datetime     not null comment '活动时间',
    url         varchar(255) not null comment '活动封面海报',
    type        integer      default 1 comment '活动类型',
    content     longtext comment '内容',

    del_flag    char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
)engine=innodb comment = '会议活动表';

--
drop table if exists meeting_guest;
create table meeting_guest
(
    id          bigint(20) not null auto_increment  comment '嘉宾表id',
    name        varchar(50)  not null comment '嘉宾姓名',
    title       varchar(100) not null comment '嘉宾头衔',
    content     varchar(200) not null comment '嘉宾主讲内容',
    views       int          default 0 comment '查看统计',
    likes       int          default 0 comment '点赞统计',
    avatar      varchar(255) not null comment '嘉宾头像',
    url         varchar(255) not null comment '嘉宾海报',

    del_flag    char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
)engine=innodb comment = '会议嘉宾表';

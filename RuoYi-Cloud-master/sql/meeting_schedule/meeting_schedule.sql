
--
drop table if exists meeting_schedule;
create table meeting_schedule
(
    id           bigint(20) not null auto_increment comment '会议预约表id',
    meeting_id   bigint(20) not null comment '会议id',
    user_id      bigint(20) not null comment '用户id',
    title        varchar(100) not null comment '会议名称',
    begin_time   datetime     not null comment '会议开始时间',
    end_time     datetime     not null comment '会议结束时间',
    phone       varchar(100) not null comment '电话',

    del_flag     char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    remark       varchar(500) default null comment '备注',
    primary key (id)
) engine=innodb comment = '会议预约表';
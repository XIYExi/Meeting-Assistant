drop table if exists meeting_clip;
create table meeting_clip
(
    id          bigint(20) not null auto_increment comment 'id',
    meeting_id   bigint(20)  default null comment '会议id',
    agenda_id        bigint(20)    default null comment '议程id',
    clip_type int default 1 comment '附件类型，1表示总附件查询meetingId， 2表示子议程附件，查询agendaId，3表示软件附件，不在会议详细中展示',

    file_name varchar(200) default '' comment '文件名称',
    upload_user_id bigint(20) default null comment '上传者',
    file_type varchar(100) default '' comment '文件类型',
    file_size integer default 0 comment '文件大小',
    url varchar(200) default '' comment 'cos存储地址',


    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '会议附件表';



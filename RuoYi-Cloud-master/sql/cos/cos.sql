
drop table if exists image;
create table image (
    id bigint(20) not null auto_increment comment '图片编号',
    url varchar(255) not null comment '图片链接',
    del_flag     char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    remark       varchar(500) default null comment '备注',
    primary key (id)
) engine=innodb comment = '图片cos表';
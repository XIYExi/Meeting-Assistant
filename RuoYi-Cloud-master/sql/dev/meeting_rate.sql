drop table if exists meeting_rate;
create table meeting_rate
(
    id          bigint(20) not null auto_increment comment 'id',
    meeting_id  bigint(20) not null  comment '会议id',
    rate    double(20, 19) default 0 comment 'softmax得分',
    primary key (id)
) engine = innodb
  auto_increment = 200 comment = '会议评分表';
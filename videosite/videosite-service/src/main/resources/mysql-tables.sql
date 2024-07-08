-- 视频网站数据库
drop database if exists videosite;
create database if not exists videosite;

drop table if exists t_user_authority;
drop table if exists t_user;
drop table if exists t_authority;
drop table if exists t_video_history;
drop table if exists t_comment;
drop table if exists t_video;
drop table if exists t_category;

create table if not exists t_user (
    id                      int          auto_increment primary key                   comment '主键',
    username                varchar(64)  not null       unique                        comment '用户名',
    nickname                varchar(64)  not null                                     comment '昵称',
    password                varchar(256) not null                                     comment '密码',
    user_type               varchar(32)  not null                                     comment '用户类型',
    created_date            datetime     not null       default now()                 comment '创建时间',
    creator                 varchar(64)  not null                                     comment '创建人',
    modified_date           datetime     not null       default now() on update now() comment '修改时间',
    modifier                varchar(64)  not null                                     comment '修改人',
    account_non_expired     bit          not null       default 1                     comment '帐户是否未过期',
    account_non_locked      bit          not null       default 1                     comment '帐户是否未锁定',
    credentials_non_expired bit          not null       default 1                     comment '密码是否未过期',
    enabled                 bit          not null       default 1                     comment '帐户是否已启用'
) comment '用户表';

create table if not exists t_authority (
    id            int          auto_increment primary key                   comment '权限主键',
    auth_name     varchar(255) not null                                     comment '权限中文名称',
    authority     varchar(255) not null       unique                        comment '权限编码',
    created_date  datetime     not null       default now()                 comment '创建时间',
    creator       varchar(64)  not null                                     comment '创建人',
    modified_date datetime     not null       default now() on update now() comment '修改时间',
    modifier      varchar(64)  not null                                     comment '修改人'
) comment '权限表';

create table if not exists t_user_authority (
    id            int          auto_increment primary key                   comment '关系主键',
    user_id       int          not null                                     comment '用户主键',
    auth_id       int          not null                                     comment '权限主键',
    created_date  datetime     not null       default now()                 comment '创建时间',
    foreign key (user_id) references t_user (id) on delete cascade,
    foreign key (auth_id) references t_authority (id) on delete cascade,
    unique key (user_id, auth_id)
) comment '用户权限关系表';

create table if not exists t_category (
    id            int         auto_increment primary key                   comment '主键',
    category_name varchar(64) not null       unique                        comment '类别名称',
    created_date  datetime    not null       default now()                 comment '创建时间',
    creator       varchar(64) not null                                     comment '创建人',
    modified_date datetime    not null       default now() on update now() comment '修改时间',
    modifier      varchar(64) not null                                     comment '修改人'
) comment '视频类别表';

create table if not exists t_video (
    id            int          auto_increment primary key                   comment '主键',
    video_name    varchar(128) not null                                     comment '视频名称',
    video_link    varchar(256) not null                                     comment '视频链接',
    video_logo    varchar(256) not null                                     comment '视频封面',
    video_hits    int          not null       default 0                     comment '视频播放量',
    category_id   int          not null                                     comment '视频类别主键',
    created_date  datetime     not null       default now()                 comment '创建时间',
    creator       varchar(64)  not null                                     comment '创建人',
    modified_date datetime     not null       default now() on update now() comment '修改时间',
    modifier      varchar(64)  not null                                     comment '修改人',
    audit_status  varchar(32)  not null                                     comment '审核状态',
    audited_date  datetime                                                  comment '审核时间',
    auditor       varchar(64)                                               comment '审核人',
    audit_reason  varchar(256)                                              comment '审核不通过原因',
    foreign key (category_id) references t_category (id) on delete cascade
) comment '视频表';

create table if not exists t_video_history (
    id            int          auto_increment primary key                   comment '主键',
    video_id      int          not null                                     comment '视频主键',
    play_count    int          not null       default 0                     comment '视频播放量',
    created_date  datetime     not null       default now()                 comment '创建时间',
    creator       varchar(64)  not null                                     comment '创建人',
    modified_date datetime     not null       default now() on update now() comment '修改时间',
    modifier      varchar(64)  not null                                     comment '修改人',
    foreign key (video_id) references t_video (id) on delete cascade,
    unique key (video_id, creator, modifier)
) comment '视频观看历史表';

create table if not exists t_comment (
    id            int          auto_increment primary key                   comment '主键',
    content       varchar(256) not null                                     comment '评论内容',
    video_id      int          not null                                     comment '视频主键',
    created_date  datetime     not null       default now()                 comment '创建时间',
    creator       varchar(64)  not null                                     comment '创建人',
    modified_date datetime     not null       default now() on update now() comment '修改时间',
    modifier      varchar(64)  not null                                     comment '修改人',
    foreign key (video_id) references t_video (id) on delete cascade
) comment '视频评论表';

-- 初始化数据
insert into t_user (username, nickname, password, user_type, creator, modifier)
values ('admin', '管理员', '{bcrypt}$2a$10$GG/O6kejMQvT/nVQAzCduuUqukadG/nSLZOUIyuNLRm./FwvqEiQC', 'ADMIN', 'admin', 'admin');

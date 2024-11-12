-- 删除表
drop table if exists t_user_authority;
drop table if exists t_user;
drop table if exists t_authority;
drop table if exists t_video_history;
drop table if exists t_comment;
drop table if exists t_video;
drop table if exists t_category;
drop table if exists t_json_web_token;

-- 删除序列
drop sequence if exists t_user_id_seq;
drop sequence if exists t_authority_id_seq;
drop sequence if exists t_category_id_seq;
drop sequence if exists t_video_id_seq;
drop sequence if exists t_video_history_id_seq;
drop sequence if exists t_comment_id_seq;

-- 创建序列
create sequence if not exists t_user_id_seq;
comment on sequence t_user_id_seq is '用户表主键序列';
create sequence if not exists t_authority_id_seq;
comment on sequence t_authority_id_seq is '权限表主键序列';
create sequence if not exists t_category_id_seq;
comment on sequence t_category_id_seq is '视频类别表主键序列';
create sequence if not exists t_video_id_seq;
comment on sequence t_video_id_seq is '视频表主键序列';
create sequence if not exists t_video_history_id_seq;
comment on sequence t_video_history_id_seq is '视频观看历史表主键序列';
create sequence if not exists t_comment_id_seq;
comment on sequence t_comment_id_seq is '视频评论表主键序列';

-- 创建表
create table if not exists t_json_web_token (
    jwt_id          varchar(64)  not null primary key,
    subject         varchar(64)  not null,
    issuer          varchar(64)  not null,
    issued_at       timestamp    not null,
    expiration_time timestamp    not null,
    not_before      timestamp    not null default now(),
    audience        varchar(256) null,
    token           text         not null
);
comment on table t_json_web_token is 'JSON令牌表';
comment on column t_json_web_token.jwt_id is '令牌的唯一标识符';
comment on column t_json_web_token.subject is '令牌的主题，通常是用户的唯一标识符';
comment on column t_json_web_token.issuer is '令牌的发行者';
comment on column t_json_web_token.issued_at is '令牌的颁发时间，即创建时间';
comment on column t_json_web_token.expiration_time is '令牌的过期时间';
comment on column t_json_web_token.not_before is '令牌的生效时间，即创建时间';
comment on column t_json_web_token.audience is '令牌的受众，表示该令牌针对哪些接收者';
comment on column t_json_web_token.token is 'JSON令牌';

create table if not exists t_user (
  id                      int          default nextval('t_user_id_seq'::regclass) primary key,
  username                varchar(64)  not null                                   unique,
  nickname                varchar(64)  not null,
  password                varchar(256) not null,
  user_type               varchar(32)  null,
  created_date            timestamp    not null                                   default now(),
  creator                 varchar(64)  not null,
  modified_date           timestamp    not null                                   default now(),
  modifier                varchar(64)  not null,
  account_non_expired     boolean      not null                                   default '1',
  account_non_locked      boolean      not null                                   default '1',
  credentials_non_expired boolean      not null                                   default '1',
  enabled                 boolean      not null                                   default '1'
);
comment on table t_user is '用户表';
comment on column t_user.id is '主键';
comment on column t_user.username is '用户名';
comment on column t_user.nickname is '昵称';
comment on column t_user.password is '密码';
comment on column t_user.user_type is '用户类型';
comment on column t_user.created_date is '创建时间';
comment on column t_user.creator is '创建人';
comment on column t_user.modified_date is '修改时间';
comment on column t_user.modifier is '修改人';
comment on column t_user.account_non_expired is '帐户是否未过期';
comment on column t_user.account_non_locked is '帐户是否未锁定';
comment on column t_user.credentials_non_expired is '密码是否未过期';
comment on column t_user.enabled is '帐户是否已启用';

create table if not exists t_authority (
  id            int          default nextval('t_authority_id_seq'::regclass) primary key,
  auth_name     varchar(255) not null,
  authority     varchar(255) not null                                        unique,
  created_date  timestamp    not null                                        default now(),
  creator       varchar(64)  not null,
  modified_date timestamp    not null                                        default now(),
  modifier      varchar(64)  not null
);
comment on table t_authority is '权限表';
comment on column t_authority.id is '权限主键';
comment on column t_authority.auth_name is '权限中文名称';
comment on column t_authority.authority is '权限编码';
comment on column t_authority.created_date is '创建时间';
comment on column t_authority.creator is '创建人';
comment on column t_authority.modified_date is '修改时间';
comment on column t_authority.modifier is '修改人';

create table if not exists t_user_authority (
  user_id int not null,
  auth_id int not null,
  foreign key (user_id) references t_user (id) on delete cascade,
  foreign key (auth_id) references t_authority (id) on delete cascade,
  unique (user_id, auth_id)
);
comment on table t_user_authority is '用户权限关系表';
comment on column t_user_authority.user_id is '用户主键';
comment on column t_user_authority.auth_id is '权限主键';

create table if not exists t_category (
  id            int         default nextval('t_category_id_seq'::regclass) primary key,
  category_name varchar(64) not null                                       unique,
  created_date  timestamp   not null                                       default now(),
  creator       varchar(64) not null,
  modified_date timestamp   not null                                       default now(),
  modifier      varchar(64) not null
);
comment on table t_category is '视频类别表';
comment on column t_category.id is '主键';
comment on column t_category.category_name is '类别名称';
comment on column t_category.created_date is '创建时间';
comment on column t_category.creator is '创建人';
comment on column t_category.modified_date is '修改时间';
comment on column t_category.modifier is '修改人';

create table if not exists t_video (
  id                int          default nextval('t_video_id_seq'::regclass) primary key,
  video_name        varchar(128) not null,
  video_link        varchar(256) not null,
  video_link_md5    varchar(32)  not null,
  video_logo        varchar(256) not null,
  video_hits        int          not null                                    default 0,
  category_id       int          not null,
  created_date      timestamp    not null                                    default now(),
  creator           varchar(64)  not null,
  modified_date     timestamp    not null                                    default now(),
  modifier          varchar(64)  not null,
  audit_status      varchar(32)  not null,
  audited_date      timestamp,
  auditor           varchar(64),
  audit_reason      varchar(256),
  foreign key (category_id) references t_category (id) on delete cascade
);
comment on table t_video is '视频表';
comment on column t_video.id is '主键';
comment on column t_video.video_name is '视频名称';
comment on column t_video.video_link is '视频链接';
comment on column t_video.video_link_md5 is '视频链接MD5';
comment on column t_video.video_logo is '视频封面';
comment on column t_video.video_hits is '视频播放量';
comment on column t_video.category_id is '视频类别主键';
comment on column t_video.created_date is '创建时间';
comment on column t_video.creator is '创建人';
comment on column t_video.modified_date is '修改时间';
comment on column t_video.modifier is '修改人';
comment on column t_video.audit_status is '审核状态';
comment on column t_video.audited_date is '审核时间';
comment on column t_video.auditor is '审核人';
comment on column t_video.audit_reason is '审核不通过原因';

create table if not exists t_video_history (
  id            int          default nextval('t_video_history_id_seq'::regclass) primary key,
  video_id      int          not null,
  play_count    int          not null                                            default 0,
  created_date  timestamp    not null                                            default now(),
  creator       varchar(64)  not null,
  modified_date timestamp    not null                                            default now(),
  modifier      varchar(64)  not null,
  foreign key (video_id) references t_video (id) on delete cascade,
  unique (video_id, creator, modifier)
);
comment on table t_video_history is '视频观看历史表';
comment on column t_video_history.id is '主键';
comment on column t_video_history.video_id is '视频主键';
comment on column t_video_history.play_count is '视频播放量';
comment on column t_video_history.created_date is '创建时间';
comment on column t_video_history.creator is '创建人';
comment on column t_video_history.modified_date is '修改时间';
comment on column t_video_history.modifier is '修改人';

create table if not exists t_comment (
  id            int          default nextval('t_comment_id_seq'::regclass) primary key,
  content       varchar(256) not null,
  video_id      int          not null,
  created_date  timestamp    not null                                      default now(),
  creator       varchar(64)  not null,
  modified_date timestamp    not null                                      default now(),
  modifier      varchar(64)  not null,
  foreign key (video_id) references t_video (id) on delete cascade
);
comment on table t_comment is '视频评论表';
comment on column t_comment.id is '主键';
comment on column t_comment.content is '评论内容';
comment on column t_comment.video_id is '视频主键';
comment on column t_comment.created_date is '创建时间';
comment on column t_comment.creator is '创建人';
comment on column t_comment.modified_date is '修改时间';
comment on column t_comment.modifier is '修改人';

-- 初始化数据
insert into t_user (username, nickname, password, creator, modifier)
values ('admin', '管理员', '{bcrypt}$2a$10$GG/O6kejMQvT/nVQAzCduuUqukadG/nSLZOUIyuNLRm./FwvqEiQC', 'admin', 'admin');
insert into t_authority (auth_name, authority, creator, modifier)
values ('系统管理员', 'ROLE_ADMIN', 'admin', 'admin'), ('普通用户', 'ROLE_NORMAL', 'admin', 'admin');
insert into t_user_authority (user_id, auth_id)
values ((select id from t_user where username = 'admin'), (select id from t_authority where authority = 'ROLE_ADMIN'));

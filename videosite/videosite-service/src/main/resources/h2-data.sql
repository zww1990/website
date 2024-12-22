insert into t_user (username, nickname, password, creator, modifier)
values ('admin', '管理员', '{bcrypt}$2a$10$GG/O6kejMQvT/nVQAzCduuUqukadG/nSLZOUIyuNLRm./FwvqEiQC', 'admin', 'admin');
insert into t_authority (auth_name, authority, creator, modifier)
values ('系统管理员', 'ROLE_ADMIN', 'admin', 'admin'), ('普通用户', 'ROLE_NORMAL', 'admin', 'admin');
insert into t_user_authority (user_id, auth_id)
values ((select id from t_user where username = 'admin'), (select id from t_authority where authority = 'ROLE_ADMIN'));

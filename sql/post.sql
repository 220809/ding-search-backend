create database if not exists your_db;

create table your_db.post
(
    id          bigint auto_increment comment '主键',
    title       varchar(256) default '标题'            not null comment '标题',
    content     text                                  not null comment '帖子内容',
    tags        varchar(512) default '[]'              not null comment '标签',
    author_id   bigint                                 null comment '作者id',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     tinyint      default 0                 not null comment '是否删除（0-未删除，1-已删除）',
    constraint post_pk
        primary key (id)
)
    comment '帖子';
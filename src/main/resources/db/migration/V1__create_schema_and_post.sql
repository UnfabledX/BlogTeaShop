CREATE SCHEMA IF NOT EXISTS blog_service;

create table if not exists blog_service.posts (
        id          bigserial,
        title       character varying,
        subtitle    character varying,
        content     character varying,
        author      character varying,
        primary key (id)
);
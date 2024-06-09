CREATE SCHEMA IF NOT EXISTS blog_service;

create table if not exists blog_service.posts (
        id          bigserial,
        title       character varying not null,
        subtitle    character varying,
        content     character varying not null,
        author      character varying not null,
        created_at  timestamp not null,
        primary key (id)
);
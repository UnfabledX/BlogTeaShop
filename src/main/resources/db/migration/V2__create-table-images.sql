create table if not exists blog_service.images
(
    image_id    bigint not null unique,
    name        character varying unique,
    post_id     bigint not null,
    primary key (image_id),
    CONSTRAINT fk_post_image
        FOREIGN KEY (post_id)
            REFERENCES blog_service.posts (id)
            ON DELETE CASCADE
)
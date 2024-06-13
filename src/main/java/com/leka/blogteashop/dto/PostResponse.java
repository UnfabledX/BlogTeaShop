package com.leka.blogteashop.dto;

import lombok.*;

@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String subtitle;
    private String content;
    private String author;
    private String createdAt;
    private Long backgroundImageId;
}

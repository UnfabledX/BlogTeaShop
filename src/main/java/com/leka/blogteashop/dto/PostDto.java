package com.leka.blogteashop.dto;

import lombok.*;

@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String titleUA;
    private String titleEN;
    private String subtitleUA;
    private String subtitleEN;
    private String contentUA;
    private String contentEN;

}

package com.leka.blogteashop.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditPostDto {

    private Long id;
    @NotEmpty(message = "{notEmpty.titleUA}")
    private String titleUA;
    @NotEmpty(message = "{notEmpty.titleEN}")
    private String titleEN;
    @NotEmpty(message = "{notEmpty.subtitleUA}")
    private String subtitleUA;
    @NotEmpty(message = "{notEmpty.subtitleEN}")
    private String subtitleEN;
    @NotEmpty(message = "{notEmpty.contentUA}")
    private String contentUA;
    @NotEmpty(message = "{notEmpty.contentEN}")
    private String contentEN;
    private Long backgroundImageId;

}

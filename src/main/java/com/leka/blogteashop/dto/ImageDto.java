package com.leka.blogteashop.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {
    private Long id;
    private String fileName;
    private String fileType;
    private Long size;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private byte[] data;
}

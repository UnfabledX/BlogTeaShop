package com.leka.blogteashop.service;

import com.leka.blogteashop.dto.ImageDto;
import org.springframework.data.domain.Page;
import org.springframework.http.client.MultipartBodyBuilder;

public interface MediaService {

    ImageDto uploadImageThrough(MultipartBodyBuilder builder);

    void deleteImageById(Long imageId);

    ImageDto updateImageById(Long imageId, MultipartBodyBuilder builder);

    Page<ImageDto> getAllImages();

    ImageDto getImageByIdWithData(Long id);

    void deleteAllImages();
}

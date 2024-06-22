package com.leka.blogteashop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validator {

    public static boolean validateImageNamingInContent(List<MultipartFile> images, String ... contents) {
        if (!images.isEmpty() && !images.get(0).isEmpty()) {
            for (String content : contents) {
                for (MultipartFile image : images) {
                    String target = "@{%s}".formatted(image.getOriginalFilename());
                    if (!StringUtils.contains(content, target)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

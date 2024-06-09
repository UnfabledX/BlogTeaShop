package com.leka.blogteashop.service;

import com.leka.blogteashop.dto.PostDto;
import com.leka.blogteashop.dto.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    PostResponse addPost(PostDto postDto, MultipartFile bgFile, List<MultipartFile> postFiles);

}

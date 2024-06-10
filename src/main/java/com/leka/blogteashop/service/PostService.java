package com.leka.blogteashop.service;

import com.leka.blogteashop.dto.PostDto;
import com.leka.blogteashop.dto.PostResponse;
import com.leka.blogteashop.dto.PostResponseOnlyId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    PostResponseOnlyId addPost(PostDto postDto, MultipartFile bgFile, List<MultipartFile> postFiles);

    PostResponse getPostById(Long postId);
}

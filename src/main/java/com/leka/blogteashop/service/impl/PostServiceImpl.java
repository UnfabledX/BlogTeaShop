package com.leka.blogteashop.service.impl;

import com.leka.blogteashop.dto.PostDto;
import com.leka.blogteashop.dto.PostResponse;
import com.leka.blogteashop.repository.PostRepository;
import com.leka.blogteashop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public PostResponse addPost(PostDto postDto, MultipartFile bgFile, List<MultipartFile> postFiles) {
        return null;
    }

}

package com.leka.blogteashop.service.impl;

import com.leka.blogteashop.dto.ImageDto;
import com.leka.blogteashop.dto.PostDto;
import com.leka.blogteashop.dto.PostResponse;
import com.leka.blogteashop.dto.PostResponseOnlyId;
import com.leka.blogteashop.exception.NotFoundException;
import com.leka.blogteashop.mapper.PostMapper;
import com.leka.blogteashop.model.Image;
import com.leka.blogteashop.model.Post;
import com.leka.blogteashop.repository.PostRepository;
import com.leka.blogteashop.service.MediaService;
import com.leka.blogteashop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    public static final String PREFIX_BACKGROUND_IMAGE = "background";

    private final MediaService mediaService;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional
    public PostResponseOnlyId addPost(PostDto postDto, MultipartFile bgFile, List<MultipartFile> postFiles) {
        Post post = postMapper.toEntity(postDto);
        MultipartBodyBuilder builder;
        if (bgFile != null && !bgFile.isEmpty()) {
            builder = createFrom(bgFile, true);
            ImageDto bgImage = mediaService.uploadImageThrough(builder);
            post.addImage(new Image(bgImage.getId(), bgImage.getFileName(), post));
        }
        if (!postFiles.isEmpty() && !postFiles.get(0).isEmpty()) {
            for (MultipartFile postFile : postFiles) {
                builder = createFrom(postFile, false);
                ImageDto postImage = mediaService.uploadImageThrough(builder);
                post.addImage(new Image(postImage.getId(), postImage.getFileName(), post));
            }
        }
        return postMapper.toResponseOnlyId(postRepository.save(post));
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        return postMapper.toResponse(post);
    }


    private MultipartBodyBuilder createFrom(MultipartFile file, boolean isBackground) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        try {
            if (file != null && file.getOriginalFilename() != null) {
                String filename = isBackground ?
                        PREFIX_BACKGROUND_IMAGE + file.getOriginalFilename() :
                        file.getOriginalFilename();
                builder.part("file", new ByteArrayResource(file.getBytes()))
                        .filename(filename);
            } else {
                throw new NotFoundException("The file is not provided");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder;
    }

}

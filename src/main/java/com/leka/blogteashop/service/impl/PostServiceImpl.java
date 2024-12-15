package com.leka.blogteashop.service.impl;

import com.leka.blogteashop.dto.*;
import com.leka.blogteashop.exception.NotFoundException;
import com.leka.blogteashop.mapper.PostMapper;
import com.leka.blogteashop.model.Image;
import com.leka.blogteashop.model.Post;
import com.leka.blogteashop.repository.PostRepository;
import com.leka.blogteashop.service.MediaService;
import com.leka.blogteashop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;
import java.util.List;

import static com.leka.blogteashop.controller.BlogController.ABOUT_ME_TITLE;

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
        if (bgFile != null && !bgFile.isEmpty()) {
            uploadBackgroundImageToPost(bgFile, post);
        }
        addPostImages(postFiles, post);
        return postMapper.toResponseOnlyId(postRepository.save(post));
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId) {
        Post post = getPost(postId);
        return postMapper.toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public EditPostDto getEditPostDtoById(Long postId) {
        Post post = getPost(postId);
        return postMapper.toEditPostDto(post);
    }

    @Override
    @Transactional
    public void editPost(Long postId, EditPostDto postDto, MultipartFile bgFile, List<MultipartFile> postFiles) {
        Post post = getPost(postId);
        postMapper.updatePost(post, postDto);
        List<Image> postImages = post.getPostImages();
        MultipartBodyBuilder builder;
        if (bgFile != null && !bgFile.isEmpty()) {
            boolean isUpdated = false;
            for (Image image : postImages) {
                if (image.getImageName().startsWith(PREFIX_BACKGROUND_IMAGE)) {
                    builder = createFrom(bgFile, true);
                    Long imageId = image.getImageId();
                    ImageDto imageDto = mediaService.updateImageById(imageId, builder);
                    image.setImageName(imageDto.getFileName());
                    isUpdated = true;
                    break;
                }
            }
            if (!isUpdated) {
                uploadBackgroundImageToPost(bgFile, post);
            }
        }
        //delete old files by checking that the image name is not found in updated contents of EditPostDto
        Iterator<Image> iterator = postImages.iterator();
        while (iterator.hasNext()) {
            Image image = iterator.next();
            if (image.getImageName().startsWith(PREFIX_BACKGROUND_IMAGE)) continue;
            String target = "@{%s}".formatted(image.getImageName());
            //in order to delete old images from database, we have to check that all contents in different languages
            //don't include references to image names. If the admin forgets to delete in English version for example,
            // the image won't be deleted in general and will be shown in English content only.
            if (!StringUtils.contains(postDto.getContentUA(), target)
                    && !StringUtils.contains(postDto.getContentEN(), target)) {
                Long imageId = image.getImageId();
                mediaService.deleteImageById(imageId);
                iterator.remove();
            }
        }
        //upload new ones if present
        addPostImages(postFiles, post);
        post.setPostImages(postImages);
        postRepository.save(post);
    }

    @Override
    public Page<PostResponse> getPosts(Pageable pageable) {
        return postRepository.findAllByTitleNotContaining(pageable, ABOUT_ME_TITLE)
                .map(postMapper::toResponse);
    }

    @Override
    public PostResponse getPostByTitle(String title) {
        Post post;
        if (postRepository.existsByTitleContaining(title)) {
            post = postRepository.findByTitleContaining(title);
        } else {
            post = postRepository.save(getDefaultInfoAboutMe());
        }
        return postMapper.toResponse(post);
    }


    @Override
    @Transactional
    public void editAbout(EditPostDto postDto, MultipartFile bgFile) {
        Post post = getPost(postDto.getId());
        postMapper.updatePost(post, postDto);
        List<Image> postImages = post.getPostImages();
        if (bgFile != null && !bgFile.isEmpty()) {
            boolean isUpdated = false;
            for (Image image : postImages) {
                if (image.getImageName().startsWith(PREFIX_BACKGROUND_IMAGE)) {
                    MultipartBodyBuilder builder = createFrom(bgFile, true);
                    Long imageId = image.getImageId();
                    ImageDto imageDto = mediaService.updateImageById(imageId, builder);
                    image.setImageName(imageDto.getFileName());
                    isUpdated = true;
                    break;
                }
            }
            if (!isUpdated) {
                uploadBackgroundImageToPost(bgFile, post);
            }
        }
        postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public EditPostDto getEditPostByTitle(String aboutMeTitle) {
        Post post = postRepository.findByTitleContaining(aboutMeTitle);
        return postMapper.toEditPostDto(post);
    }

    @Override
    @Transactional
    public void deleteById(Long postId) {
        postRepository.findById(postId).ifPresent(post -> {
            List<Image> postImages = post.getPostImages();
            if (postImages != null && !postImages.isEmpty()) {
                postImages.forEach(image -> mediaService.deleteImageById(image.getImageId()));
            }
            postRepository.delete(post);
        });
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

    private void addPostImages(List<MultipartFile> postFiles, Post post) {
        MultipartBodyBuilder builder;
        if (!postFiles.isEmpty() && !postFiles.get(0).isEmpty()) {
            for (MultipartFile postFile : postFiles) {
                builder = createFrom(postFile, false);
                ImageDto postImage = mediaService.uploadImageThrough(builder);
                post.addImage(new Image(postImage.getId(), postImage.getFileName(), post));
            }
        }
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));
    }

    private void uploadBackgroundImageToPost(MultipartFile bgFile, Post post) {
        MultipartBodyBuilder builder = createFrom(bgFile, true);
        ImageDto bgImage = mediaService.uploadImageThrough(builder);
        post.addImage(new Image(bgImage.getId(), bgImage.getFileName(), post));
    }


    private static Post getDefaultInfoAboutMe() {
        return Post.builder()
                .title("Про мене#&#" + ABOUT_ME_TITLE)
                .subtitle("Чим я займаюсь#&#This is what I do.")
                .content("<p>Необхідно заповнити</p>#&#<p>Need to fill it up</p>")
                .author("admin")
                .build();
    }
}

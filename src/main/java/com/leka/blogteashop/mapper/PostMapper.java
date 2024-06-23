package com.leka.blogteashop.mapper;

import com.leka.blogteashop.dto.EditPostDto;
import com.leka.blogteashop.dto.PostDto;
import com.leka.blogteashop.dto.PostResponse;
import com.leka.blogteashop.dto.PostResponseOnlyId;
import com.leka.blogteashop.model.Image;
import com.leka.blogteashop.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.leka.blogteashop.service.impl.PostServiceImpl.PREFIX_BACKGROUND_IMAGE;

@Component
@RequiredArgsConstructor
public class PostMapper {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public Post toEntity(PostDto postDto) {
        if (postDto == null) return null;
        Post.PostBuilder postBuilder = Post.builder();
        if (postDto.getId() != null) {
            postBuilder.id(postDto.getId());
        }
        return postBuilder
                .title(postDto.getTitleUA() + "#&#" + postDto.getTitleEN())
                .subtitle(postDto.getSubtitleUA() + "#&#" + postDto.getSubtitleEN())
                .content(postDto.getContentUA() + "#&#" + postDto.getContentEN())
                .postImages(new ArrayList<>())
                .build();
    }

    public PostResponse toResponse(Post post) {
        if (post == null) return new PostResponse();
        String content = post.getContent();
        List<Image> postImages = post.getPostImages();
        Long backgroundImageId = null;
        if (postImages != null && !postImages.isEmpty()) {
            for (Image postImage : postImages) {
                if (postImage.getImageName().startsWith(PREFIX_BACKGROUND_IMAGE)) {
                    backgroundImageId = postImage.getImageId();
                } else {
                    String target = Pattern.quote("@{%s}".formatted(postImage.getImageName()));
                    String replacement = "%s/image/%d".formatted(contextPath, postImage.getImageId());
                    content = content.replaceAll(target, replacement);
                }
            }
        }
        LocalDateTime createdAt = post.getCreatedAt();
        //August 24, 2024
        String formattedTime = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(createdAt);
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .subtitle(post.getSubtitle())
                .content(content)
                .author(post.getAuthor())
                .createdAt(formattedTime)
                .backgroundImageId(backgroundImageId)
                .build();
    }

    public PostResponseOnlyId toResponseOnlyId(Post post) {
        if (post == null) return new PostResponseOnlyId();
        return PostResponseOnlyId.builder().id(post.getId()).build();
    }

    public EditPostDto toEditPostDto(Post post) {
        String title = post.getTitle();
        String[] titles = title.split("#&#");
        String subtitle = post.getSubtitle();
        String[] subtitles = subtitle.split("#&#");
        String content = post.getContent();
        String[] contents = content.split("#&#");
        List<Image> postImageNames = post.getPostImages();
        Long backgroundImageId = null;
        for (Image postImage : postImageNames) {
            if (postImage.getImageName().startsWith(PREFIX_BACKGROUND_IMAGE)) {
                backgroundImageId = postImage.getImageId();
                break;
            }
        }
        return EditPostDto.builder()
                .id(post.getId())
                .titleUA(titles[0])
                .titleEN(titles[1])
                .subtitleUA(subtitles[0])
                .subtitleEN(subtitles[1])
                .contentUA(contents[0])
                .contentEN(contents[1])
                .backgroundImageId(backgroundImageId)
                .build();
    }

    public void updatePost(Post post, EditPostDto postDto) {
        post.setTitle(postDto.getTitleUA() + "#&#" + postDto.getTitleEN());
        post.setSubtitle(postDto.getSubtitleUA() + "#&#" + postDto.getSubtitleEN());
        post.setContent(postDto.getContentUA() + "#&#" + postDto.getContentEN());
    }
}

package com.leka.blogteashop.controller;

import com.leka.blogteashop.dto.ImageDto;
import com.leka.blogteashop.dto.PostDto;
import com.leka.blogteashop.dto.PostResponse;
import com.leka.blogteashop.dto.PostResponseOnlyId;
import com.leka.blogteashop.service.MediaService;
import com.leka.blogteashop.service.PostService;
import com.leka.blogteashop.service.impl.AuthService;
import com.leka.blogteashop.service.jwt.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
public class BlogController {

    private final JwtService jwtService;
    private final AuthService authService;
    private final PostService postService;
    private final MediaService mediaService;

    @GetMapping("/about")
    public String getAbout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("{} {}", authentication.getPrincipal().toString(), authentication.getAuthorities());
        return "about";
    }

    @GetMapping("/contact")
    public String getContacts() {
        return "contact";
    }

    @GetMapping("/main")
    public String getMainPage(@RequestParam(value = "token", required = false) String token,
                              HttpServletRequest request) {
        if (token == null || jwtService.isTokenExpired(token)) {
            authService.authenticateTheAnonymousUser(request);
            return "index";
        }
        Claims claims = jwtService.getAllClaims(token);
        authService.authenticateTheUser(request, claims);
        return "index";
    }

    @GetMapping("/create-post")
    public String getCreatePost(@ModelAttribute("request") PostDto postDto, Model model) {
        model.addAttribute("post", postDto);
        return "create-post";
    }

    @PostMapping("/addPost")
    public String addProduct(@RequestParam(value = "backgroundImage", required = false) MultipartFile bgImage,
                             @RequestParam(value = "postImages", required = false) List<MultipartFile> postImages,
                             @Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return getCreatePost(postDto, model);
        }
        PostResponseOnlyId response = postService.addPost(postDto, bgImage, postImages);
        return "redirect:/post/" + response.getId();
    }

    @GetMapping("/post/{postId}")
    public String getPost(@PathVariable Long postId, Model model) {
        PostResponse response = postService.getPostById(postId);
        model.addAttribute("post", response);
        return "post";
    }

    @ResponseBody
    @GetMapping("/image/{id}")
    public byte[] getImageById(@PathVariable("id") Long id) {
        return mediaService.getImageByIdWithData(id).getData();
    }

}

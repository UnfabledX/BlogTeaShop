package com.leka.blogteashop.controller;

import com.leka.blogteashop.dto.EditPostDto;
import com.leka.blogteashop.dto.PostDto;
import com.leka.blogteashop.dto.PostResponse;
import com.leka.blogteashop.dto.PostResponseOnlyId;
import com.leka.blogteashop.service.MediaService;
import com.leka.blogteashop.service.PostService;
import com.leka.blogteashop.service.impl.AuthService;
import com.leka.blogteashop.service.jwt.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import static com.leka.blogteashop.utils.Validator.validateImageNamingInContent;

@Log4j2
@Controller
@RequiredArgsConstructor
public class BlogController {

    public static final int DEFAULT_SIZE = 4;

    private final JwtService jwtService;
    private final AuthService authService;
    private final PostService postService;
    private final MediaService mediaService;

    @Value("${teashop.link}")
    private String toOnlineTeaShop;

    @GetMapping("/to-main-online-shop")
    public RedirectView getToOnlineTeaShop() {
        return new RedirectView(toOnlineTeaShop);
    }

    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }

    @GetMapping("/contact")
    public String getContacts() {
        return "contact";
    }

    @GetMapping("/main")
    public String getMainPage(@RequestParam(value = "token", required = false) String token,
                              @PageableDefault(size = DEFAULT_SIZE, sort = "createdAt", direction = Sort.Direction.DESC)
                              Pageable pageable, Model model, HttpServletRequest request) {
        if (token == null || jwtService.isTokenExpired(token)) {
            authService.authenticateTheAnonymousUser(request);
            return "index";
        }
        Claims claims = jwtService.getAllClaims(token);
        authService.authenticateTheUser(request, claims);
        Page<PostResponse> postResponses = postService.getPosts(pageable);
        model.addAttribute("postList", postResponses);
        return "index";
    }

    @GetMapping("/create-post")
    public String getCreatePost(@ModelAttribute("request") PostDto postDto, Model model) {
        model.addAttribute("post", postDto);
        return "create-post";
    }

    @PostMapping("/addPost")
    public String addPost(@RequestParam(value = "backgroundImage", required = false) MultipartFile bgImage,
                          @RequestParam(value = "postImages", required = false) List<MultipartFile> postImages,
                          @Valid @ModelAttribute("post") PostDto postDto,
                          BindingResult result, Model model) {
        //check that file names in the content are the same names of uploaded files.
        boolean isValid = validateImageNamingInContent(postImages, postDto.getContentUA(), postDto.getContentEN());
        if (!isValid) {
            result.addError(new ObjectError("post",
                    "Please, check image names (i.e. \"@{post_image.jpg}\") in the content sections"));
        }
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

    @GetMapping("/edit-post/{postId}")
    public String getEditPost(@PathVariable Long postId, Model model) {
        EditPostDto dto = postService.getEditPostDtoById(postId);
        model.addAttribute("post", dto);
        return "edit-post";
    }

    @PostMapping("/editPost/{postId}")
    public String editPost(@PathVariable Long postId,
                           @RequestParam(value = "backgroundImage", required = false) MultipartFile bgImage,
                           @RequestParam(value = "postImages", required = false) List<MultipartFile> postImages,
                           @Valid @ModelAttribute("post") EditPostDto postDto,
                           BindingResult result, Model model) {
        boolean isValid = validateImageNamingInContent(postImages, postDto.getContentUA(), postDto.getContentEN());
        if (!isValid) {
            result.addError(new ObjectError("post",
                    "Please, check image names (i.e. \"@{post_image.jpg}\") in the content sections"));
        }
        if (result.hasErrors()) {
            return getEditPost(postId, model);
        }
        postService.editPost(postId, postDto, bgImage, postImages);
        return "redirect:/post/" + postId;
    }

    @ResponseBody
    @GetMapping("/image/{id}")
    public byte[] getImageById(@PathVariable("id") Long id) {
        return mediaService.getImageByIdWithData(id).getData();
    }

    @GetMapping("/")
    public String getMainPageInternally(
            @PageableDefault(size = DEFAULT_SIZE, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable, Model model) {
        Page<PostResponse> postResponses = postService.getPosts(pageable);
        model.addAttribute("postList", postResponses);
        return "index";
    }

    @PostMapping("/loadMorePosts")
    public String loadMorePosts(@CookieValue(value = "counter", defaultValue = "0") Integer counter,
                                Model model, HttpServletResponse response) {
        counter++;
        int size = DEFAULT_SIZE * (counter + 1);
        PageRequest pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
        Page<PostResponse> postResponses = postService.getPosts(pageable);
        model.addAttribute("postList", postResponses);
        response.addCookie(new Cookie("counter", counter.toString()));
        return "fragments :: updated-list";
    }

}

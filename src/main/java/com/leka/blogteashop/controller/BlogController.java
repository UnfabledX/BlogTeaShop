package com.leka.blogteashop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BlogController {

    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }

    @GetMapping("/contact")
    public String getContacts() {
        return "contact";
    }

    @GetMapping("/post")
    public String getPost() {
        return "post";
    }
}

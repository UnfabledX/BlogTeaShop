package com.leka.blogteashop.controller;

import com.leka.blogteashop.service.AuthService;
import com.leka.blogteashop.service.jwt.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
@RequiredArgsConstructor
public class BlogController {

    private final JwtService jwtService;
    private final AuthService authService;

    @GetMapping("/about")
    public String getAbout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getPrincipal().toString() + " " + authentication.getAuthorities());
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

}

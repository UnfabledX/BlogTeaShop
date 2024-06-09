package com.leka.blogteashop.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public void authenticateTheUser(HttpServletRequest request, Claims claims) {
        String principal = claims.get("username", String.class);
        String authority = claims.get("roles", String.class);
        UsernamePasswordAuthenticationToken authUserToken =
                new UsernamePasswordAuthenticationToken(
                        principal, null,
                        AuthorityUtils.createAuthorityList(authority)
                );
        applyAuthTokenToSecurityApplicationContext(request, authUserToken);
    }

    public void authenticateTheAnonymousUser(HttpServletRequest request) {
        AnonymousAuthenticationToken anonymousToken = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        applyAuthTokenToSecurityApplicationContext(request, anonymousToken);
    }

    private static void applyAuthTokenToSecurityApplicationContext(
            HttpServletRequest request, AbstractAuthenticationToken token) {
        token.setDetails(new WebAuthenticationDetails(request));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
    }
}

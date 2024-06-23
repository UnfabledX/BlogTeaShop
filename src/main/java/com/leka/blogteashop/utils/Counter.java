package com.leka.blogteashop.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = TARGET_CLASS)
public class Counter {

    private int count = 0;

    public void increment() {
        count++;
    }

}

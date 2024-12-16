package com.leka.blogteashop.exception.handler;

import com.leka.blogteashop.exception.CounterLimitException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CounterLimitException.class)
    public String handleUserAlreadyExistsException(CounterLimitException ex) {
        return "redirect:/contact?counterLimitReached";
    }
}
package com.leka.blogteashop.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CounterLimitException extends RuntimeException {

    public CounterLimitException(String message) {
        super(message);
    }
}

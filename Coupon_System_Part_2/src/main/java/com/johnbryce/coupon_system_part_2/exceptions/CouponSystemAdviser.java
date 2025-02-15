package com.johnbryce.coupon_system_part_2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CouponSystemAdviser {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = CouponSystemException.class)
    public Error handleCouponException(CouponSystemException exception) {
        return new Error(exception.getErrorMessage().getCode(),
                exception.getErrorMessage().getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    public Error handleAnyException() {
        return new Error(400, "Something unexpected " +
                "happens,Please try again later");
    }

}

package com.johnbryce.coupon_system_part_2.exceptions;

import lombok.Getter;

@Getter
public class CouponSystemException extends Exception {
    private final ErrorMessage errorMessage;

    public CouponSystemException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}

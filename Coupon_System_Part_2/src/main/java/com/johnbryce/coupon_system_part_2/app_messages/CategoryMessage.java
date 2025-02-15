package com.johnbryce.coupon_system_part_2.app_messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryMessage {

    CATEGORY_EXISTS_BY_ID(2000,
            "Category exists by id"),
    CATEGORY_NOT_EXISTS_BY_ID(2001,
            "Category not exists by id"),
    CATEGORY_DELETED(2002, "Category deleted"),
    CATEGORY_UPDATED(2003, "Category updated"),
    CATEGORY_EXISTS_BY_NAME(2004, "Category exists by name"),
    CATEGORY_NOT_EXISTS_BY_NAME(2005, "Category not exists by name"),
    WRONG_CATEGORY(2006,
            "Cannot get customers with coupons filtered by wrong category");


    private final int code;
    private final String message;


}

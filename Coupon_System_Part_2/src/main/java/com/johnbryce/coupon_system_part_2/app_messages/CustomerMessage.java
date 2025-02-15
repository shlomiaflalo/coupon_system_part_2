package com.johnbryce.coupon_system_part_2.app_messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerMessage {

    CUSTOMER_GOT_UPDATED(2046, "Customer got updated"),
    CUSTOMER_GOT_DELETED(2047, "Customer got deleted"),
    CUSTOMER_EXISTS_BY_EMAIL_AND_PASSWORD(2048, "Customer exists by email and password"),
    CUSTOMER_NOT_EXISTS_BY_EMAIL_AND_PASSWORD(2049, "Customer not exists by email and password"),
    CUSTOMER_EXISTS_BY_ID(2050, "Customer exists by id"),
    CUSTOMER_NOT_EXISTS_BY_ID(2051, "Customer not exists by id"),
    CUSTOMER_NOT_EXISTS_BY_EMAIL(2052, "Customer not exists by email"),
    CUSTOMER_EXISTS_BY_EMAIL(2053, "Customer exists by email"),
    CUSTOMER_EMAIL_EXISTS_BY_ID(2054, "Customer email exists by id"),
    CUSTOMER_EMAIL_NOT_EXISTS_BY_ID(2055, "Customer email not exists by id"),
    CUSTOMER_EMAIL_EXISTS_EXCLUDE(2056, "Customer email exists exclude this customer"),
    CUSTOMER_EMAIL_NOT_EXISTS_EXCLUDE(2057, "Customer email not exists exclude this customer");

    private final int code;
    private final String message;


}

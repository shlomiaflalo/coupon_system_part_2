package com.johnbryce.coupon_system_part_2.exceptions.purchases;

import com.johnbryce.coupon_system_part_2.exceptions.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseException implements ErrorMessage {

    PURCHASE_NOT_EXISTS(1033, "Purchase not exists"),
    PURCHASE_EXISTS(1034, "Purchase already exists"),
    PURCHASE_NOT_EXISTS_BY_CUSTOMER_ID_AND_COUPON_ID(1035,
            "Purchase not exists by customer id and coupon id");
    private int code;
    private String message;
}

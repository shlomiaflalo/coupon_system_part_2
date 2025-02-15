package com.johnbryce.coupon_system_part_2.app_messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseMessage {

    SUCCESSFULLY_PURCHASE_COUPON(2036,
            "Successfully purchase coupon"),
    PURCHASE_GOT_UPDATED(2037, "Purchase got updated"),
    PURCHASE_GOT_DELETED(2038, "Purchase got deleted"),
    PURCHASES_GOT_DELETED_BY_COUPON_ID(2039, "Purchases got deleted by coupon id"),
    PURCHASES_BY_COMPANY_GOT_REMOVED(2040, "Purchases got removed by company id"),
    PURCHASES_BY_CUSTOMER_GOT_REMOVED(2041, "Purchases got removed by customer id"),
    PURCHASE_COUPON_BY_CUSTOMER(2042, "Coupon has been purchased by customer"),
    HAS_NOT_PURCHASE_COUPON_BY_CUSTOMER(2043, "Coupon hasn't purchased by customer"),
    PURCHASE_EXISTS_BY_ID(2044, "Purchase exists by id"),
    PURCHASE_NOT_EXISTS_BY_ID(2045, "Purchase not exists by id"),
    ;

    private final int code;
    private final String message;


}

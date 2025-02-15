package com.johnbryce.coupon_system_part_2.app_messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponMessage {

    ALL_COUPONS_DELETED_BY_COMPANY_ID(2016,
            "All coupons got deleted by the company id"),
    REMOVING_EXPIRED_COUPONS(2017,
            "Removing all expired coupons - daily job: started"),
    COUPON_AMOUNT_AVAILABLE(2018,
            "The amount of coupons is available"),
    COUPON_AMOUNT_NOT_AVAILABLE(2019,
            "The amount of coupons is not available"),
    COUPON_DUE_DATE_IS_GOOD(2020,
            "Coupon due date is good"),
    COUPON_DUE_DATE_IS_BEHIND(2021,
            "Coupon Due Date is behind us"),
    REMOVED_COUPON_BY_CUSTOMER_ID(2022,
            "Removed coupons by customer id if exists"),
    COUPON_TITLE_EXISTS_BY_COMPANY_ID(2023,
            "Coupon title exists by company id"),
    COUPON_TITLE_NOT_EXISTS_BY_COMPANY_ID(2024,
            "Coupon title not exists by company id"),
    COUPON_EXISTS_BY_ID_AND_COMPANY_ID(2025,
            "Coupon exists by id and company id"),
    COUPON_NOT_EXISTS_BY_ID_AND_COMPANY_ID(2026,
            "Coupon not exists by id and company id"),
    COUPON_TITLE_EXISTS_BY_COMPANY_ID_EXCLUDE(2027,
            "Coupon title exists by company id (exclude this company) "),
    COUPON_TITLE_NOT_EXISTS_BY_COMPANY_ID_EXCLUDE(2028,
            "Coupon title not exists by company id (exclude this company)"),
    COUPON_GOT_UPDATED(2029,
            "Coupon got updated"),
    COUPON_GOT_DELETED(2030,
            "Coupon got deleted"),
    COUPON_IS_ADDED_FOR_COMPANY(2031,
            "Coupon is added for company"),
    COUPON_TYPE_PURCHASED(2032,
            "Coupon type is purchased before - by customer"),
    COUPON_TYPE_NOT_PURCHASED(2033,
            "Coupon type is not purchased before - by customer"),
    COUPON_EXISTS_BY_ID(2034,
            "Coupon exists by id"),
    COUPON_NOT_EXISTS_BY_ID(2035,
            "Coupon not exists by id"),
    ;

    private final int code;
    private final String message;


}

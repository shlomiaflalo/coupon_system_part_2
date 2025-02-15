package com.johnbryce.coupon_system_part_2.dto;

import lombok.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDto {

    private int id;
    private CustomerDto customer;
    private CouponDto coupon;
    private int review;


}

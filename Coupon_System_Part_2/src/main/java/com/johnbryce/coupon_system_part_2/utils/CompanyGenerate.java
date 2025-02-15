package com.johnbryce.coupon_system_part_2.utils;

import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyGenerate {

    String name;
    String email;
}

package com.johnbryce.coupon_system_part_2.dto;

import lombok.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private int id;
    private String name;

}
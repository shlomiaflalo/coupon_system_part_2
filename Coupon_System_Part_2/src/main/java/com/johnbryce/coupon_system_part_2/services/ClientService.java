package com.johnbryce.coupon_system_part_2.services;

import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;

public interface ClientService {
    boolean login(String email, String password) throws CouponSystemException;
}
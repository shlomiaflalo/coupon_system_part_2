package com.johnbryce.coupon_system_part_2.services;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;

import java.util.List;

public interface TableService<T, ID>{

    T add(T t) throws Exception;
    T getSingle(ID id) throws CouponSystemException;
    Message update(T updateRecord, ID id) throws CouponSystemException;
    Message delete(ID id) throws CouponSystemException;
    List<T> getAll() throws CouponSystemException;
    boolean isExists(ID id);
}

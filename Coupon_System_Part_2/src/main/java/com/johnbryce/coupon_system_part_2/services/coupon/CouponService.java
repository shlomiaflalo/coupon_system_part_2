package com.johnbryce.coupon_system_part_2.services.coupon;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CouponDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.TableService;

import java.util.List;

public interface CouponService extends TableService<CouponDto, Integer> {
    List<CouponDto> getCompanyCoupons(int companyId) throws CouponSystemException;
    List<CouponDto> getCompanyCoupons(int companyId, double maxPrice) throws CouponSystemException;
    List<CouponDto> getCompanyCouponsByCategoryId(int companyId, int categoryId) throws CouponSystemException;
    List<CouponDto> getCouponsByCategoryIdAndCompanyId(int categoryId, int companyId) throws CouponSystemException;
    List<CouponDto> getCustomerCoupons(int customerId) throws CouponSystemException;
    List<CouponDto> getCustomerCouponsByCategoryId(int customerId, int categoryId) throws CouponSystemException;
    List<CouponDto> getCustomerCouponsByMaxPrice(int customerId,double maxPrice) throws CouponSystemException;
    CouponDto getLastRecordCoupon() throws CouponSystemException;
    CouponDto getFirstRecordCoupon() throws CouponSystemException;
    Message removeAllExpiredCoupons() throws CouponSystemException;
    Message isCouponAvailable(int couponId) throws CouponSystemException;
    Message dueDateCouponCheck(int couponId) throws CouponSystemException;
    Message isCouponTitleExistsByCompanyId(int companyId, String title) throws CouponSystemException;
    Message isCouponExistsByCouponIdAndCompanyId(int couponId, int companyId) throws CouponSystemException;
    Message isCouponTitleExistsByCompanyIdExclude(int companyId, String title,int CouponId) throws CouponSystemException;
    Message addCouponByCompany(int companyId, CouponDto coupon) throws CouponSystemException;
    Message updateCouponByCompany(int companyId, CouponDto coupon) throws CouponSystemException;
    Message deleteCouponByCompany(int companyId, int couponId) throws CouponSystemException;
    Message isCouponTypePurchased(int customerId, int couponId) throws CouponSystemException;
    Message isCouponExistsByIdForController(int id) throws CouponSystemException;
    void updateCouponAmountAndPurchased(int couponId) throws CouponSystemException;
}


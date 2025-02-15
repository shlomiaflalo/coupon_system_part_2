package com.johnbryce.coupon_system_part_2.services.purchase;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.PurchaseDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.TableService;

import java.sql.SQLException;
import java.util.List;

public interface PurchaseService extends TableService<PurchaseDto,Integer> {
    List<PurchaseDto> getCustomerPurchases(int customerId) throws CouponSystemException;
    Message purchaseCoupon(int customerId, int couponId) throws CouponSystemException, Exception;
    Message isCouponPurchasedByCustomer(int customerId, int couponId) throws CouponSystemException;
    Message deleteCouponPurchase(int customerId, int couponId) throws CouponSystemException;
    Message deleteCouponPurchasesByCouponId(int couponId) throws CouponSystemException, SQLException;
    Message deleteCouponPurchaseByCompanyId(int companyId) throws CouponSystemException, SQLException;
    Message isPurchaseExistsByIdForController(int id) throws CouponSystemException;
    Message deleteCouponPurchaseByCustomerId(int customerId) throws CouponSystemException;
    void updateOrAddPurchasesViaCustomer(PurchaseDto purchaseDto) throws Exception;
    void checkCouponAndPurchaseOrUpdate(int customerId, int couponId, boolean addOrUpdate,
                                boolean autoReview, Integer... personalReview) throws CouponSystemException, Exception;
}

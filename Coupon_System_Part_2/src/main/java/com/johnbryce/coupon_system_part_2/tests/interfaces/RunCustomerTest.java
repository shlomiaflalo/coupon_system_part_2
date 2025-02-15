package com.johnbryce.coupon_system_part_2.tests.interfaces;


public interface RunCustomerTest {

    void runAllCustomerTests() throws InterruptedException;

    void loginTest();

    void purchaseTest();

    void wrongPurchasedByWrongCustomerIdTest();

    void wrongPurchasedByWrongCouponIdTest();

    void wrongPurchasedThisCouponTest();

    void getCustomerCouponsTest();

    void getWrongCustomerCouponsTest();

    void getCustomerCouponsByCategoryTest();

    void getWrongCustomerCouponsByCategoryTest();

    void getCustomerCouponsByMaxPriceTest();

    void getWrongCustomerCouponsByMaxPriceTest();

    void getCustomerDetailsTest();

    void getWrongCustomerDetailsTest();

    void getSingleByEmailTest();

    void getSingleByEmailWrongTest();

}

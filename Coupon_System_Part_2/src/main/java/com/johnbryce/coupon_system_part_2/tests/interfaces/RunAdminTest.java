package com.johnbryce.coupon_system_part_2.tests.interfaces;

import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;

public interface RunAdminTest {

    void runAllAdminTests() throws InterruptedException, CouponSystemException;

    void loginTest() throws InterruptedException;

    void addCompanyTest();

    void addWrongCompanyTest() throws Exception;

    void updateCompanyTest();

    void updateWrongCompanyTest();

    void deleteCompanyTest();

    void deleteWrongCompanyTest();

    void getAllCompaniesTest();

    void getOneCompanyTest();

    void getOneWrongCompanyTest();

    void addCustomerTest();

    void addWrongCustomerTest();

    void updateCustomerTest();

    void updateWrongCustomerTest();

    void deleteCustomerTest();

    void deleteWrongCustomerTest();

    void getAllCustomersTest();

    void getOneCustomerTest();

    void getOneWrongCustomerTest();

    void getSingleCompanyByEmailTest();

    void getWrongSingleCompanyByEmailTest();

    void getSingleCustomerByEmail() throws Exception;

    void getWrongSingleCustomerByEmail() throws Exception;
}

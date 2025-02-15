package com.johnbryce.coupon_system_part_2.services.customer;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CustomerDto;
import com.johnbryce.coupon_system_part_2.entities.Customer;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.ClientService;
import com.johnbryce.coupon_system_part_2.services.TableService;

import java.sql.SQLException;
import java.util.List;

public interface CustomerService extends TableService<CustomerDto,Integer>, ClientService {

    List<CustomerDto> getAllCustomersByCouponCategory(int categoryId) throws CouponSystemException;
    CustomerDto getSingleByEmail(String email) throws CouponSystemException;
    CustomerDto getFirstCustomerRecord() throws SQLException, CouponSystemException;
    CustomerDto getLastCustomerRecord() throws SQLException, CouponSystemException;
    Message isCustomerEmailExistsExclude(String email, int customerId) throws CouponSystemException;
    Message isCustomerEmailExistsById(String email, int customerId) throws CouponSystemException;
    Message isCustomerEmailExists(String email) throws CouponSystemException;
    Message isCustomerExists(String email, String password) throws CouponSystemException;
    Message isCustomerExistsByIdForController(int id) throws CouponSystemException;
    void removePurchasesByCouponId(int couponId) throws CouponSystemException, SQLException;
    void removePurchasesByCompanyId(int companyId) throws CouponSystemException, SQLException;
    void cleanPurchaseListByCustomerId(int customerId) throws CouponSystemException;
    void insertOrUpdateCustomerAfterLogic(CustomerDto customer, Integer id) throws CouponSystemException;

}

package com.johnbryce.coupon_system_part_2.services.Admin;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CompanyDto;
import com.johnbryce.coupon_system_part_2.dto.CustomerDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.ClientService;

import java.sql.SQLException;
import java.util.List;

public interface AdminService extends ClientService {

    Message updateCompany(CompanyDto company, int id) throws Exception;
    Message deleteCompany(int companyId) throws Exception;
    Message updateCustomer(CustomerDto customer, int id) throws Exception;
    Message deleteCustomer(int customerId) throws Exception;
    List<CustomerDto> getAllCustomers() throws CouponSystemException;
    List<CompanyDto> getAllCompanies() throws CouponSystemException;
    CompanyDto getSingleCompanyByEmail(String email) throws CouponSystemException;
    CompanyDto getFirstCompanyRecord() throws CouponSystemException;
    CompanyDto getLastCompanyRecord() throws CouponSystemException;
    CompanyDto addCompany(CompanyDto company) throws Exception;
    CompanyDto getOneCompany(int companyId) throws CouponSystemException;
    CustomerDto getSingleCustomerByEmail(String email) throws CouponSystemException;
    CustomerDto getLastCustomerRecord() throws CouponSystemException, SQLException;
    CustomerDto getFirstCustomerRecord() throws CouponSystemException, SQLException;
    CustomerDto addCustomer(CustomerDto customer) throws Exception;
    CustomerDto getOneCustomer(int customerId) throws CouponSystemException;


}

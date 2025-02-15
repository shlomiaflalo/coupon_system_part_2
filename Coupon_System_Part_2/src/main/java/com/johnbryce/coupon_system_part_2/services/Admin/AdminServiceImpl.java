package com.johnbryce.coupon_system_part_2.services.Admin;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CompanyDto;
import com.johnbryce.coupon_system_part_2.dto.CustomerDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.exceptions.generic.GenericException;
import com.johnbryce.coupon_system_part_2.services.company.CompanyService;
import com.johnbryce.coupon_system_part_2.services.customer.CustomerService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final String email = "admin@admin.com";
    private final String password = "admin";

    private final CustomerService customerService;
    private final CompanyService companyService;

    public AdminServiceImpl(@Lazy CustomerService customerService,
                            @Lazy CompanyService companyService) {
        this.customerService = customerService;
        this.companyService = companyService;

    }

    @Override
    public boolean login(String email, String password) throws CouponSystemException {
        if (!(email.equals(this.email) && password.equals(this.password))) {
            throw new CouponSystemException(GenericException.EMAIL_AND_PASSWORD_IS_NOT_CORRECT);
        }
        return true;
    }

    @Transactional
    @Override
    public CompanyDto addCompany(CompanyDto company) throws Exception {
        return companyService.add(company);
    }

    @Transactional
    @Override
    public Message updateCompany(CompanyDto company, int id) throws Exception {
        return companyService.update(company, id);
    }

    @Transactional
    @Override
    public Message deleteCompany(int companyId) throws Exception {
        return companyService.delete(companyId);
    }

    @Override
    public List<CompanyDto> getAllCompanies() throws CouponSystemException {
        return companyService.getAll();
    }

    @Override
    public CompanyDto getOneCompany(int companyId) throws CouponSystemException {
        return companyService.getSingle(companyId);
    }

    @Transactional
    @Override
    public CustomerDto addCustomer(CustomerDto customer) throws Exception {
        return customerService.add(customer);
    }

    @Transactional
    @Override
    public Message updateCustomer(CustomerDto customer, int id) throws Exception {
        return customerService.update(customer, id);
    }

    @Transactional
    @Override
    public Message deleteCustomer(int customerId) throws Exception {
        //purchaseService.deleteCouponPurchaseByCustomerId(customerId);
        return customerService.delete(customerId);
    }

    @Override
    public List<CustomerDto> getAllCustomers() throws CouponSystemException {
        return customerService.getAll();
    }

    @Override
    public CustomerDto getOneCustomer(int customerId) throws CouponSystemException {
        return customerService.getSingle(customerId);
    }

    @Override
    public CompanyDto getSingleCompanyByEmail(String email) throws CouponSystemException {
        return companyService.getSingleByEmail(email);
    }

    @Override
    public CustomerDto getSingleCustomerByEmail(String email) throws CouponSystemException {
        return customerService.getSingleByEmail(email);
    }

    @Override
    public CustomerDto getFirstCustomerRecord() throws CouponSystemException, SQLException {
        return customerService.getFirstCustomerRecord();
    }

    @Override
    public CustomerDto getLastCustomerRecord() throws CouponSystemException, SQLException {
        return customerService.getLastCustomerRecord();
    }

    @Override
    public CompanyDto getFirstCompanyRecord() throws CouponSystemException {
        return companyService.getFirstCompanyRecord();
    }

    @Override
    public CompanyDto getLastCompanyRecord() throws CouponSystemException {
        return companyService.getLastCompanyRecord();
    }


}

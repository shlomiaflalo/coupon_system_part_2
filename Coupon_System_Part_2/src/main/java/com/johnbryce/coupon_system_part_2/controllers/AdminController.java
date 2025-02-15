package com.johnbryce.coupon_system_part_2.controllers;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CompanyDto;
import com.johnbryce.coupon_system_part_2.dto.CustomerDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.Admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/company")
    public CompanyDto addCompany(@RequestBody CompanyDto companyDto)
            throws Exception {
        return adminService.addCompany(companyDto);
    }

    @PutMapping("/company/{companyId}")
    public Message updateCompany(@RequestBody CompanyDto company, @PathVariable("companyId")
    int companyId) throws Exception {
        return adminService.updateCompany(company, companyId);
    }

    @DeleteMapping("/company/{companyId}")
    public Message deleteCompany(@PathVariable("companyId") int companyId)
            throws Exception {
        return adminService.deleteCompany(companyId);
    }

    @GetMapping("/getAllCompanies")
    public List<CompanyDto> getAllCompanies() throws CouponSystemException {
        return adminService.getAllCompanies();
    }


    @GetMapping("/getOneCompany/{companyId}")
    public CompanyDto getOneCompany(@PathVariable("companyId") int companyId) throws Exception {
        return adminService.getOneCompany(companyId);
    }

    @PostMapping("/customer")
    public CustomerDto addCustomer(@RequestBody CustomerDto customer) throws Exception {
        return adminService.addCustomer(customer);
    }

    @PutMapping("/customer/{customerId}")
    public Message updateCustomer(@RequestBody CustomerDto customer, @PathVariable("customerId") int customerId) throws Exception {
        return adminService.updateCustomer(customer, customerId);
    }

    @DeleteMapping("/customer/{customerId}")
    public Message deleteCustomer(@PathVariable("customerId") int customerId) throws Exception {
        return adminService.deleteCustomer(customerId);
    }

    @GetMapping("/getAllCustomers")
    public List<CustomerDto> getAllCustomers() throws CouponSystemException {
        return adminService.getAllCustomers();
    }

    @GetMapping("/getOneCustomer/{customerId}")
    public CustomerDto getOneCustomer(@PathVariable("customerId") int customerId) throws Exception {
        return adminService.getOneCustomer(customerId);
    }

    @GetMapping("/getSingleCompanyByEmail")
    public CompanyDto getSingleCompanyByEmail(@RequestParam("companyEmail") String companyEmail) throws CouponSystemException {
        return adminService.getSingleCompanyByEmail(companyEmail);
    }

    @GetMapping("/getSingleCustomerByEmail")
    public CustomerDto getSingleCustomerByEmail(@RequestParam("customerEmail") String customerEmail) throws CouponSystemException {
        return adminService.getSingleCustomerByEmail(customerEmail);
    }

    @GetMapping("/getLastCustomerRecord")
    public CustomerDto getLastCustomerRecord() throws SQLException, CouponSystemException {
        return adminService.getLastCustomerRecord();
    }

    @GetMapping("/getFirstCustomerRecord")
    public CustomerDto getFirstCustomerRecord() throws SQLException, CouponSystemException {
        return adminService.getFirstCustomerRecord();
    }

    @GetMapping("/getFirstCompanyRecord")
    public CompanyDto getFirstCompanyRecord() throws CouponSystemException {
        return adminService.getFirstCompanyRecord();
    }

    @GetMapping("/getLastCompanyRecord")
    public CompanyDto getLastCompanyRecord() throws CouponSystemException {
        return adminService.getLastCompanyRecord();
    }
}

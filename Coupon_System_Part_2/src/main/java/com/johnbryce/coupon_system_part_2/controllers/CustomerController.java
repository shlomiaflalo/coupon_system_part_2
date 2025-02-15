package com.johnbryce.coupon_system_part_2.controllers;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CustomerDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping("/getSingleByEmail")
    public CustomerDto getSingleByEmail(@RequestParam("email") String email) throws CouponSystemException {
        return customerService.getSingleByEmail(email);
    }

    @GetMapping("/getFirstCustomerRecord")
    public CustomerDto getFirstCustomerRecord() throws CouponSystemException, SQLException {
        return customerService.getFirstCustomerRecord();
    }

    @GetMapping("/getLastCustomerRecord")
    public CustomerDto getLastCustomerRecord() throws CouponSystemException, SQLException {
        return customerService.getLastCustomerRecord();
    }


    @GetMapping("/getAllCustomersByCouponCategory/{categoryId}")
    public List<CustomerDto> getAllCustomersByCouponCategory(@PathVariable("categoryId") int categoryId) throws CouponSystemException {
        return customerService.getAllCustomersByCouponCategory(categoryId);
    }

    @GetMapping("/{customerId}/isCustomerEmailExistsExclude")
    public Message isCustomerEmailExistsExclude(@RequestParam("email") String email, @PathVariable("customerId") int customerId) throws CouponSystemException {
        return customerService.isCustomerEmailExistsExclude(email, customerId);
    }

    @GetMapping("/{customerId}/isCustomerEmailExistsById")
    public Message isCustomerEmailExistsById(@RequestParam("email") String email, @PathVariable("customerId") int customerId) throws CouponSystemException {
        return customerService.isCustomerEmailExistsById(email, customerId);
    }

    @GetMapping("/isCustomerEmailExists")
    public Message isCustomerEmailExists(@RequestParam("email") String email) throws CouponSystemException {
        return customerService.isCustomerEmailExists(email);
    }

    @GetMapping("/existsByEmailAndPassword")
    public Message isCustomerExists(@RequestParam("email") String email, @RequestParam("password") String password) throws CouponSystemException {
        return customerService.isCustomerExists(email, password);
    }

    @GetMapping("/isCustomerExistsByIdForController/{id}")
    public Message isCustomerExistsByIdForController(@PathVariable("id") int id) throws CouponSystemException {
        return customerService.isCustomerExistsByIdForController(id);
    }


    @PostMapping
    public CustomerDto add(@RequestBody CustomerDto customerDto) throws Exception {
        return customerService.add(customerDto);
    }


    @PutMapping("/{id}")
    public Message update(@RequestBody CustomerDto customerDto, @PathVariable("id") int id) throws CouponSystemException {
        return customerService.update(customerDto, id);
    }


    @DeleteMapping("/{id}")
    public Message delete(@PathVariable("id") int id) throws CouponSystemException {
        return customerService.delete(id);
    }


    @GetMapping("all")
    public List<CustomerDto> getAll() throws CouponSystemException {
        return customerService.getAll();
    }

    @GetMapping("/getSingle/{id}")
    public CustomerDto getSingle(@PathVariable("id") int id) throws CouponSystemException {
        return customerService.getSingle(id);
    }

}

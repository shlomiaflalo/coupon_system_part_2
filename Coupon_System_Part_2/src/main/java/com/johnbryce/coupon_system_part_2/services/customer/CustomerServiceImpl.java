package com.johnbryce.coupon_system_part_2.services.customer;

import com.johnbryce.coupon_system_part_2.app_messages.CustomerMessage;
import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CustomerDto;
import com.johnbryce.coupon_system_part_2.dto.PurchaseDto;
import com.johnbryce.coupon_system_part_2.entities.Customer;
import com.johnbryce.coupon_system_part_2.entities.Purchase;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.exceptions.categories.CategoryException;
import com.johnbryce.coupon_system_part_2.exceptions.companies.CompanyException;
import com.johnbryce.coupon_system_part_2.exceptions.coupons.CouponException;
import com.johnbryce.coupon_system_part_2.exceptions.customers.CustomerException;
import com.johnbryce.coupon_system_part_2.exceptions.generic.GenericException;
import com.johnbryce.coupon_system_part_2.repositories.CustomerRepository;
import com.johnbryce.coupon_system_part_2.services.category.CategoryService;
import com.johnbryce.coupon_system_part_2.services.company.CompanyService;
import com.johnbryce.coupon_system_part_2.services.coupon.CouponService;
import com.johnbryce.coupon_system_part_2.services.purchase.PurchaseService;
import com.johnbryce.coupon_system_part_2.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PurchaseService purchaseService;
    private final Utils utils;
    private final CategoryService categoryService;
    private final CouponService couponService;
    private final CompanyService companyService;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            @Lazy PurchaseService purchaseService,
            Utils utils,
            CategoryService categoryService,
            @Lazy CouponService couponService,
            CompanyService companyService) {
        this.customerRepository = customerRepository;
        this.purchaseService = purchaseService;
        this.utils = utils;
        this.categoryService = categoryService;
        this.couponService = couponService;
        this.companyService = companyService;
    }

    @Transactional
    @Override
    public void cleanPurchaseListByCustomerId(int customerId) throws CouponSystemException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new CouponSystemException(
                        CustomerException.CUSTOMER_IS_NOT_EXISTS));
        if (customer.getPurchases() != null && !customer.getPurchases().isEmpty()) {
            customer.getPurchases().clear();
        }
        customerRepository.save(customer);
    }

    @Transactional
    @Override
    public CustomerDto add(CustomerDto customer) throws CouponSystemException {
        if (customerRepository.existsById(customer.getId())) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_ALREADY_EXISTS);
        }

        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new CouponSystemException(CustomerException.EMAIL_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        if (customerRepository.existsByFirstNameAndLastName(customer.getFirstName(), customer.getLastName())) {
            throw new CouponSystemException(CustomerException.FIRST_AND_LAST_NAME_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        Customer customerConvert = utils.convertFromDto(customer, Customer.class);
        Customer customerInDb = customerRepository.save(customerConvert);
        return utils.convertToDto(customerInDb, CustomerDto.class);
    }

    @Transactional
    @Override
    public void insertOrUpdateCustomerAfterLogic(CustomerDto customer, Integer id) throws CouponSystemException {
        if (customer.getId() != id) {
            customer.setId(id);
        }
        customerRepository.save(utils.convertFromDto(customer, Customer.class));
    }

    @Transactional
    @Override
    public Message update(CustomerDto customerDto, Integer id) throws CouponSystemException {
        Customer existingCustomer =
                customerRepository.findById(id).orElseThrow(() ->
                        new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS));

        // Check for email conflict
        if (customerRepository.existsByEmailAndIdNot(customerDto.getEmail(), id)) {
            throw new CouponSystemException(CustomerException.EMAIL_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        // Check for name conflict
        if (customerRepository.existsByFirstNameAndLastNameAndIdNot
                (customerDto.getFirstName(), customerDto.getLastName(), id)) {
            throw new CouponSystemException(CustomerException.
                    FIRST_AND_LAST_NAME_IN_USE_BY_ANOTHER_CUSTOMER);
        }


        existingCustomer.setFirstName(customerDto.getFirstName());
        existingCustomer.setLastName(customerDto.getLastName());
        existingCustomer.setEmail(customerDto.getEmail());
        existingCustomer.setPassword(customerDto.getPassword());

        /* Check if the customer ID is different
        and delete all purchases related to the old customer ID*/
        if (!Objects.equals(existingCustomer.getId(), customerDto.getId())) {
            existingCustomer.getPurchases().clear();
        } else {
            if (customerDto.getPurchases() != null &&
                    !customerDto.getPurchases().isEmpty()) {
                // Add new purchases to the existing ones
                existingCustomer.getPurchases().addAll(
                        utils.convertFromDtoToEntityList(
                                customerDto.getPurchases(), Purchase.class));
            }
        }

        Customer customerInDb = customerRepository.save(existingCustomer);

        return Message.builder().code(CustomerMessage
                        .CUSTOMER_GOT_UPDATED
                        .getCode()).
                message(CustomerMessage.
                        CUSTOMER_GOT_UPDATED
                        .getMessage()).
                data(utils.convertToDto(customerInDb, CustomerDto.class))
                .build();
    }


    @Transactional
    @Override
    public Message delete(Integer integer) throws CouponSystemException {
        Customer customer = customerRepository.findById(integer)
                .orElseThrow(() -> new CouponSystemException(
                        CustomerException.CUSTOMER_IS_NOT_EXISTS));

        cleanPurchaseListByCustomerId(integer);
        customerRepository.deleteById(integer);

        return Message.builder().code(CustomerMessage
                        .CUSTOMER_GOT_DELETED
                        .getCode()).
                message(CustomerMessage.
                        CUSTOMER_GOT_DELETED
                        .getMessage()).
                data(utils.convertToDto(customer, CustomerDto.class))
                .build();

    }

    @Transactional
    @Override
    public List<CustomerDto> getAll() throws CouponSystemException {
        return utils.convertFromEntityListToDto(customerRepository.findAll(), CustomerDto.class);
    }

    @Transactional
    @Override
    public CustomerDto getSingle(Integer integer) throws CouponSystemException {
        Customer customer = customerRepository.findById(integer).orElseThrow(() ->
                new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS));
        return utils.convertToDto(customer, CustomerDto.class);
    }

    @Transactional
    @Override
    public boolean isExists(Integer id) {
        return customerRepository.existsById(id);
    }

    @Transactional
    @Override
    public CustomerDto getFirstCustomerRecord() throws CouponSystemException {
        return utils.convertToDto(customerRepository.findFirstByOrderByIdAsc(), CustomerDto.class);
    }

    @Transactional
    @Override
    public CustomerDto getLastCustomerRecord() throws CouponSystemException {
        return utils.convertToDto(customerRepository.findFirstByOrderByIdDesc(), CustomerDto.class);
    }

    @Transactional
    @Override
    public List<CustomerDto> getAllCustomersByCouponCategory(int categoryId) throws CouponSystemException {
        if (!categoryService.isExists(categoryId)) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS);
        }

        List<CustomerDto> filteredCustomers =
                purchaseService.getAll().stream().filter((coupon) ->
                        coupon.getCoupon().getCategory().getId() == categoryId).map(
                        PurchaseDto::getCustomer).toList();

        if (filteredCustomers.isEmpty()) {
            throw new CouponSystemException(GenericException.EMPTY_LIST);
        }

        return filteredCustomers;
    }

    @Transactional
    @Override
    public Message isCustomerEmailExistsExclude(String email, int customerId) {
        if (customerRepository.existsByEmailAndIdNot(email, customerId)) {
            return Message.builder().code(CustomerMessage
                            .CUSTOMER_EMAIL_EXISTS_EXCLUDE
                            .getCode()).
                    message(CustomerMessage.
                            CUSTOMER_EMAIL_EXISTS_EXCLUDE
                            .getMessage()).
                    data(true)
                    .build();
        } else {
            return Message.builder().code(CustomerMessage
                            .CUSTOMER_EMAIL_NOT_EXISTS_EXCLUDE
                            .getCode()).
                    message(CustomerMessage.
                            CUSTOMER_EMAIL_NOT_EXISTS_EXCLUDE
                            .getMessage()).
                    data(false)
                    .build();
        }
    }

    @Transactional
    @Override
    public Message isCustomerEmailExistsById(String email, int customerId) {
        if (customerRepository.existsByEmailAndId(email, customerId)) {
            return Message.builder().code(CustomerMessage
                            .CUSTOMER_EMAIL_EXISTS_BY_ID
                            .getCode()).
                    message(CustomerMessage.
                            CUSTOMER_EMAIL_EXISTS_BY_ID
                            .getMessage()).
                    data(true)
                    .build();
        } else {
            return Message.builder().code(CustomerMessage
                            .CUSTOMER_EMAIL_NOT_EXISTS_BY_ID
                            .getCode()).
                    message(CustomerMessage.
                            CUSTOMER_EMAIL_NOT_EXISTS_BY_ID
                            .getMessage()).
                    data(false)
                    .build();
        }
    }

    @Transactional
    @Override
    public Message isCustomerEmailExists(String email) {
        if (customerRepository.existsByEmail(email)) {
            return Message.builder().code(CustomerMessage
                            .CUSTOMER_EXISTS_BY_EMAIL
                            .getCode()).
                    message(CustomerMessage.
                            CUSTOMER_EXISTS_BY_EMAIL
                            .getMessage()).
                    data(true)
                    .build();
        } else {
            return Message.builder().code(CustomerMessage
                            .CUSTOMER_NOT_EXISTS_BY_EMAIL
                            .getCode()).
                    message(CustomerMessage.
                            CUSTOMER_NOT_EXISTS_BY_EMAIL
                            .getMessage()).
                    data(false)
                    .build();
        }
    }

    @Transactional
    @Override
    public Message isCustomerExists(String email, String password) {
        if (customerRepository.existsByEmailAndPassword(email, password)) {
            return Message.builder().code(CustomerMessage
                            .CUSTOMER_EXISTS_BY_EMAIL_AND_PASSWORD
                            .getCode()).
                    message(CustomerMessage.
                            CUSTOMER_EXISTS_BY_EMAIL_AND_PASSWORD
                            .getMessage()).
                    data(true)
                    .build();
        } else {
            return Message.builder().code(CustomerMessage
                            .CUSTOMER_NOT_EXISTS_BY_EMAIL_AND_PASSWORD
                            .getCode()).
                    message(CustomerMessage.
                            CUSTOMER_NOT_EXISTS_BY_EMAIL_AND_PASSWORD
                            .getMessage()).
                    data(false)
                    .build();
        }
    }

    @Transactional
    @Override
    public Message isCustomerExistsByIdForController(int id) {
        if (customerRepository.existsById(id)) {
            return Message.builder().code(CustomerMessage
                            .CUSTOMER_EXISTS_BY_ID
                            .getCode()).
                    message(CustomerMessage.
                            CUSTOMER_EXISTS_BY_ID
                            .getMessage()).
                    data(true)
                    .build();
        } else {
            return Message.builder().code(CustomerMessage
                            .CUSTOMER_NOT_EXISTS_BY_ID
                            .getCode()).
                    message(CustomerMessage.
                            CUSTOMER_NOT_EXISTS_BY_ID
                            .getMessage()).
                    data(false)
                    .build();
        }
    }

    @Transactional
    @Override
    public boolean login(String email, String password) throws CouponSystemException {
        if (!customerRepository.existsByEmailAndPassword(email, password)) {
            throw new CouponSystemException(GenericException.EMAIL_AND_PASSWORD_IS_NOT_CORRECT);
        }
        return true;
    }

    @Transactional
    @Override
    public CustomerDto getSingleByEmail(String email) throws CouponSystemException {
        if (!customerRepository.existsByEmail(email)) {
            throw new CouponSystemException(GenericException.EMAIL_IS_NOT_FOUND);
        }
        return utils.convertToDto(customerRepository.findByEmail(email), CustomerDto.class);
    }

    @Transactional
    @Override
    public void removePurchasesByCouponId(int couponId) throws CouponSystemException {
        if (!couponService.isExists(couponId)) {
            throw new CouponSystemException(CouponException.COUPON_NOT_EXISTS);
        }
        List<Customer> customers = customerRepository.findAll();

        customers.forEach(customer -> {
            boolean isRemoved = customer.getPurchases().removeIf((
                    purchase) -> purchase.getCoupon().getId()
                    == couponId);

            if (isRemoved) {
                customerRepository.saveAndFlush(customer);
            }
        });
    }

    @Transactional
    @Override
    public void removePurchasesByCompanyId(int companyId) throws CouponSystemException {
        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }

        List<Customer> customers = customerRepository.findAll();

        customers.forEach(customer -> {
            boolean isRemoved = customer.getPurchases().removeIf((
                    purchase) -> purchase.getCoupon().getCompany().getId()
                    == companyId);

            if (isRemoved) {
                customerRepository.saveAndFlush(customer);
            }
        });
    }

}

package com.johnbryce.coupon_system_part_2.tests.rest_template;

import com.johnbryce.coupon_system_part_2.console_messages_color.AsciiLogos;
import com.johnbryce.coupon_system_part_2.console_messages_color.ConsoleColorsUtil;
import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CategoryDto;
import com.johnbryce.coupon_system_part_2.utils.Utils;
import com.johnbryce.coupon_system_part_2.dto.CouponDto;
import com.johnbryce.coupon_system_part_2.dto.CustomerDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.security.ClientType;
import com.johnbryce.coupon_system_part_2.security.LoginManager;
import com.johnbryce.coupon_system_part_2.tests.interfaces.RunCustomerTest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class CustomerTest implements RunCustomerTest {

    @Value("${url.customer.controller}")
    private String customerUrl;
    @Value("${url.coupon.controller}")
    private String couponUrl;
    @Value("${url.category.controller}")
    private String categoryUrl;
    @Value("${url.purchase.controller}")
    private String purchaseUrl;

    private final Utils utils;
    private CustomerDto customer = null;

    private final LoginManager loginManager;
    private boolean isLoggedIn = false;

    private int customerId;
    private String customerEmail;

    private static int timeForThreadDelayMessage = 1300;


    @Override
    public void runAllCustomerTests() throws InterruptedException {
        //Sign in first as a customer
        loginTest();
        if (isLoggedIn) {
            Thread.sleep(timeForThreadDelayMessage);
            AsciiLogos.customerLogo();
            Thread.sleep(timeForThreadDelayMessage);
            //Testing all customer service functions - using controller with current data
            ConsoleColorsUtil.printSuccessfulMessage("Starting tests that should works...");
            //Thread is for delaying the message and test result for better reading
            Thread.sleep(timeForThreadDelayMessage);
            purchaseTest();
            Thread.sleep(timeForThreadDelayMessage);
            getCustomerCouponsTest();
            Thread.sleep(timeForThreadDelayMessage);
            getCustomerCouponsByCategoryTest();
            Thread.sleep(timeForThreadDelayMessage);
            getCustomerCouponsByMaxPriceTest();
            Thread.sleep(timeForThreadDelayMessage);
            getCustomerDetailsTest();
            Thread.sleep(timeForThreadDelayMessage);
            getSingleByEmailTest();
            Thread.sleep(timeForThreadDelayMessage);

        /*Testing all customer service functions - using controller
        with wrong data to check exceptions and situations
        that must fail*/

            ConsoleColorsUtil.printFailedMessage("Starting tests that shouldn't works...");
            wrongPurchasedThisCouponTest();
            Thread.sleep(timeForThreadDelayMessage);
            wrongPurchasedByWrongCustomerIdTest();
            Thread.sleep(timeForThreadDelayMessage);
            wrongPurchasedByWrongCouponIdTest();
            Thread.sleep(timeForThreadDelayMessage);
            getWrongCustomerCouponsTest();
            Thread.sleep(timeForThreadDelayMessage);
            getWrongCustomerCouponsByCategoryTest();
            Thread.sleep(timeForThreadDelayMessage);
            getWrongCustomerCouponsByMaxPriceTest();
            Thread.sleep(timeForThreadDelayMessage);
            getWrongCustomerDetailsTest();
            Thread.sleep(timeForThreadDelayMessage);
            getSingleByEmailWrongTest();
            Thread.sleep(timeForThreadDelayMessage);
        } else {
            System.out.println("Your log in details is wrong - therefore, You cannot access the system");
        }
    }

    @Override
    public void loginTest() {
        try {
            customer = utils.get(
                    customerUrl, "/getLastCustomerRecord", CustomerDto.class);
            customerId = customer.getId();
            customerEmail = customer.getEmail();
            String customerPassword = customer.getPassword();

            loginManager.
                    login(customerEmail, customerPassword, ClientType.CUSTOMER);
            isLoggedIn = true;
        } catch (CouponSystemException e) {
            isLoggedIn = false;
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }
        ConsoleColorsUtil.printSuccessfulMessage("\nCustomer logged in successfully\n");
    }

    @Override
    public void purchaseTest() {
        Message message = utils.postWithMessageResponseAndParamsOnly(purchaseUrl,
                "/{customerId}/{couponId}", customerId,
                getFirstRecordCoupon().getId());
        ConsoleColorsUtil.printSuccessfulMessage("Customer purchased coupon successfully: \n" + message);
    }

    @Override
    public void wrongPurchasedByWrongCustomerIdTest() {
        //Purchase coupon by wrong customer that not exists - should throw an CouponSystemException
        try {
            Message message = utils.postWithMessageResponseAndParamsOnly(purchaseUrl,
                    "/{customerId}/{couponId}", 100,
                    getLastRecordCoupon().getId());

            System.out.println(message);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void wrongPurchasedByWrongCouponIdTest() {
        //Purchase coupon by customer, wrong CouponId that not exists - should throw an CouponSystemException
        try {
            utils.getWithPath(purchaseUrl,
                    "/{customerId}/{couponId}", Message.class, customerId,
                    100);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void wrongPurchasedThisCouponTest() {
        //Purchase coupon that a customer already bought - should throw an CouponSystemException
        try {
            Message message = utils.getWithPath(purchaseUrl,
                    "/{customerId}/{couponId}", Message.class, customerId,
                    getFirstRecordCoupon().getId());
            System.out.println(message);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void getCustomerCouponsTest() {
        ConsoleColorsUtil.printSuccessfulMessage("Successfully got you" +
                " all customer's coupons purchases (if exists)");
        System.out.println("Customer that bought it: " + customer);
        List<CouponDto> coupons = utils.getListWithPath(couponUrl,
                "/getCustomerCoupons/{customerId}", CouponDto[].class, customerId);
        coupons.forEach(System.out::println);
    }

    @Override
    public void getWrongCustomerCouponsTest() {
            /*Trying to get coupons by customer that not exists
            Should throw an CouponSystemException*/
        try {
            utils.getListWithPath(couponUrl,
                    "/getCustomerCoupons/{customerId}", CouponDto[].class, 100);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void getCustomerCouponsByCategoryTest() {
        ConsoleColorsUtil.printSuccessfulMessage
                ("Successfully got you all customer's coupons" +
                        " by category 'sports' \n (if customer have it)");
        Message categoryExists =
                utils.getWithPath(categoryUrl, "/isCategoryExistsByName?name=sports",
                        Message.class);
        if (categoryExists.getData().equals(true)) {
            System.out.println(categoryExists);

            List<CouponDto> coupons = utils.getListWithRequestBody(couponUrl,
                    "/getCustomerCouponsByCategory/{customerId}",
                    CouponDto[].class, CategoryDto.builder().id(1).name("sports").build(),
                    customerId);
            coupons.forEach(System.out::println);

        } else {
            System.out.println(categoryExists);
        }
    }

    @Override
    public void getWrongCustomerCouponsByCategoryTest() {
            /*Trying to get customer's coupons filtered by randomCategory()
            with wrong customerId - should
            throw an CouponSystemException*/

        CategoryDto categoryDto = randomCategory();

        Message categoryExists =
                utils.getWithPath(categoryUrl, "/isCategoryExistsByName?name=" +
                                categoryDto.getName(),
                        Message.class);

        if (categoryExists.getData().equals(true)) {
            System.out.println(categoryExists);
            try {
                utils.getListWithPath(couponUrl,
                        "/getCustomerCouponsByCategoryId/{customerId}/{categoryId}",
                        CouponDto[].class, 100, categoryDto.getId()
                );
            } catch (Exception e) {
                ConsoleColorsUtil.printFailedMessage(e.getMessage());
            }

        } else {
            System.out.println(categoryExists);
        }
    }

    @Override
    public void getCustomerCouponsByMaxPriceTest() {
        ConsoleColorsUtil.printSuccessfulMessage("Successfully got you" +
                " all customer's coupons by max price of 400 dollars \n (if customer have it)");
        utils.getListWithPath(couponUrl,
                "/{customerId}/getCouponsByMaxPriceAndCustomerId?maxPrice=400",
                CouponDto[].class, customerId).forEach(System.out::println);
    }

    @Override
    public void getWrongCustomerCouponsByMaxPriceTest() {
        //Trying to get customer's coupons filtered by max price of 30 dollars with wrong customerId - should
        //throw an CouponSystemException
        try {
            utils.getListWithPath(couponUrl,
                    "/{customerId}/getCouponsByMaxPriceAndCustomerId?maxPrice=400",
                    CouponDto[].class, 100);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void getCustomerDetailsTest() {
        ConsoleColorsUtil.printSuccessfulMessage("Successfully got you one customer" +
                " as requested \n (if customer wasn't removed)");
        CustomerDto customer = utils.getWithPath(customerUrl, "/getSingle/{id}",
                CustomerDto.class, customerId);
        System.out.println(customer);
    }

    @Override
    public void getWrongCustomerDetailsTest() {
        //Trying to get customer that not exists - should throw an CouponSystemException
        try {
            CustomerDto customer = utils.getWithPath(customerUrl, "/getSingle/{id}",
                    CustomerDto.class, 100);
            System.out.println(customer);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }


    @Override
    public void getSingleByEmailTest() {
        ConsoleColorsUtil.printSuccessfulMessage(
                "Successfully got you a customer details by email as requested" +
                        " (if customer wasn't removed)");
        CustomerDto customerDto = utils.get(customerUrl,
                "/getSingleByEmail?email=" + customerEmail,
                CustomerDto.class);
        System.out.println(customerDto);
    }

    @Override
    public void getSingleByEmailWrongTest() {
        //Trying to get wrong customer details with email that not exists - should
        //throw an CouponSystemException
        try {
            utils.get(customerUrl,
                    "/getSingleByEmail?email=MosheMoshe@MosheMoshe.com",
                    CustomerDto.class);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    public CouponDto getLastRecordCoupon() {
        return utils.get(couponUrl,
                "/getLastRecordCoupon", CouponDto.class);
    }

    public CouponDto getFirstRecordCoupon() {
        return utils.get(couponUrl,
                "/getFirstRecordCoupon", CouponDto.class);
    }

    public CategoryDto randomCategory() {
        return utils.get(categoryUrl,
                "/getRandomCategory", CategoryDto.class);
    }


}

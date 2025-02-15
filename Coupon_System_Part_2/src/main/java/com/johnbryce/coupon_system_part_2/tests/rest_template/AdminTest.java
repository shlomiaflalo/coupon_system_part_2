package com.johnbryce.coupon_system_part_2.tests.rest_template;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.console_messages_color.AsciiLogos;
import com.johnbryce.coupon_system_part_2.console_messages_color.ConsoleColorsUtil;
import com.johnbryce.coupon_system_part_2.dto.CustomerDto;
import com.johnbryce.coupon_system_part_2.utils.Utils;
import com.johnbryce.coupon_system_part_2.dto.CompanyDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.security.ClientType;
import com.johnbryce.coupon_system_part_2.security.LoginManager;
import com.johnbryce.coupon_system_part_2.tests.interfaces.RunAdminTest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminTest implements RunAdminTest {

    @Value("${url.admin.controller}")
    private String adminUrl;
    private final Utils utils;
    private final LoginManager loginManager;
    private boolean isLoggedIn = false;
    private static int timeForThreadDelayMessage = 1300;


    @Override
    public void runAllAdminTests() throws InterruptedException {
        //Sign in first as an Admin
        loginTest();
        if (isLoggedIn) {
            AsciiLogos.adminLogo();
            Thread.sleep(timeForThreadDelayMessage);
            //Testing all Admin adminService functions - using controller with current data
            ConsoleColorsUtil.printSuccessfulMessage("Starting tests that should works...");
            //Thread is for delaying the message and test result for better reading
            Thread.sleep(timeForThreadDelayMessage);
            addCompanyTest();
            Thread.sleep(timeForThreadDelayMessage);
            updateCompanyTest();
            Thread.sleep(timeForThreadDelayMessage);
            deleteCompanyTest();
            Thread.sleep(timeForThreadDelayMessage);
            getAllCompaniesTest();
            Thread.sleep(timeForThreadDelayMessage);
            getOneCompanyTest();
            Thread.sleep(timeForThreadDelayMessage);
            addCustomerTest();
            Thread.sleep(timeForThreadDelayMessage);
            updateCustomerTest();
            Thread.sleep(timeForThreadDelayMessage);
            getAllCustomersTest();
            Thread.sleep(timeForThreadDelayMessage);
            getOneCustomerTest();
            Thread.sleep(timeForThreadDelayMessage);
            deleteCustomerTest();
            Thread.sleep(timeForThreadDelayMessage);
            getSingleCompanyByEmailTest();
            Thread.sleep(timeForThreadDelayMessage);
            getSingleCustomerByEmail();
            Thread.sleep(timeForThreadDelayMessage);

            /*Testing all admin service functions using controller -
            with wrong data to check exceptions and situations
            that must fail*/

            ConsoleColorsUtil.printFailedMessage("Starting tests that shouldn't works...");
            Thread.sleep(timeForThreadDelayMessage);
            addWrongCompanyTest();
            Thread.sleep(timeForThreadDelayMessage);
            updateWrongCompanyTest();
            Thread.sleep(timeForThreadDelayMessage);
            deleteWrongCompanyTest();
            Thread.sleep(timeForThreadDelayMessage);
            getOneWrongCompanyTest();
            Thread.sleep(timeForThreadDelayMessage);
            addWrongCustomerTest();
            Thread.sleep(timeForThreadDelayMessage);
            updateWrongCustomerTest();
            Thread.sleep(timeForThreadDelayMessage);
            deleteWrongCustomerTest();
            Thread.sleep(timeForThreadDelayMessage);
            getOneWrongCustomerTest();
            Thread.sleep(timeForThreadDelayMessage);
            getWrongSingleCompanyByEmailTest();
            Thread.sleep(timeForThreadDelayMessage);
            getWrongSingleCustomerByEmail();
            Thread.sleep(timeForThreadDelayMessage);
        } else {
            System.out.println("Your log in details is wrong - therefore," +
                    " You cannot access the system");
        }
    }

    @Override
    public void loginTest() throws InterruptedException {
        try {
            loginManager.login("admin@admin.com",
                    "admin", ClientType.ADMINISTRATOR);
            isLoggedIn = true;
        } catch (CouponSystemException e) {
            isLoggedIn = false;
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }
        ConsoleColorsUtil.printSuccessfulMessage("Admin logged in successfully\n");
        Thread.sleep(timeForThreadDelayMessage);
    }

    @Override
    public void addCompanyTest() {
        CompanyDto company = new CompanyDto(0, "Intel",
                "intel@gmail.com", "1234");
        CompanyDto companyAdded =
                utils.post(CompanyDto.class, adminUrl, "/company", company);
        ConsoleColorsUtil.printSuccessfulMessage(
                "Company is being added successfully: " + companyAdded);
    }

    @Override
    public void addWrongCompanyTest() {
        System.out.println("Getting a company from database to insert it again");
        CompanyDto company = utils.get(adminUrl, "/getLastCompanyRecord", CompanyDto.class);
        //Adding company that already exists -
        //should throw an CouponSystemException on controller
        try {
            utils.post(CompanyDto.class, adminUrl, "/company", company);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage("Company wasn't added to db: " + company);
        }

    }

    @Override
    public void updateCompanyTest() {
        CompanyDto companyFromDb = utils.get(adminUrl,
                "/getSingleCompanyByEmail?companyEmail=intel@gmail.com",
                CompanyDto.class);


        companyFromDb.setEmail("intel2@gmail.com");
        companyFromDb.setPassword("123456");

        Message message = utils.updateWithDifferentReturnType(
                Message.class, adminUrl, "/company/{companyId}", companyFromDb,
                companyFromDb.getId());
        ConsoleColorsUtil.printSuccessfulMessage("company updated successfully");
        System.out.println(message + "\n");
    }

    @Override
    public void updateWrongCompanyTest() {
        System.out.println("Getting a companies from database to update another existing company email");
        CompanyDto companyFromDb1 = utils.getWithPath
                (adminUrl, "/getOneCompany/{companyId}", CompanyDto.class, 5);

        CompanyDto companyFromDb2 = utils.getWithPath
                (adminUrl, "/getOneCompany/{companyId}", CompanyDto.class, 2);
        companyFromDb1.setEmail(companyFromDb2.getEmail());
        //Trying to update another company email on 'companyFromDb1'
        //Should throw an CouponSystemException on controller
        try {
            utils.updateWithDifferentReturnType(
                    Message.class, adminUrl, "/company/{companyId}", companyFromDb1,
                    companyFromDb1.getId());
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void deleteCompanyTest() {
        int companyId = 1;
        CompanyDto companyToDelete = utils.getWithPath
                (adminUrl, "/getOneCompany/{companyId}", CompanyDto.class, companyId);

        System.err.println("This company is going to be deleted: \n" +
                companyToDelete);

        Message message = utils.delete(Message.class, adminUrl,
                "/company/{companyId}",
                companyId);
        ConsoleColorsUtil.printSuccessfulMessage("company id: " + companyId + " is deleted successfully\n");
        System.out.println(message + "\n");
    }

    @Override
    public void deleteWrongCompanyTest() {
        int companyId = 1000;
        //Trying to delete company that don't exists -
        //should throw an CouponSystemException on controller
        try {
            utils.delete(Message.class, adminUrl, "/company/{companyId}",
                    companyId);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
            ;
        }
    }

    @Override
    public void getAllCompaniesTest() {
        ConsoleColorsUtil.printSuccessfulMessage("Successfully got you all companies");
        List<CompanyDto> companies =
                Arrays.stream(utils.get(adminUrl,
                        "/getAllCompanies", CompanyDto[].class)).toList();
        companies.forEach(System.out::println);
    }

    @Override
    public void getOneCompanyTest() {
        int companyId = 2;
        ConsoleColorsUtil.printSuccessfulMessage("Successfully got you one company as requested");
        CompanyDto companyFromDb = utils.getWithPath
                (adminUrl, "/getOneCompany/{companyId}", CompanyDto.class,
                        companyId);
        System.out.println(companyFromDb + "\n");
    }

    @Override
    public void getOneWrongCompanyTest() {
        int companyId = 300;
        //Trying to get a company that do not exist - should throw an CouponSystemException
        //on controller
        try {
            utils.getWithPath
                    (adminUrl, "/getOneCompany/{companyId}", CompanyDto.class, companyId);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
            ;
        }
    }

    @Override
    public void addCustomerTest() {
        CustomerDto customer = new CustomerDto(0,
                "George", "Orwell", "George@Orwell.com",
                "1234", null);
        ConsoleColorsUtil.printSuccessfulMessage("Successfully added a customer as requested");
        CustomerDto customerAdded =
                utils.post(CustomerDto.class, adminUrl, "/customer", customer);
        System.out.println(customerAdded + "\n");
    }

    @Override
    public void addWrongCustomerTest() {
        System.out.println("Getting a customer from database to insert it again");
        CustomerDto customerDto = utils.getWithPath
                (adminUrl, "/getOneCustomer/{customerId}", CustomerDto.class, 2);
        //Trying to add a customer that already exists - should throw an CouponSystemException
        //on controller
        try {
            utils.post(CustomerDto.class, adminUrl, "/customer", customerDto);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
            ;
        }
    }

    @Override
    public void updateCustomerTest() {
        CustomerDto customer = new CustomerDto(1, "Moshe", "Cohen", "Moshe@Cohen.com", "1234", null);
        ConsoleColorsUtil.printSuccessfulMessage("Successfully updated a customer with id: " + customer.getId() + " as requested");
        Message message = utils.updateWithDifferentReturnType
                (Message.class, adminUrl, "/customer/{customerId}", customer,
                        customer.getId());
        System.out.println(message + "\n");
    }

    @Override
    public void updateWrongCustomerTest() {
        System.out.println("Getting a customers from database" +
                " to update another existing customer email");
        CustomerDto customerFromDb1 = utils.getWithPath
                (adminUrl, "/getOneCustomer/{customerId}", CustomerDto.class, 2);
        CustomerDto customerFromDb2 = utils.getWithPath
                (adminUrl, "/getOneCustomer/{customerId}", CustomerDto.class, 3);
        customerFromDb1.setEmail(customerFromDb2.getEmail());
        //Trying to update a customer with another customer email -
        //should throw an CouponSystemException on controller
        try {
            utils.updateWithDifferentReturnType(
                    Message.class, adminUrl, "/customer/{customerId}", customerFromDb1,
                    customerFromDb1.getId()
            );
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
            ;
        }
    }

    @Override
    public void deleteCustomerTest() {
        int customerId = 1;
        ConsoleColorsUtil.printSuccessfulMessage("Successfully deleted a customer id" +
                " : " + customerId + " as requested");
        Message message = utils.delete(Message.class, adminUrl, "/customer/{customerId}",
                customerId);
        System.out.println(message + "\n");
    }

    @Override
    public void deleteWrongCustomerTest() {
        int customerId = 350;
        //Trying to delete a customer that don't exists -
        //should throw an CouponSystemException on controller
        try {
            utils.delete(Message.class, adminUrl, "/customer/{customerId}",
                    customerId);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
            ;
        }
    }

    @Override
    public void getAllCustomersTest() {
        ConsoleColorsUtil.printSuccessfulMessage("Successfully got you all customers");
        List<CustomerDto> customers =
                Arrays.stream(utils.get(adminUrl, "/getAllCustomers", CustomerDto[].class)).toList();
        customers.forEach(System.out::println);
    }


    @Override
    public void getOneCustomerTest() {
        int customerId = 1;
        ConsoleColorsUtil.printSuccessfulMessage("Successfully got you one customer as requested");
        CustomerDto customer = utils.getWithPath
                (adminUrl, "/getOneCustomer/{customerId}", CustomerDto.class, customerId);
        System.out.println(customer + "\n");
    }


    @Override
    public void getOneWrongCustomerTest() {
        int customerId = 500;
        //Trying to get a customer that don't exists -
        // should throw an CouponSystemException on controller
        try {
            utils.getWithPath
                    (adminUrl, "/getOneCustomer/{customerId}", CustomerDto.class, customerId);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
            ;
        }
    }


    @Override
    public void getSingleCompanyByEmailTest() {
        CompanyDto companyFromDb = utils.get(adminUrl,
                "/getSingleCompanyByEmail?companyEmail=intel2@gmail.com",
                CompanyDto.class);

        ConsoleColorsUtil.printSuccessfulMessage("Successfully got you " +
                "one company by email as requested: " + companyFromDb + "\n");
    }

    @Override
    public void getWrongSingleCompanyByEmailTest() {
        CompanyDto companyNotExists = null;
        try {
            companyNotExists = utils.getWithPath
                    (adminUrl, "/getOneCompany/{companyId}", CompanyDto.class, 700);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
            ;
        }

        //Trying to get a company by email that don't exists -
        //should throw an CouponSystemException on controller

        try {
            utils.get(adminUrl,
                    "/getSingleCompanyByEmail?companyEmail=" + companyNotExists.getEmail(),
                    CompanyDto.class);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
            ;
        }
    }

    @Override
    public void getSingleCustomerByEmail() {
        CustomerDto customer1 = utils.getWithPath
                (adminUrl, "/getOneCustomer/{customerId}", CustomerDto.class, 2);

        ConsoleColorsUtil.printSuccessfulMessage("Successfully got" +
                " you one company by email as requested");

        CustomerDto customer2 = utils.get(adminUrl,
                "/getSingleCustomerByEmail?customerEmail=" + customer1.getEmail(),
                CustomerDto.class);
        System.out.println(customer2 + "\n");
    }

    @Override
    public void getWrongSingleCustomerByEmail() {
        CustomerDto customer1 = null;
        try {
            customer1 = utils.getWithPath
                    (adminUrl, "/getOneCustomer/{customerId}", CustomerDto.class, 700);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
            ;
        }

        //Trying to get a customer by email that don't exists -
        //should throw an CouponSystemException on controller

        try {
            utils.get(adminUrl,
                    "/getSingleCustomerByEmail?customerEmail=" + customer1.getEmail(),
                    CustomerDto.class);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
            ;
        }
    }

}

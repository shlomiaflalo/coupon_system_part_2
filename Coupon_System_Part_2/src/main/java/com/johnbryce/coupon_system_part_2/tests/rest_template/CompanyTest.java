package com.johnbryce.coupon_system_part_2.tests.rest_template;

import com.johnbryce.coupon_system_part_2.console_messages_color.AsciiLogos;
import com.johnbryce.coupon_system_part_2.console_messages_color.ConsoleColorsUtil;
import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.utils.Utils;
import com.johnbryce.coupon_system_part_2.dto.CategoryDto;
import com.johnbryce.coupon_system_part_2.dto.CompanyDto;
import com.johnbryce.coupon_system_part_2.dto.CouponDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.security.ClientType;
import com.johnbryce.coupon_system_part_2.security.LoginManager;
import com.johnbryce.coupon_system_part_2.tests.interfaces.RunCompanyTest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CompanyTest implements RunCompanyTest {

    @Value("${url.company.controller}")
    private String companyUrl;

    @Value("${url.category.controller}")
    private String categoryUrl;

    @Value("${url.coupon.controller}")
    private String couponUrl;

    private final Utils utils;
    private final LoginManager loginManager;
    private boolean isLoggedIn = false;

    private CompanyDto company = null;
    private int companyId;

    private static int timeForThreadDelayMessage = 1300;

    @Override
    public void runAllCompanyTests() throws InterruptedException {
        //Sign in first as a company
        loginTest();
        if (isLoggedIn) {
            Thread.sleep(timeForThreadDelayMessage);
            AsciiLogos.companyLogo();
            Thread.sleep(timeForThreadDelayMessage);
            //Testing all company service functions - using controller with current data
            ConsoleColorsUtil.printSuccessfulMessage("Starting tests that should works...");
            //Thread is for delaying the message and test result for better reading
            Thread.sleep(timeForThreadDelayMessage);
            addCouponTest();
            Thread.sleep(timeForThreadDelayMessage);
            updateCouponTest();
            Thread.sleep(timeForThreadDelayMessage);
            deleteCouponTest();
            Thread.sleep(timeForThreadDelayMessage);
            getCompanyCouponsTest();
            Thread.sleep(timeForThreadDelayMessage);
            getCompanyCouponsFilteredByCategoryTest();
            Thread.sleep(timeForThreadDelayMessage);
            getCompanyCouponsFilteredByMaxPriceTest();
            Thread.sleep(timeForThreadDelayMessage);
            getCompanyDetailsTest();
            Thread.sleep(timeForThreadDelayMessage);
            getSingleByEmailTest();
            Thread.sleep(timeForThreadDelayMessage);

        /*Testing all company service functions using controller -
        with wrong data to check exceptions and situations
        that must fail*/

            ConsoleColorsUtil.printFailedMessage("Starting tests that shouldn't works...");
            Thread.sleep(timeForThreadDelayMessage);
            addCouponByWrongCompanyTest();
            Thread.sleep(timeForThreadDelayMessage);
            addCouponThatBeenAddedTest();
            Thread.sleep(timeForThreadDelayMessage);
            addCouponSameTitleTest();
            Thread.sleep(timeForThreadDelayMessage);
            updateCouponByWrongCompanyIdTest();
            Thread.sleep(timeForThreadDelayMessage);
            updateCouponByWrongCouponIdTest();
            Thread.sleep(timeForThreadDelayMessage);
            updateCouponOfAnotherCompanyTest();
            Thread.sleep(timeForThreadDelayMessage);
            updateCouponSameTitleTest();
            Thread.sleep(timeForThreadDelayMessage);
            deleteCouponByWrongCouponIdTest();
            Thread.sleep(timeForThreadDelayMessage);
            deleteCouponByWrongCompanyIdTest();
            Thread.sleep(timeForThreadDelayMessage);
            getCompanyCouponsFilteredByWrongCompanyIdTest();
            Thread.sleep(timeForThreadDelayMessage);
            getCompanyCouponsFilteredByMaxPriceByWrongCompanyIdTest();
            Thread.sleep(timeForThreadDelayMessage);
            getWrongCompanyDetailsTest();
            Thread.sleep(timeForThreadDelayMessage);
            getSingleByWrongEmailTest();
            Thread.sleep(timeForThreadDelayMessage);
        } else {
            System.out.println("Your log in details is wrong - therefore, You cannot access the system");
        }
    }

    @Override
    public void loginTest() {
        try {
            company = utils.get(
                    companyUrl, "/getLastCompanyRecord", CompanyDto.class);
            companyId = company.getId();
            String companyEmail = company.getEmail();
            String companyPassword = company.getPassword();

            loginManager.login(companyEmail, companyPassword, ClientType.COMPANY);
            isLoggedIn = true;
        } catch (CouponSystemException e) {
            isLoggedIn = false;
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }
        ConsoleColorsUtil.printSuccessfulMessage("\nCompany logged in successfully\n");
    }

    @Override
    public void addCouponTest() {
        CategoryDto categoryDto = utils.get(categoryUrl,
                "/getRandomCategory", CategoryDto.class);

        CouponDto coupon = CouponDto.builder()
                .company(company).
                category(categoryDto)
                .title("50% off the 'spring collections' :) using java")
                .description("In israel")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .amount(20)
                .price(5.5)
                .image("path: https//www.johnBryce.co.il/Image")
                .build();

        utils.postWithMessageResponseAndPaths(couponUrl,
                "/addCouponByCompany/{companyId}", coupon, coupon.getCompany().getId());

        ConsoleColorsUtil.printSuccessfulMessage(
                "Successfully added the coupon\n" + getLastRecordCoupon());
    }

    @Override
    public void addCouponByWrongCompanyTest() {
        //Trying to Add coupon by wrong company that not exists -
        // should throw an CouponSystemException from controller
        CouponDto couponDto = utils.get(couponUrl,
                "/getFirstRecordCoupon", CouponDto.class);
        try {
            Message message =
                    utils.postWithMessageResponseAndPaths(couponUrl,
                            "/addCouponByCompany/{companyId}", couponDto, 100);
            System.out.println(message);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void addCouponThatBeenAddedTest() {
        CouponDto couponAdded = utils.get(couponUrl,
                "/getFirstRecordCoupon", CouponDto.class);

        //Trying to add coupon that has been added already -
        //should throw an CouponSystemException

        try {
            Message message =
                    utils.postWithMessageResponseAndPaths(couponUrl,
                            "/addCouponByCompany/{companyId}", couponAdded, companyId);
            System.out.println(message);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }


    }

    @Override
    public void addCouponSameTitleTest() {
        //Trying to add coupon that has same title like another coupon that belong to
        //that companyId - should throw an CouponSystemException

        //Taking record that exists on db and change set its id
        //But not changing its title for the test
        CouponDto couponAdded = getLastRecordCoupon();
        couponAdded.setId(300);

        try {
            //I don't use the companyId value because I deleted its coupon on the 1st part of the test
            Message message =
                    utils.postWithMessageResponseAndPaths(couponUrl,
                            "/addCouponByCompany/{companyId}",
                            couponAdded, couponAdded.getCompany().getId());
            System.out.println(message);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }


    }

    @Override
    public void updateCouponTest() {
        LocalDate startDate = LocalDate.now().plusDays(3);
        LocalDate endDate = LocalDate.now().plusDays(9);
        CategoryDto randomCategory = utils.get(categoryUrl,
                "/getRandomCategory", CategoryDto.class);

        CouponDto couponFromDbToUpdate = getLastRecordCoupon();

        CouponDto couponToUpdate = CouponDto.builder()
                .id(couponFromDbToUpdate.getId()).
                category(randomCategory)
                .title("75% off the" +
                        " 'spring collections' :) using java")
                .description("In Morocco")
                .startDate(startDate)
                .endDate(endDate)
                .amount(23)
                .price(6.6)
                .image("path: https//www.johnBryce.co.il/Image")
                .build();

        Message couponResponse = utils.
                updateWithDifferentReturnType(Message.class, couponUrl,
                        "/updateCouponByCompany/{companyId}",
                        couponToUpdate, companyId);

        ConsoleColorsUtil.printSuccessfulMessage("Successfully updated the coupon\n" + couponResponse);
    }

    @Override
    public void updateCouponByWrongCompanyIdTest() {
        //Trying to Update the last record with wrong companyId
        //should throw an CouponSystemException
        CouponDto coupon = getLastRecordCoupon();
        coupon.setPrice(500);
        coupon.setDescription("Some description");

        try {
            CouponDto couponResponse = utils.
                    updateWithPath(CouponDto.class, couponUrl,
                            "/updateCouponByCompany/{companyId}",
                            coupon, 500);
            System.out.println(couponResponse);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void updateCouponByWrongCouponIdTest() {
        //Trying to Update the last record with wrong couponId
        //should throw an CouponSystemException
        try {
            CouponDto couponResponse = utils.
                    updateWithPath(CouponDto.class, couponUrl,
                            "/updateCouponByCompany/{companyId}",
                            new CouponDto(), companyId);
            System.out.println(couponResponse);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void updateCouponOfAnotherCompanyTest() {
        //Trying to Update coupon that belong to another company
        //should throw an CouponSystemException
        try {
            CouponDto couponAdded = utils.get(couponUrl,
                    "/getFirstRecordCoupon", CouponDto.class);

            CouponDto couponResponse = utils.
                    updateWithPath(CouponDto.class, couponUrl,
                            "/updateCouponByCompany/{companyId}",
                            couponAdded, companyId);
            System.out.println(couponResponse);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void updateCouponSameTitleTest() {
        //Trying to update coupon that has same title like another coupon that belong to
        //that companyId if company have any coupons of course -
        //should throw an CouponSystemException

        //On the update if a company doesn't change its coupon title
        //It's will let it update the coupon but - if a company update its coupon title to
        //another title that exists already on this company coupons we will get
        //'CouponSystemException' (COUPON_TITLE_EXISTS_FOR_THIS_COMPANY)

        //Adding 2 coupons to the companyId for testing, Taking the other coupon's title
        //that belong to that company and insert it to the other and test the CouponSystemException
        LocalDate startDate = LocalDate.now().plusDays(4);
        LocalDate endDate = LocalDate.now().plusDays(9);
        CategoryDto randomCategory = utils.get(categoryUrl,
                "/getRandomCategory", CategoryDto.class);

        CouponDto couponFirst = CouponDto.builder()
                .company(company).
                category(randomCategory)
                .title("In israel")
                .description("50% off the 'spring collections' :) using java")
                .startDate(startDate)
                .endDate(endDate)
                .amount(20)
                .price(5.5)
                .image("path: https//www.johnBryce.co.il/Image")
                .build();

        /*Adding 1st coupon to company
        Saving the coupon that been inserted just now for later use*/
        utils.postWithMessageResponseAndPaths(couponUrl,
                "/addCouponByCompany/{companyId}", couponFirst, companyId);

        CouponDto couponSecondFromDb = getLastRecordCoupon();
        couponSecondFromDb.setTitle("Some Title");
        couponSecondFromDb.setCategory(null);
        couponSecondFromDb.setId(0);
        //Adding 2nd coupon to company with different title
        utils.postWithMessageResponseAndPaths(couponUrl,
                "/addCouponByCompany/{companyId}"
                , couponSecondFromDb, companyId);

        //Now after inserting 2 coupons for the company - we take title from first coupon and put it
        //in the second coupon and try to update it to the db - According to
        //the instructions of the project
        //Company can't have duplicates titles for its coupons

        couponSecondFromDb = getLastRecordCoupon();
        couponSecondFromDb.setTitle(couponFirst.getTitle());
        try {
            Message couponResponse = utils.
                    updateWithDifferentReturnType(Message.class, couponUrl,
                            "/updateCouponByCompany/{companyId}",
                            couponSecondFromDb, companyId);
            System.out.println(couponResponse);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }


    @Override
    public void deleteCouponTest() {
        Message message = utils.delete(Message.class, couponUrl,
                "/deleteCouponByCompany/{companyId}/{couponId}"
                , companyId, getLastRecordCoupon().getId());

        ConsoleColorsUtil.printSuccessfulMessage(
                "Successfully deleted the coupon\n" + message);
    }

    @Override
    public void deleteCouponByWrongCouponIdTest() {
        try {
            Message message = utils.delete(Message.class, couponUrl,
                    "/deleteCouponByCompany/{companyId}/{couponId}", companyId, 700);
            ConsoleColorsUtil.printSuccessfulMessage(
                    "failed: \n" + message);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void deleteCouponByWrongCompanyIdTest() {
        try {

            Message message = utils.delete(Message.class, couponUrl,
                    "/deleteCouponByCompany/{companyId}/{couponId}",
                    700, getLastRecordCoupon().getId());
            ConsoleColorsUtil.printSuccessfulMessage(
                    "failed: \n" + message);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }
    }

    @Override
    public void getCompanyCouponsTest() {
        try {
            List<CouponDto> companyCoupons = utils.getListWithPath(
                    couponUrl, "/getCompanyCoupons/{companyId}", CouponDto[].class,
                    company.getId());
            if (!companyCoupons.isEmpty()) {
                System.out.println("This coupons belong to this company: " + company);
                companyCoupons.forEach(System.out::println);
                ConsoleColorsUtil.printSuccessfulMessage("Successfully got you all company's coupons");
            }
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void getCompanyCouponsFilteredByCategoryTest() {

        try {
            List<CouponDto> companyCoupons = utils.getListWithPath(
                    couponUrl, "/getCompanyCouponsByCategoryId/{companyId}/{categoryId}",
                    CouponDto[].class, company.getId(), 1);
            companyCoupons.forEach(System.out::println);
            ConsoleColorsUtil.printSuccessfulMessage("Successfully got you" +
                    " all company's coupons filtered by category id (sports) - (if exists)");
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void getCompanyCouponsFilteredByWrongCompanyIdTest() {
        try {
            utils.getListWithPath(couponUrl,
                    "/getCompanyCouponsByCategoryId/{companyId}/{categoryId}",
                    CouponDto[].class, 700, 1);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void getCompanyCouponsFilteredByMaxPriceTest() {
        try {
            List<CouponDto> companyCoupons = utils.getListWithPath(
                    couponUrl, "/{companyId}/getCompanyCouponsByMaxPrice?maxPrice = " + 300,
                    CouponDto[].class, company.getId());
            companyCoupons.forEach(System.out::println);
            ConsoleColorsUtil.printSuccessfulMessage("Successfully got you all company's coupons filtered by max price of 300$ - (if exists)");

        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    @Override
    public void getCompanyCouponsFilteredByMaxPriceByWrongCompanyIdTest() {
        try {
            utils.getListWithPath(
                    couponUrl, "/{companyId}/getCompanyCouponsByMaxPrice?maxPrice = " + 30,
                    CouponDto[].class, 70);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }
    }

    @Override
    public void getCompanyDetailsTest() {
        ConsoleColorsUtil.printSuccessfulMessage("Successfully" +
                " got you company info - (if not been removed)");
        CompanyDto companyDto =
                utils.getWithPath(companyUrl, "/{companyId}", CompanyDto.class, company.getId());
        System.out.println(companyDto);
    }

    @Override
    public void getWrongCompanyDetailsTest() {
        try {
            utils.getWithPath(companyUrl, "/{companyId}"
                    , CompanyDto.class, 600);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }


    @Override
    public void getSingleByEmailTest() {
        CompanyDto companyDto = utils.get(companyUrl,
                "/getSingleByEmail?email=" + company.getEmail(), CompanyDto.class);
        System.out.println(companyDto);
        ConsoleColorsUtil.printSuccessfulMessage("Successfully got you company info by email - (if not been removed)");
    }

    @Override
    public void getSingleByWrongEmailTest() {
        //Find a company by wrong email that not exist
        try {
            utils.get(companyUrl,
                    "/getSingleByEmail?email=MosheMoshe@JustForException.com", CompanyDto.class);
        } catch (Exception e) {
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }

    }

    public CouponDto getLastRecordCoupon() {
        return utils.get(couponUrl,
                "/getLastRecordCoupon", CouponDto.class);
    }

}

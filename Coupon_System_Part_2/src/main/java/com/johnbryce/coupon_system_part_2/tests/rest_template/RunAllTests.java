package com.johnbryce.coupon_system_part_2.tests.rest_template;


import com.johnbryce.coupon_system_part_2.console_messages_color.ConsoleColorsUtil;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.tests.interfaces.RunAdminTest;
import com.johnbryce.coupon_system_part_2.tests.interfaces.RunCompanyTest;
import com.johnbryce.coupon_system_part_2.tests.interfaces.RunCustomerTest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(3)
public class RunAllTests implements CommandLineRunner {

    private final RunAdminTest runAdminTest;
    private final RunCustomerTest runCustomerTest;
    private final RunCompanyTest runCompanyTest;

    public void runAllTests() throws CouponSystemException, InterruptedException {
        try {
            //Testing admin & company & customer services using controller
            runAdminTest.runAllAdminTests();
            runCustomerTest.runAllCustomerTests();
            runCompanyTest.runAllCompanyTests();


        } catch (DataAccessException e) {
            /* Finished waiting for a connection from the spring pool -
              time is out and exception is being thrown.
             connection-timeout=20000 (18 seconds if no
             connection is available it's coming here)*/
            ConsoleColorsUtil.printFailedMessage(e.getMessage());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        runAllTests();
    }
}

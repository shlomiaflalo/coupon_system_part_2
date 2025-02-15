package com.johnbryce.coupon_system_part_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CouponSystemPart2Application {

    public static void main(String[] args) {
        SpringApplication.run(CouponSystemPart2Application.class, args);
        System.out.println("\nServer is running");
    }

}

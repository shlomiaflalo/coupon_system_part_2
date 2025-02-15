package com.johnbryce.coupon_system_part_2.coupons_cleaner;

import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponExpirationDailyJob {

    private final CouponService couponService;

    @Scheduled(cron = "0 0 0 * * *")
    public void removeAllExpiredCoupons() throws CouponSystemException {
        couponService.removeAllExpiredCoupons();
    }

}

package com.johnbryce.coupon_system_part_2.controllers;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CouponDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;


    @GetMapping("/getCompanyCoupons/{companyId}")
    public List<CouponDto> getCompanyCoupons(@PathVariable("companyId") int companyId) throws CouponSystemException {
        return couponService.getCompanyCoupons(companyId);
    }

    @GetMapping("/{companyId}/getCompanyCouponsByMaxPrice")
    public List<CouponDto> getCompanyCoupons(@PathVariable("companyId") int companyId,
                                             @RequestParam("maxPrice") double maxPrice) throws CouponSystemException {
        return couponService.getCompanyCoupons(companyId, maxPrice);
    }

    @GetMapping("/{customerId}/getCouponsByMaxPriceAndCustomerId")
    public List<CouponDto> getCouponsByMaxPriceAndCustomerId(@RequestParam("maxPrice")
                                                             double maxPrice, @PathVariable("customerId") int customerId) throws CouponSystemException {
        return couponService.getCustomerCouponsByMaxPrice(customerId, maxPrice);
    }

    @GetMapping("/getCouponsByCategoryIdAndCompanyId/{categoryId}/{companyId}")
    public List<CouponDto> getCouponsByCategoryIdAndCompanyId(@PathVariable("categoryId") int categoryId,
                                                              @PathVariable("companyId") int companyId) throws CouponSystemException {
        return couponService.getCouponsByCategoryIdAndCompanyId(categoryId, companyId);
    }

    @GetMapping("/getLastRecordCoupon")
    public CouponDto getLastRecordCoupon() throws CouponSystemException {
        return couponService.getLastRecordCoupon();
    }

    @GetMapping("/getFirstRecordCoupon")
    public CouponDto getFirstRecordCoupon() throws CouponSystemException {
        return couponService.getFirstRecordCoupon();
    }

    @GetMapping("/getCustomerCouponsByCategoryId/{customerId}/{categoryId}")
    public List<CouponDto> getCustomerCouponsByCategoryId(@PathVariable("customerId")
                                                          int customerId, @PathVariable("categoryId") int categoryId)
            throws CouponSystemException {
        return couponService.getCustomerCouponsByCategoryId(customerId, categoryId);
    }


    @GetMapping("/isCouponAvailable/{couponId}")
    public Message isCouponAvailable(@PathVariable("couponId") int couponId) throws CouponSystemException {
        return couponService.isCouponAvailable(couponId);
    }

    @GetMapping("/dueDateCouponCheck/{couponId}")
    public Message dueDateCouponCheck(@PathVariable("couponId") int couponId) throws CouponSystemException {
        return couponService.dueDateCouponCheck(couponId);
    }

    @GetMapping("/{companyId}/isCouponTitleExistsByCompanyId")
    public Message isCouponTitleExistsByCompanyId(@RequestParam("title") String title, @PathVariable("companyId") int companyId) throws CouponSystemException {
        return couponService.isCouponTitleExistsByCompanyId(companyId, title);
    }

    @GetMapping("/isCouponExistsByCouponIdAndCompanyId/{couponId}/{companyId}")
    public Message isCouponExistsByCouponIdAndCompanyId
            (@PathVariable("couponId") int couponId, @PathVariable("companyId") int companyId) throws CouponSystemException {
        return couponService.isCouponExistsByCouponIdAndCompanyId(couponId, companyId);
    }

    @GetMapping("/{companyId}/{CouponId}/existsByCompanyIdAndTitleExclude")
    public Message isCouponTitleExistsByCompanyIdExclude(@PathVariable("companyId") int companyId,
                                                         @PathVariable("CouponId") int CouponId, @RequestParam("title") String title) throws CouponSystemException {

        return couponService.isCouponTitleExistsByCompanyIdExclude(companyId, title, CouponId);
    }


    @GetMapping("/getCustomerCoupons/{customerId}")
    public List<CouponDto> getCustomerCoupons(@PathVariable("customerId") int customerId) throws CouponSystemException {
        return couponService.getCustomerCoupons(customerId);
    }


    @PostMapping("/addCouponByCompany/{companyId}")
    public Message addCouponByCompany(@PathVariable("companyId") int companyId,
                                      @RequestBody CouponDto coupon) throws Exception {
        return couponService.addCouponByCompany(companyId, coupon);
    }


    @PutMapping("/updateCouponByCompany/{companyId}")
    public Message updateCouponByCompany(@PathVariable("companyId") int companyId, @RequestBody CouponDto coupon) throws Exception {
        return couponService.updateCouponByCompany(companyId, coupon);
    }

    @DeleteMapping("/deleteCouponByCompany/{companyId}/{couponId}")
    public Message deleteCouponByCompany(@PathVariable("companyId") int companyId, @PathVariable("couponId") int couponId) throws CouponSystemException {
        return couponService.deleteCouponByCompany(companyId, couponId);
    }


    @GetMapping("/getCompanyCouponsByCategoryId/{companyId}/{categoryId}")
    public List<CouponDto> getCompanyCouponsByCategoryId
            (@PathVariable("companyId") int companyId, @PathVariable("categoryId") int categoryId) throws CouponSystemException {
        return couponService.getCompanyCouponsByCategoryId(companyId, categoryId);
    }

    @GetMapping("/isCouponExistsByIdForController/{id}")
    public Message isCouponExistsByIdForController(@PathVariable("id") int id) throws CouponSystemException {
        return couponService.isCouponExistsByIdForController(id);
    }

    @PostMapping
    public CouponDto add(@RequestBody CouponDto couponDto) throws Exception {
        return couponService.add(couponDto);
    }


    @PutMapping("/{couponId}")
    public Message update(@RequestBody CouponDto couponDto, @PathVariable("couponId") int couponId) throws Exception {
        return couponService.update(couponDto, couponId);
    }


    @DeleteMapping("/{couponId}")
    public Message delete(@PathVariable("couponId") int couponId) throws Exception {
        return couponService.delete(couponId);
    }

    @GetMapping("/all")
    public List<CouponDto> getAll() throws CouponSystemException {
        return couponService.getAll();
    }

    @GetMapping("/{couponId}")
    public CouponDto getSingle(@PathVariable("couponId") int couponId) throws Exception {
        return couponService.getSingle(couponId);
    }


}

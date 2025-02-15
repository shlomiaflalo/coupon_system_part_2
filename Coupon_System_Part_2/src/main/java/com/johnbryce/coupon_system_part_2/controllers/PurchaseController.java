package com.johnbryce.coupon_system_part_2.controllers;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.PurchaseDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.purchase.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("api/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;


    @PostMapping("/{customerId}/{couponId}")
    public Message purchaseCoupon
            (@PathVariable("customerId") int customerId, @PathVariable("couponId") int couponId) throws Exception {
        return purchaseService.purchaseCoupon(customerId, couponId);
    }


    @GetMapping("/isCouponPurchasedByCustomer/{customerId}/{couponId}")
    public Message isCouponPurchasedByCustomer
            (@PathVariable("customerId") int customerId, @PathVariable("couponId") int couponId) throws CouponSystemException {
        return purchaseService.isCouponPurchasedByCustomer(customerId, couponId);
    }


    @GetMapping("/purchasesByCustomerId/{customerId}")
    public List<PurchaseDto> getPurchasesByCustomerId(@PathVariable("customerId") int customerId) throws CouponSystemException {
        return purchaseService.getCustomerPurchases(customerId);
    }


    @DeleteMapping("/deleteCouponPurchase/{customerId}/{couponId}")
    public Message deleteCouponPurchase
            (@PathVariable("customerId") int customerId,
             @PathVariable("couponId") int couponId) throws Exception {
        return purchaseService.deleteCouponPurchase(customerId, couponId);
    }


    @DeleteMapping("/deleteCouponPurchasesByCouponId/{couponId}")
    public Message deleteCouponPurchasesByCouponId(@PathVariable("couponId") int couponId) throws Exception {
        return purchaseService.deleteCouponPurchasesByCouponId(couponId);
    }


    @GetMapping("/getCouponsByCustomer/{customerId}")
    public List<PurchaseDto> getCouponsByCustomer(@PathVariable("customerId") int customerId) throws CouponSystemException {
        return purchaseService.getCustomerPurchases(customerId);
    }


    @DeleteMapping("/deleteCouponPurchaseByCompanyId/{companyId}")
    public Message deleteCouponPurchaseByCompanyId(@PathVariable("companyId") int companyId) throws Exception {
        return purchaseService.deleteCouponPurchaseByCompanyId(companyId);
    }


    @GetMapping("/isPurchaseExistsByIdForController/{id}")
    public Message isPurchaseExistsByIdForController(@PathVariable("id") int id) throws CouponSystemException {
        return purchaseService.isPurchaseExistsByIdForController(id);
    }


    @DeleteMapping("/deleteCouponPurchaseByCustomerId/{customerId}")
    public Message deleteCouponPurchaseByCustomerId(@PathVariable("customerId") int customerId) throws CouponSystemException {
        return purchaseService.deleteCouponPurchaseByCustomerId(customerId);
    }


    @PostMapping
    public PurchaseDto add(@RequestBody PurchaseDto purchaseDto) throws Exception {
        return purchaseService.add(purchaseDto);
    }


    @PutMapping("/{id}")
    public Message update(@RequestBody PurchaseDto purchaseDto, @PathVariable("id") Integer id) throws Exception {
        return purchaseService.update(purchaseDto, id);
    }


    @DeleteMapping("/{id}")
    public Message delete(@PathVariable("id") int id) throws Exception {
        return purchaseService.delete(id);
    }


    @GetMapping("/all")
    public List<PurchaseDto> getAll() throws SQLException, CouponSystemException {
        return purchaseService.getAll();
    }


    @GetMapping("/{id}")
    public PurchaseDto getSingle(@PathVariable("id") int id) throws Exception {
        return purchaseService.getSingle(id);
    }

}

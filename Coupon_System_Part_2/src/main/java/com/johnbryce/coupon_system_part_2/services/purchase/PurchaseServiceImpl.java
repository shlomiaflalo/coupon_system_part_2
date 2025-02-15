package com.johnbryce.coupon_system_part_2.services.purchase;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.app_messages.PurchaseMessage;
import com.johnbryce.coupon_system_part_2.dto.CouponDto;
import com.johnbryce.coupon_system_part_2.dto.CustomerDto;
import com.johnbryce.coupon_system_part_2.dto.PurchaseDto;
import com.johnbryce.coupon_system_part_2.entities.Purchase;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.exceptions.companies.CompanyException;
import com.johnbryce.coupon_system_part_2.exceptions.coupons.CouponException;
import com.johnbryce.coupon_system_part_2.exceptions.customers.CustomerException;
import com.johnbryce.coupon_system_part_2.exceptions.generic.GenericException;
import com.johnbryce.coupon_system_part_2.exceptions.purchases.PurchaseException;
import com.johnbryce.coupon_system_part_2.repositories.PurchaseRepository;
import com.johnbryce.coupon_system_part_2.services.company.CompanyService;
import com.johnbryce.coupon_system_part_2.services.coupon.CouponService;
import com.johnbryce.coupon_system_part_2.services.customer.CustomerService;
import com.johnbryce.coupon_system_part_2.utils.Utils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final CouponService couponService;
    private final Utils utils;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               @Lazy CustomerService customerService,
                               @Lazy CompanyService companyService,
                               @Lazy CouponService couponService,
                               Utils utils) {
        this.purchaseRepository = purchaseRepository;
        this.customerService = customerService;
        this.companyService = companyService;
        this.couponService = couponService;
        this.utils = utils;
    }

    @Transactional
    @Override
    public Message isPurchaseExistsByIdForController(int integer) {
        if (purchaseRepository.existsById(integer)) {
            return Message.builder().code(PurchaseMessage
                            .PURCHASE_EXISTS_BY_ID
                            .getCode()).
                    message(PurchaseMessage
                            .PURCHASE_EXISTS_BY_ID
                            .getMessage()).
                    data(true).
                    build();
        } else {
            return Message.builder().code(PurchaseMessage
                            .PURCHASE_NOT_EXISTS_BY_ID
                            .getCode()).
                    message(PurchaseMessage.
                            PURCHASE_NOT_EXISTS_BY_ID.
                            getMessage()).
                    data(false).
                    build();
        }

    }

    @Transactional
    @Override
    public Message purchaseCoupon(int customerId, int couponId) throws Exception {

        checkCouponAndPurchaseOrUpdate(customerId, couponId,
                true, true);

        PurchaseDto purchaseInserted =
                utils.convertToDto(purchaseRepository.
                        findFirstByOrderByIdDesc(), PurchaseDto.class);

        return Message.builder().code(PurchaseMessage
                        .SUCCESSFULLY_PURCHASE_COUPON
                        .getCode()).
                message(PurchaseMessage.
                        SUCCESSFULLY_PURCHASE_COUPON
                        .getMessage()).
                data(purchaseInserted).
                build();
    }

    @Transactional
    @Override
    public PurchaseDto add(PurchaseDto purchase) throws Exception {
        checkCouponAndPurchaseOrUpdate
                (purchase.getCustomer().getId(), purchase.getCoupon().getId(),
                        true, false, purchase.getReview());
        return utils.convertToDto(purchaseRepository.
                findFirstByOrderByIdDesc(), PurchaseDto.class);
    }

    @Transactional
    @Override
    public Message update(PurchaseDto purchase, Integer id) throws CouponSystemException {
        if (!purchaseRepository.existsById(id)) {
            throw new CouponSystemException(PurchaseException.PURCHASE_NOT_EXISTS);
        }
        checkCouponAndPurchaseOrUpdate(purchase.getCustomer().getId(),
                purchase.getCoupon().getId(), false, false,
                purchase.getReview(), id);

        PurchaseDto purchaseInserted =
                utils.convertToDto(purchaseRepository.
                        findPurchaseByCoupon_idAndCustomer_id(
                                purchase.getCoupon().getId(),
                                purchase.getCustomer().getId()), PurchaseDto.class);

        return Message.builder().code(PurchaseMessage
                        .PURCHASE_GOT_UPDATED
                        .getCode()).
                message(PurchaseMessage.
                        PURCHASE_GOT_UPDATED
                        .getMessage()).
                data(purchaseInserted)
                .build();

    }

    @Transactional
    @Override
    public Message delete(Integer integer) throws CouponSystemException {
        Purchase purchase = purchaseRepository.findById(integer)
                .orElseThrow(() -> new CouponSystemException(
                        PurchaseException.PURCHASE_NOT_EXISTS));

        return Message.builder().code(PurchaseMessage
                        .PURCHASE_GOT_DELETED
                        .getCode()).
                message(PurchaseMessage.
                        PURCHASE_GOT_DELETED
                        .getMessage()).
                data(utils.convertToDto(purchase, PurchaseDto.class))
                .build();

    }

    @Transactional
    @Override
    public List<PurchaseDto> getAll() throws CouponSystemException {
        return utils.convertFromEntityListToDto(purchaseRepository.findAll(), PurchaseDto.class);
    }

    @Transactional
    @Override
    public PurchaseDto getSingle(Integer integer) throws CouponSystemException {
        Purchase purchase = purchaseRepository.findById(integer).orElseThrow(()
                -> new CouponSystemException(PurchaseException.PURCHASE_NOT_EXISTS));
        return utils.convertToDto(purchase, PurchaseDto.class);
    }

    @Transactional
    @Override
    public boolean isExists(Integer integer) {
        return purchaseRepository.existsById(integer);
    }

    @Transactional
    @Override
    public Message isCouponPurchasedByCustomer(int customerId, int couponId) {
        if (purchaseRepository.existsPurchaseByCoupon_IdAndCustomer_Id(couponId, customerId)) {
            return Message.builder().code(PurchaseMessage
                            .PURCHASE_COUPON_BY_CUSTOMER
                            .getCode()).
                    message(PurchaseMessage.
                            PURCHASE_COUPON_BY_CUSTOMER
                            .getMessage()).
                    data(true)
                    .build();
        } else {
            return Message.builder().code(PurchaseMessage
                            .HAS_NOT_PURCHASE_COUPON_BY_CUSTOMER
                            .getCode()).
                    message(PurchaseMessage.
                            HAS_NOT_PURCHASE_COUPON_BY_CUSTOMER
                            .getMessage()).
                    data(false)
                    .build();
        }

    }

    @Transactional
    @Override
    public List<PurchaseDto> getCustomerPurchases(int customerId) throws CouponSystemException {
        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);

        }
        List<PurchaseDto> purchases = utils.convertFromEntityListToDto(
                purchaseRepository.findAllByCustomer_id(customerId),
                PurchaseDto.class);
        if (!purchases.isEmpty()) {
            return purchases;
        } else {
            throw new CouponSystemException(GenericException.EMPTY_LIST);
        }
    }


    @Transactional
    @Override
    public Message deleteCouponPurchase(int customerId, int couponId) throws CouponSystemException {
        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }
        if (!couponService.isExists(couponId)) {
            throw new CouponSystemException(CouponException.COUPON_NOT_EXISTS);
        }

        Purchase purchase = Optional.ofNullable(purchaseRepository
                        .findPurchaseByCoupon_idAndCustomer_id(couponId, customerId)).
                orElseThrow(() -> new CouponSystemException(PurchaseException.PURCHASE_NOT_EXISTS));

        PurchaseDto purchaseForMessage =
                utils.convertToDto(purchase, PurchaseDto.class);

        return Message.builder().code(PurchaseMessage
                        .PURCHASE_GOT_DELETED
                        .getCode()).
                message(PurchaseMessage.
                        PURCHASE_GOT_DELETED
                        .getMessage()).
                data(purchaseForMessage)
                .build();
    }

    @Transactional
    @Override
    public Message deleteCouponPurchasesByCouponId(int couponId) throws CouponSystemException, SQLException {

        if (!couponService.isExists(couponId)) {
            throw new CouponSystemException(CouponException.COUPON_NOT_EXISTS);
        }

        List<Purchase> purchases =
                purchaseRepository.findAllByCoupon_Id(couponId);

        customerService.removePurchasesByCouponId(couponId);

        return Message.builder().code(PurchaseMessage
                        .PURCHASES_GOT_DELETED_BY_COUPON_ID
                        .getCode()).
                message(PurchaseMessage.
                        PURCHASES_GOT_DELETED_BY_COUPON_ID
                        .getMessage()).
                data(utils.convertFromEntityListToDto(purchases, PurchaseDto.class))
                .build();
    }

    @Transactional
    @Override
    public Message deleteCouponPurchaseByCompanyId(int companyId) throws CouponSystemException, SQLException {
        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        List<Purchase> purchases = purchaseRepository.findAllByCoupon_Company_Id(companyId);
        customerService.removePurchasesByCompanyId(companyId);

        return Message.builder().code(PurchaseMessage
                        .PURCHASES_BY_COMPANY_GOT_REMOVED
                        .getCode()).
                message(PurchaseMessage.
                        PURCHASES_BY_COMPANY_GOT_REMOVED
                        .getMessage()).
                data(utils.convertFromEntityListToDto(purchases, PurchaseDto.class))
                .build();

    }

    @Transactional
    @Override
    public Message deleteCouponPurchaseByCustomerId(int customerId)
            throws CouponSystemException {

        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }

        List<Purchase> purchases = purchaseRepository.
                findAllByCustomer_id(customerId);

        if (purchases.isEmpty()) {
            throw new CouponSystemException(GenericException.EMPTY_LIST);
        }

        customerService.cleanPurchaseListByCustomerId(customerId);

        return Message.builder().code(PurchaseMessage
                        .PURCHASES_BY_CUSTOMER_GOT_REMOVED
                        .getCode()).
                message(PurchaseMessage.
                        PURCHASES_BY_CUSTOMER_GOT_REMOVED
                        .getMessage()).
                data(utils.convertFromEntityListToDto(purchases, PurchaseDto.class))
                .build();
    }

    @Transactional
    @Override
    public void updateOrAddPurchasesViaCustomer(PurchaseDto purchaseDto) throws CouponSystemException {
        CustomerDto customer = customerService.getSingle(purchaseDto.getCustomer().getId());
        List<PurchaseDto> updateListOnCustomer = new ArrayList<>();

        if (customer.getPurchases() != null && !customer.getPurchases().isEmpty()) {
            updateListOnCustomer.addAll(customer.getPurchases());
        }

        updateListOnCustomer.add(purchaseDto);

        customer.setPurchases(updateListOnCustomer);
        customerService.insertOrUpdateCustomerAfterLogic(customer, customer.getId());
    }

    @Transactional
    @Override
    public void checkCouponAndPurchaseOrUpdate(int customerId, int couponId,
                                               boolean addOrUpdatePurchase, boolean autoReview,
                                               Integer... reviewAndPurchaseIdForUpdate) throws CouponSystemException {
        //1. First Check: if coupon exists
        if (!couponService.isExists(couponId)) {
            throw new CouponSystemException(CouponException.COUPON_NOT_EXISTS);
        }

        //2. 2ND Check: if customer exists
        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }

        CouponDto couponFromDb = couponService.getSingle(couponId);

        //3. 3rd Check: if coupon have been purchased by customer
        if (purchaseRepository.existsPurchaseByCoupon_IdAndCustomer_Id(couponId, customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_BOUGHT_THIS_COUPON_BEFORE);
        }

        //4. Check if customer bought this coupon type before
        if (couponService.isCouponTypePurchased(customerId, couponId).getData().equals(true)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_BOUGHT_THIS_COUPON_TYPE_BEFORE);
        }

        //5. Check if the coupon a customer trying to buy still exists on the inventory
        if (couponFromDb.getAmount() <= 0) {
            throw new CouponSystemException(CouponException.COUPON_IS_OUT_OF_STOCK);
        }

        //6. Checking that the due date of a coupon wasn't arrived.
        if (couponFromDb.getEndDate().isBefore(LocalDate.now())) {
            throw new CouponSystemException(CouponException.DUE_DATE_COUPON);
        }

        PurchaseDto purchaseDto;

        if (autoReview) {
            purchaseDto = PurchaseDto.
                    builder().customer(customerService.getSingle(customerId))
                    .coupon(couponFromDb).
                    review(ThreadLocalRandom.current().nextInt(1, 6))
                    .build();
        } else {
            purchaseDto = PurchaseDto.
                    builder().
                    customer(customerService.getSingle(customerId))
                    .coupon(couponFromDb).
                    review(reviewAndPurchaseIdForUpdate[0])
                    .build();
        }

        if (!addOrUpdatePurchase) {
            //On update
            purchaseDto.setId(reviewAndPurchaseIdForUpdate[1]);
        }

       /*7. Purchasing the coupon via customer entity by adding it to the purchase
        list or updating it on customer purchase list - coupon will get purchased ur update
        depends on the addOrUpdate value (this function used in few places)*/
        updateOrAddPurchasesViaCustomer
                (purchaseDto);

              /*8. Lowering the inventory's coupons in 1
              (-1 coupon after purchasing it) and updating the
              purchased column which Not exists on dto,
              for database only (updated by +1) - using native query*/
        couponService.updateCouponAmountAndPurchased(couponId);
    }
}

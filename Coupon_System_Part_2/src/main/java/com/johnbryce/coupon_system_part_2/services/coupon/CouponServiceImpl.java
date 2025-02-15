package com.johnbryce.coupon_system_part_2.services.coupon;

import com.johnbryce.coupon_system_part_2.app_messages.CouponMessage;
import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CouponDto;
import com.johnbryce.coupon_system_part_2.dto.PurchaseDto;
import com.johnbryce.coupon_system_part_2.entities.Coupon;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.exceptions.categories.CategoryException;
import com.johnbryce.coupon_system_part_2.exceptions.companies.CompanyException;
import com.johnbryce.coupon_system_part_2.exceptions.coupons.CouponException;
import com.johnbryce.coupon_system_part_2.exceptions.customers.CustomerException;
import com.johnbryce.coupon_system_part_2.repositories.CouponRepository;
import com.johnbryce.coupon_system_part_2.services.category.CategoryService;
import com.johnbryce.coupon_system_part_2.services.company.CompanyService;
import com.johnbryce.coupon_system_part_2.services.customer.CustomerService;
import com.johnbryce.coupon_system_part_2.services.purchase.PurchaseService;
import com.johnbryce.coupon_system_part_2.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final PurchaseService purchaseService;
    private final CategoryService categoryService;
    private final Utils utils;

    public CouponServiceImpl(CouponRepository couponRepository,
                             @Lazy CustomerService customerService,
                             @Lazy CompanyService companyService,
                             @Lazy PurchaseService purchaseService,
                             @Lazy CategoryService categoryService,
                             Utils utils) {
        this.couponRepository = couponRepository;
        this.customerService = customerService;
        this.companyService = companyService;
        this.purchaseService = purchaseService;
        this.categoryService = categoryService;
        this.utils = utils;
    }

    @Override
    public Message isCouponExistsByIdForController(int integer) {
        if (couponRepository.existsById(integer)) {
            return Message.builder().code(CouponMessage
                            .COUPON_EXISTS_BY_ID
                            .getCode()).
                    message(CouponMessage.
                            COUPON_EXISTS_BY_ID.
                            getMessage()).
                    data(true).
                    build();
        } else {
            return Message.builder().code(CouponMessage
                            .COUPON_NOT_EXISTS_BY_ID
                            .getCode()).
                    message(CouponMessage.
                            COUPON_NOT_EXISTS_BY_ID.
                            getMessage()).
                    data(false).
                    build();
        }

    }

    @Transactional
    @Override
    public void updateCouponAmountAndPurchased(int couponId) {
        couponRepository.updateCouponAmountAndPurchased(couponId);
    }

    @Transactional
    @Override
    public List<CouponDto> getCompanyCoupons(int companyId) throws CouponSystemException {
        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        return utils.convertFromEntityListToDto
                (couponRepository.findAllByCompany_Id(companyId), CouponDto.class);
    }

    @Override
    public List<CouponDto> getCouponsByCategoryIdAndCompanyId(int categoryId, int companyId) throws CouponSystemException {
        if (!categoryService.isExists(categoryId)) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS_BY_ID);
        }
        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        return utils.convertFromEntityListToDto
                (couponRepository.findAllByCompany_IdAndCategory_Id(companyId, categoryId), CouponDto.class);
    }

    @Transactional
    @Override
    public Message removeAllExpiredCoupons() {
        couponRepository.removeAllExpiredCoupons();
        System.out.println("All expired coupons that exists have been removed");
        return Message.builder().code(CouponMessage
                        .REMOVING_EXPIRED_COUPONS
                        .getCode()).
                message(CouponMessage.
                        REMOVING_EXPIRED_COUPONS.
                        getMessage()).
                data(couponRepository.findAllByEndDateLessThan(LocalDate.now())).
                build();
    }

    @Override
    public CouponDto getLastRecordCoupon() throws CouponSystemException {
        return utils.convertToDto
                (couponRepository.
                                findFirstByOrderByIdDesc(),
                        CouponDto.class);
    }

    @Override
    public CouponDto getFirstRecordCoupon() throws CouponSystemException {
        return utils.convertToDto
                (couponRepository.findFirstByOrderByIdAsc(),
                        CouponDto.class);
    }


    @Override
    public Message isCouponAvailable(int couponId) throws CouponSystemException {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() ->
                new CouponSystemException(CouponException.COUPON_NOT_EXISTS));

        if (coupon.getAmount() > 0) {
            return Message.builder().code(CouponMessage
                            .COUPON_AMOUNT_AVAILABLE
                            .getCode()).
                    message(CouponMessage.
                            COUPON_AMOUNT_AVAILABLE.
                            getMessage()).
                    data(true).
                    build();
        } else {
            return Message.builder().code(CouponMessage
                            .COUPON_AMOUNT_NOT_AVAILABLE
                            .getCode()).
                    message(CouponMessage.
                            COUPON_AMOUNT_NOT_AVAILABLE.
                            getMessage()).
                    data(false).
                    build();
        }

    }

    @Override
    public Message dueDateCouponCheck(int couponId) throws CouponSystemException {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() ->
                new CouponSystemException(CouponException.COUPON_NOT_EXISTS));


        if (coupon.getEndDate().isAfter(LocalDate.now())) {
            return Message.builder().code(CouponMessage
                            .COUPON_DUE_DATE_IS_GOOD
                            .getCode()).
                    message(CouponMessage.
                            COUPON_DUE_DATE_IS_GOOD.
                            getMessage()).
                    data(true).
                    build();
        } else {
            return Message.builder().code(CouponMessage
                            .COUPON_DUE_DATE_IS_BEHIND
                            .getCode()).
                    message(CouponMessage.
                            COUPON_DUE_DATE_IS_BEHIND.
                            getMessage()).
                    data(false).
                    build();
        }

    }

    @Override
    public Message isCouponTitleExistsByCompanyId(int companyId, String title) {
        if (couponRepository.existsByCompanyIdAndTitle(companyId, title)) {
            return Message.builder().code(CouponMessage
                            .COUPON_TITLE_EXISTS_BY_COMPANY_ID
                            .getCode()).
                    message(CouponMessage.
                            COUPON_TITLE_EXISTS_BY_COMPANY_ID.
                            getMessage()).
                    data(true).
                    build();
        } else {
            return Message.builder().code(CouponMessage
                            .COUPON_TITLE_NOT_EXISTS_BY_COMPANY_ID
                            .getCode()).
                    message(CouponMessage.
                            COUPON_TITLE_NOT_EXISTS_BY_COMPANY_ID.
                            getMessage()).
                    data(false).
                    build();
        }
    }

    @Override
    public Message isCouponExistsByCouponIdAndCompanyId(int couponId, int companyId) {
        if (couponRepository.existsByIdAndCompany_Id(couponId, companyId)) {
            return Message.builder().code(CouponMessage
                            .COUPON_EXISTS_BY_ID_AND_COMPANY_ID
                            .getCode()).
                    message(CouponMessage.
                            COUPON_EXISTS_BY_ID_AND_COMPANY_ID.
                            getMessage()).
                    data(true).
                    build();

        } else {
            return Message.builder().code(CouponMessage
                            .COUPON_NOT_EXISTS_BY_ID_AND_COMPANY_ID
                            .getCode()).
                    message(CouponMessage.
                            COUPON_NOT_EXISTS_BY_ID_AND_COMPANY_ID.
                            getMessage()).
                    data(false).
                    build();
        }
    }

    @Override
    public Message isCouponTitleExistsByCompanyIdExclude(int companyId, String title, int CouponId) {
        if (couponRepository.existsByCompany_idAndTitleAndIdNot(companyId, title, CouponId)) {
            return Message.builder().code(CouponMessage
                            .COUPON_TITLE_EXISTS_BY_COMPANY_ID_EXCLUDE
                            .getCode()).
                    message(CouponMessage.
                            COUPON_TITLE_EXISTS_BY_COMPANY_ID_EXCLUDE.
                            getMessage()).
                    data(true).
                    build();

        } else {
            return Message.builder().code(CouponMessage
                            .COUPON_TITLE_NOT_EXISTS_BY_COMPANY_ID_EXCLUDE
                            .getCode()).
                    message(CouponMessage.
                            COUPON_TITLE_NOT_EXISTS_BY_COMPANY_ID_EXCLUDE.
                            getMessage()).
                    data(false).
                    build();
        }
    }

    @Override
    public List<CouponDto> getCustomerCoupons(int customerId) throws CouponSystemException {
        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }
        return purchaseService.getCustomerPurchases(customerId).stream().map(PurchaseDto::getCoupon).toList();
    }


    @Override
    public List<CouponDto> getCustomerCouponsByCategoryId(int customerId, int categoryId) throws CouponSystemException {
        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }
        if (!categoryService.isExists(categoryId)) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS_BY_ID);
        }
        return purchaseService.getCustomerPurchases(customerId).stream()
                .map(PurchaseDto::getCoupon).filter(coupon -> coupon.getCategory().getId() == categoryId).toList();
    }

    @Override
    public List<CouponDto> getCustomerCouponsByMaxPrice(int customerId, double maxPrice) throws CouponSystemException {
        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }
        if (maxPrice <= 0 || maxPrice > 1_000_000) {
            throw new CouponSystemException(CouponException.COUPON_MAX_PRICE_IS_OUT_OF_RANGE);
        }

        return purchaseService.getCustomerPurchases(customerId).stream()
                .map(PurchaseDto::getCoupon).filter(coupon -> coupon.getPrice() <= maxPrice).toList();
    }

    @Transactional
    @Override
    public CouponDto add(CouponDto coupon) throws CouponSystemException {
        if (couponRepository.existsById(coupon.getId())) {
            throw new CouponSystemException(CouponException.COUPON_ALREADY_EXISTS);
        }

        if (couponRepository.existsByTitleAndDescription(coupon.getTitle(), coupon.getDescription())) {
            throw new CouponSystemException(CouponException.COUPON_EXISTS_BY_TITLE_AND_DESCRIPTION);
        }

        coupon.setCompany(companyService.getRandomCompany());
        coupon.setCategory(categoryService.getRandomCategory());

        Coupon couponAdded = utils.convertFromDto(coupon, Coupon.class);
        Coupon couponInDb = couponRepository.save(couponAdded);
        return utils.convertToDto(couponInDb, CouponDto.class);
    }

    @Transactional
    @Override
    public Message update(CouponDto coupon, Integer id) throws CouponSystemException {

        CouponDto couponDto = utils.convertToDto(
                couponRepository.findById(id).orElseThrow(
                        () -> new CouponSystemException(CouponException.COUPON_NOT_EXISTS)),
                CouponDto.class);

        if (couponRepository.existsByTitleAndDescriptionAndIdNot
                (coupon.getTitle(), coupon.getDescription(), id)) {
            throw new CouponSystemException(CouponException.COUPON_EXISTS_BY_TITLE_AND_DESCRIPTION);
        }

        if (coupon.getId() != id) {
            coupon.setId(id);
        }

        coupon.setCategory(couponDto.getCategory());
        coupon.setCompany(couponDto.getCompany());
        Coupon couponUpdated = utils.convertFromDto(coupon, Coupon.class);

        Coupon couponInDb = couponRepository.save(couponUpdated);

        CouponDto couponForMessage = utils.convertToDto(couponInDb, CouponDto.class);

        return Message.builder().code(CouponMessage
                        .COUPON_GOT_UPDATED
                        .getCode()).
                message(CouponMessage.
                        COUPON_GOT_UPDATED.
                        getMessage()).
                data(couponForMessage).
                build();

    }

    @Transactional
    @Override
    public Message delete(Integer integer) throws CouponSystemException {
        Coupon coupon = couponRepository.findById(integer).orElseThrow(
                () -> new CouponSystemException(CouponException.COUPON_NOT_EXISTS));

        couponRepository.deleteById(integer);

        CouponDto couponForMessage = utils.convertToDto(coupon, CouponDto.class);

        return Message.builder().code(CouponMessage
                        .COUPON_GOT_DELETED
                        .getCode()).
                message(CouponMessage.
                        COUPON_GOT_DELETED.
                        getMessage()).
                data(couponForMessage).
                build();
    }

    @Override
    public List<CouponDto> getAll() throws CouponSystemException {
        return utils.convertFromEntityListToDto(couponRepository.findAll(), CouponDto.class);
    }

    @Override
    public CouponDto getSingle(Integer integer) throws CouponSystemException {
        Coupon coupon = couponRepository.findById(integer).
                orElseThrow(
                        () -> new CouponSystemException(CouponException.COUPON_NOT_EXISTS));
        return utils.convertToDto(coupon, CouponDto.class);
    }

    @Override
    public boolean isExists(Integer integer) {
        return couponRepository.existsById(integer);
    }

    @Override
    public List<CouponDto> getCompanyCoupons(int companyId, double maxPrice) throws CouponSystemException {
        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        if (maxPrice <= 0 || maxPrice > 1_000_000) {
            throw new CouponSystemException(CouponException.COUPON_MAX_PRICE_IS_OUT_OF_RANGE);
        }

        return utils.convertFromEntityListToDto(couponRepository.
                findAllByCompanyIdAndPriceIsLessThanEqual(companyId, maxPrice), CouponDto.class);
    }

    @Transactional
    @Override
    public Message addCouponByCompany(int companyId, CouponDto coupon) throws CouponSystemException {
        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        if (couponRepository.existsById(coupon.getId())) {
            throw new CouponSystemException(CouponException.COUPON_ALREADY_EXISTS);
        }
        if (couponRepository.existsByCompanyIdAndTitle(companyId, coupon.getTitle())) {
            throw new CouponSystemException(CouponException.COUPON_TITLE_EXISTS_FOR_THIS_COMPANY);
        }

        coupon.setCompany(companyService.getSingle(companyId));
        if (coupon.getCategory() == null) {
            coupon.setCategory(categoryService.getRandomCategory());
        }
        Coupon couponInDb = couponRepository.save(utils.convertFromDto(coupon, Coupon.class));

        CouponDto couponForMessage = utils.convertToDto(couponInDb, CouponDto.class);

        return Message.builder().code(CouponMessage
                        .COUPON_IS_ADDED_FOR_COMPANY
                        .getCode()).
                message(CouponMessage.
                        COUPON_IS_ADDED_FOR_COMPANY.
                        getMessage()).
                data(couponForMessage).
                build();
    }

    @Transactional
    @Override
    public Message updateCouponByCompany(int companyId, CouponDto coupon) throws CouponSystemException {
        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }

        CouponDto couponDto = utils.convertToDto(couponRepository.findById(coupon.getId()).orElseThrow(
                () -> new CouponSystemException(CouponException.COUPON_NOT_EXISTS)
        ), CouponDto.class);

        if (!couponRepository.existsByIdAndCompany_Id(coupon.getId(), companyId)) {
            throw new CouponSystemException(CouponException.CANNOT_EDIT_COUPON_OF_ANOTHER_COMPANY);
        }

        //In case the client want to update coupon with title that exists for this company
        //According to the instructions we cannot add a new coupon with duplicate title
        if (couponRepository.existsByCompany_idAndTitleAndIdNot(companyId,
                coupon.getTitle(), coupon.getId())) {
            throw new CouponSystemException(CouponException.COUPON_TITLE_EXISTS_FOR_THIS_COMPANY);
        }

        coupon.setCompany(companyService.getSingle(companyId));
        if (coupon.getCategory() == null) {
            coupon.setCategory(couponDto.getCategory());
        }
        Coupon couponInDb = couponRepository.save(utils.convertFromDto(coupon, Coupon.class));

        CouponDto couponForMessage = utils.convertToDto(couponInDb, CouponDto.class);

        return Message.builder().code(CouponMessage
                        .COUPON_GOT_UPDATED
                        .getCode()).
                message(CouponMessage.
                        COUPON_GOT_UPDATED.
                        getMessage()).
                data(couponForMessage).
                build();

    }

    @Transactional
    @Override
    public Message deleteCouponByCompany(int companyId, int couponId) throws CouponSystemException {
        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() ->
                new CouponSystemException(CouponException.COUPON_NOT_EXISTS));

        companyService.deleteCouponByCompanyId(companyId, coupon.getId());

        CouponDto couponForMessage = utils.convertToDto(coupon, CouponDto.class);

        return Message.builder().code(CouponMessage
                        .COUPON_GOT_DELETED
                        .getCode()).
                message(CouponMessage.
                        COUPON_GOT_DELETED.
                        getMessage()).
                data(couponForMessage).
                build();
    }

    @Override
    public List<CouponDto> getCompanyCouponsByCategoryId(int companyId, int categoryId) throws CouponSystemException {
        if (!companyService.isExists(companyId)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        if (!categoryService.isExists(categoryId)) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS_BY_ID);
        }

        return utils.convertFromEntityListToDto(couponRepository.
                findAllByCompany_IdAndCategory_Id(
                        companyId, categoryId), CouponDto.class);
    }


    @Override
    public Message isCouponTypePurchased(int customerId, int couponId) throws CouponSystemException {
        Coupon coupon = couponRepository.findById(couponId).
                orElseThrow(() -> new CouponSystemException(CouponException.COUPON_NOT_EXISTS));

        if (!customerService.isExists(customerId)) {
            throw new CouponSystemException(CustomerException.CUSTOMER_IS_NOT_EXISTS);
        }

        CouponDto couponDto = utils.convertFromDto(coupon, CouponDto.class);

        boolean couponCategoryFoundOnCustomerPurchases = false;
        boolean isNotHaveAnyPurchaseHistory = true;
        try {
            if (!purchaseService.getCustomerPurchases(customerId).isEmpty()) {
                isNotHaveAnyPurchaseHistory = false;
            }
        } catch (CouponSystemException e) {
            log.error("", e);
        }

        if (!isNotHaveAnyPurchaseHistory) {
            try {
                couponCategoryFoundOnCustomerPurchases =
                        purchaseService.getCustomerPurchases(customerId).stream().
                                map(PurchaseDto::getCoupon).anyMatch(c ->
                                        c.getCategory().equals(couponDto.getCategory()));
            } catch (CouponSystemException e) {
                log.error("", e);
            }
        }

        if (couponCategoryFoundOnCustomerPurchases) {
            return Message.builder().code(CouponMessage
                            .COUPON_TYPE_PURCHASED
                            .getCode()).
                    message(CouponMessage.
                            COUPON_TYPE_PURCHASED.
                            getMessage()).
                    data(true).
                    build();
        } else {
            return Message.builder().code(CouponMessage
                            .COUPON_TYPE_NOT_PURCHASED
                            .getCode()).
                    message(CouponMessage.
                            COUPON_TYPE_NOT_PURCHASED
                            .getMessage()).
                    data(false).
                    build();
        }


    }
}

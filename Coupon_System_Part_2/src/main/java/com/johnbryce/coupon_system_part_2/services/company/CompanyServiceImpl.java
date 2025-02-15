package com.johnbryce.coupon_system_part_2.services.company;

import com.johnbryce.coupon_system_part_2.app_messages.CompanyMessage;
import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CompanyDto;
import com.johnbryce.coupon_system_part_2.entities.Company;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.exceptions.companies.CompanyException;
import com.johnbryce.coupon_system_part_2.exceptions.coupons.CouponException;
import com.johnbryce.coupon_system_part_2.exceptions.customers.CustomerException;
import com.johnbryce.coupon_system_part_2.exceptions.generic.GenericException;
import com.johnbryce.coupon_system_part_2.repositories.CompanyRepository;
import com.johnbryce.coupon_system_part_2.services.coupon.CouponService;
import com.johnbryce.coupon_system_part_2.utils.Utils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CouponService couponService;
    private final Utils utils;

    public CompanyServiceImpl(CompanyRepository companyRepository,
                              @Lazy CouponService couponService,
                              Utils utils) {
        this.companyRepository = companyRepository;
        this.couponService = couponService;
        this.utils = utils;
    }

    @Override
    public CompanyDto getRandomCompany() throws CouponSystemException {
        CompanyDto companyDto =
                utils.convertToDto(companyRepository.findRandomCompany(), CompanyDto.class);
        if (companyDto == null) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }
        return companyDto;
    }

    @Override
    public CompanyDto getFirstCompanyRecord() throws CouponSystemException {
        return utils.convertToDto(companyRepository.findFirstByOrderByIdAsc(), CompanyDto.class);
    }

    @Override
    public CompanyDto getLastCompanyRecord() throws CouponSystemException {
        return utils.convertToDto(companyRepository.findFirstByOrderByIdDesc(), CompanyDto.class);

    }

    @Override
    public Message isCompanyEmailExistsExclude(String email, int companyId) {
        return Message.builder().code(CompanyMessage.
                        COMPANY_EMAIL_EXISTS_EXCLUDE.getCode()).
                message(CompanyMessage.
                        COMPANY_EMAIL_EXISTS_EXCLUDE.
                        getMessage()).
                data(companyRepository.
                        existsByEmailAndIdNot(email, companyId)).
                build();
    }

    @Override
    public Message isCompanyExistsByEmailAndPassword(String email, String password) {
        return Message.builder().code(CompanyMessage.
                        COMPANY_EXISTS_BY_EMAIL_AND_PASSWORD.getCode()).
                message(CompanyMessage.
                        COMPANY_EXISTS_BY_EMAIL_AND_PASSWORD.
                        getMessage()).
                data(companyRepository.existsByEmailAndPassword(email, password)).
                build();
    }

    @Override
    public Message isCompanyExistsByName(String name) {
        return Message.builder().code(CompanyMessage.
                        COMPANY_EXISTS_BY_NAME
                        .getCode()).
                message(CompanyMessage.
                        COMPANY_EXISTS_BY_NAME.
                        getMessage()).
                data(companyRepository.existsByName(name)).
                build();
    }

    @Override
    public Message isCompanyExistsByEmail(String email) {
        return Message.builder().code(CompanyMessage.
                        COMPANY_EXISTS_BY_EMAIL
                        .getCode()).
                message(CompanyMessage.
                        COMPANY_EXISTS_BY_EMAIL.
                        getMessage()).
                data(companyRepository.existsByEmail(email)).
                build();
    }

    @Override
    public Message isCompanyExistsByNameAndId(String name, int id) {
        return Message.builder().code(CompanyMessage.
                        COMPANY_EXISTS_BY_NAME_AND_ID
                        .getCode()).
                message(CompanyMessage.
                        COMPANY_EXISTS_BY_NAME_AND_ID.
                        getMessage()).
                data(companyRepository.existsByNameAndId(name, id)).
                build();
    }

    @Transactional
    @Override
    public CompanyDto add(CompanyDto companyDto) throws CouponSystemException {
        if (companyRepository.existsByName(companyDto.getName())) {
            throw new CouponSystemException(CompanyException.COMPANY_NAME_ALREADY_EXISTS);
        }
        if (companyRepository.existsByEmail(companyDto.getEmail())) {
            throw new CouponSystemException(CompanyException.COMPANY_EMAIL_ALREADY_EXISTS);
        }
        if (companyRepository.existsById(companyDto.getId())) {
            throw new CouponSystemException(CompanyException.COMPANY_ID_ALREADY_EXISTS);
        }

        Company company = utils.convertFromDto(companyDto, Company.class);
        Company companyInDb = companyRepository.save(company);
        return utils.convertToDto(companyInDb, CompanyDto.class);
    }

    @Transactional
    @Override
    public Message update(CompanyDto company, Integer id) throws CouponSystemException {
        if (!companyRepository.existsById(id)) {
            throw new CouponSystemException(CompanyException.COMPANY_NOT_FOUND);
        }

        if (!companyRepository.existsByNameAndId(company.getName(), id)) {
            throw new CouponSystemException(CompanyException.CANNOT_EDIT_COMPANY_NAME_AND_ID);
        }

        //If there is any company with this email except myself - because i can update my
        //email if no other company is using it.
        if (companyRepository.existsByEmailAndIdNot(company.getEmail(), company.getId())) {
            throw new CouponSystemException(CustomerException.EMAIL_IN_USE_BY_ANOTHER_CUSTOMER);
        }

        if (company.getId() != id) {
            company.setId(id);
        }

        Company companyToUpdate = utils.convertFromDto(company, Company.class);
        Company companyInDb = companyRepository.save(companyToUpdate);

        return Message.builder().code(CompanyMessage.
                        COMPANY_IS_UPDATED
                        .getCode()).
                message(CompanyMessage.
                        COMPANY_IS_UPDATED.
                        getMessage()).
                data(utils.convertToDto(companyInDb, CompanyDto.class)).
                build();
    }

    @Transactional
    @Override
    public Message delete(Integer integer) throws CouponSystemException {
        Company company = companyRepository.findById(integer).
                orElseThrow(() ->
                        new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));

        companyRepository.deleteById(integer);

        return Message.builder().code(CompanyMessage.
                        COMPANY_IS_DELETED
                        .getCode()).
                message(CompanyMessage.
                        COMPANY_IS_DELETED.
                        getMessage()).
                data(utils.convertToDto(company, CompanyDto.class)).
                build();
    }

    @Override
    public List<CompanyDto> getAll() throws CouponSystemException {
        return utils.convertFromEntityListToDto(companyRepository.findAll(), CompanyDto.class);
    }

    @Override
    public CompanyDto getSingle(Integer integer) throws CouponSystemException {
        Company company = companyRepository.findById(integer).orElseThrow(() ->
                new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));
        return utils.convertToDto(company, CompanyDto.class);
    }

    @Override
    public boolean isExists(Integer integer) {
        return companyRepository.existsById(integer);
    }

    @Override
    public Message isCompanyExistsByIdForController(int integer) {
        if (companyRepository.existsById(integer)) {
            return Message.builder().code(CompanyMessage.
                            COMPANY_EXISTS
                            .getCode()).
                    message(CompanyMessage.
                            COMPANY_EXISTS.
                            getMessage()).
                    data(true).
                    build();
        } else {
            return Message.builder().code(CompanyMessage.
                            COMPANY_NOT_EXISTS
                            .getCode()).
                    message(CompanyMessage.
                            COMPANY_NOT_EXISTS.
                            getMessage()).
                    data(false).
                    build();
        }

    }

    @Override
    public void deleteCouponByCompanyId(int companyId, int couponId) throws CouponSystemException {
        Company company = companyRepository.findById(companyId).orElseThrow(() ->
                new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));

        if (!couponService.isExists(couponId)) {
            throw new CouponSystemException(CouponException.COUPON_NOT_EXISTS);
        }
        for (int i = 0; i < company.getCoupons().size(); i++) {
            if (company.getCoupons().get(i).getId() == couponId) {
                company.getCoupons().remove(i);
                return;
            }
        }
        companyRepository.save(company);
    }

    @Override
    public boolean login(String email, String password) throws CouponSystemException {
        if (!companyRepository.existsByEmailAndPassword(email, password)) {
            throw new CouponSystemException(GenericException.EMAIL_AND_PASSWORD_IS_NOT_CORRECT);
        }
        return true;
    }

    @Override
    public CompanyDto getSingleByEmail(String email) throws CouponSystemException {
        if (!companyRepository.existsByEmail(email)) {
            throw new CouponSystemException(GenericException.EMAIL_IS_NOT_FOUND);
        }
        return utils.convertToDto(companyRepository.findByEmail(email), CompanyDto.class);
    }


    @Override
    public CompanyDto getCompanyDetails(int companyId) throws CouponSystemException {
        Company company = companyRepository.findById(companyId).orElseThrow(() ->
                new CouponSystemException(CompanyException.COMPANY_NOT_FOUND));
        return utils.convertToDto(company, CompanyDto.class);

    }

}

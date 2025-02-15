package com.johnbryce.coupon_system_part_2.services.company;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CompanyDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.ClientService;
import com.johnbryce.coupon_system_part_2.services.TableService;

public interface CompanyService extends ClientService, TableService<CompanyDto,Integer> {

    CompanyDto getRandomCompany() throws CouponSystemException;
    CompanyDto getCompanyDetails(int companyId) throws CouponSystemException;
    CompanyDto getSingleByEmail(String email) throws CouponSystemException;
    CompanyDto getFirstCompanyRecord() throws CouponSystemException;
    CompanyDto getLastCompanyRecord() throws CouponSystemException;
    Message isCompanyEmailExistsExclude(String email, int companyId) throws CouponSystemException;
    Message isCompanyExistsByEmailAndPassword(String email, String password) throws CouponSystemException;
    Message isCompanyExistsByName(String name) throws CouponSystemException;
    Message isCompanyExistsByEmail(String email) throws CouponSystemException;
    Message isCompanyExistsByNameAndId(String name, int id) throws CouponSystemException;
    Message isCompanyExistsByIdForController(int id) throws CouponSystemException;
    void deleteCouponByCompanyId(int companyId, int couponId) throws CouponSystemException;

}

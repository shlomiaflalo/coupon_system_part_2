package com.johnbryce.coupon_system_part_2.security;

import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.exceptions.generic.GenericException;
import com.johnbryce.coupon_system_part_2.services.Admin.AdminService;
import com.johnbryce.coupon_system_part_2.services.ClientService;
import com.johnbryce.coupon_system_part_2.services.company.CompanyService;
import com.johnbryce.coupon_system_part_2.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LoginManager {

    private final AdminService adminService;
    private final CustomerService customerService;
    private final CompanyService companyService;

    public ClientService login(String email, String password, ClientType clientType) throws CouponSystemException {
        switch (clientType) {
            case ADMINISTRATOR: {
                if (adminService.login(email, password)) {
                    return adminService;
                }
            }
            case COMPANY: {
                if (companyService.login(email, password)) {
                    return companyService;
                }
            }
            case CUSTOMER: {
                if (customerService.login(email, password)) {
                    return customerService;
                }
            }

        }
        throw new CouponSystemException(GenericException.UNKNOWN_OBJECT_TYPE);
    }
}

package com.johnbryce.coupon_system_part_2.app_messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyMessage {

    COMPANY_EXISTS_BY_EMAIL_AND_PASSWORD(2007,
            "Company exists by email and password"),
    COMPANY_NOT_EXISTS(2008,
            "Company not exists"),
    COMPANY_EXISTS(2009,
            "Company is exists"),
    COMPANY_IS_DELETED(2010,
            "Company got deleted successfully"),
    COMPANY_EXISTS_BY_NAME_AND_ID(2011,
            "Company exists by name and id"),
    COMPANY_EXISTS_BY_NAME(2012,
            "Company exists by name"),
    COMPANY_EXISTS_BY_EMAIL(2013,
            "Company exists by email"),
    COMPANY_IS_UPDATED(2014,
            "Company got updated successfully"),
    COMPANY_EMAIL_EXISTS_EXCLUDE(
            2015, "company email exists exclude");

    private final int code;
    private final String message;


}

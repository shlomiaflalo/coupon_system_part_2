package com.johnbryce.coupon_system_part_2.exceptions.generic;

import com.johnbryce.coupon_system_part_2.exceptions.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenericException implements ErrorMessage {

    EMAIL_IS_NOT_CORRECT(1026, "Email is not correct"),
    EMAIL_IS_NOT_FOUND(1027, "Email is not found"),
    EMAIL_AND_PASSWORD_IS_NOT_CORRECT(1028, "Email and password is not correct"),
    PASSWORD_IS_NOT_CORRECT(1029, "Password is not correct"),
    EMPTY_LIST(1030, "The list is empty"),
    USELESS_EDITING(1031, "Useless editing - you cannot" +
            " edit your info with same information as before"),
    UNKNOWN_OBJECT_TYPE(1032, "Unknown object type");

    private final int code;
    private final String message;


}

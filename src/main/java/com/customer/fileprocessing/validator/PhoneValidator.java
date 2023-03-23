package com.customer.fileprocessing.validator;

import com.customer.fileprocessing.utill.Utility;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public void initialize(final ValidPhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String phone, final ConstraintValidatorContext context) {
        return (validateEmail(phone));
    }

    private boolean validateEmail(final String phone) {
        if(StringUtils.isEmpty(phone))
            return false;
        return Utility.validatePhoneNumber(phone);
    }
}

package com.customer.fileprocessing.validator;

import com.customer.fileprocessing.utill.Utility;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(final ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
        return (validateEmail(email));
    }

    private boolean validateEmail(final String email) {
        if(StringUtils.isEmpty(email))
            return false;
        return Utility.validateEmail(email);
    }
}

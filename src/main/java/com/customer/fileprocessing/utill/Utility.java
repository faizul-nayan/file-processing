package com.customer.fileprocessing.utill;

import java.util.regex.Pattern;

public class Utility {
    public static final Pattern PHONE_PATTERN = Pattern.compile("^(1[ -]?)?\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$");
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    public static final String ROOT_FILE_PATH = "src/main/resources/export/";

    public static boolean validatePhoneNumber(String phoneNumber) {
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    public static boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}

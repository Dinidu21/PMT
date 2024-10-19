package com.dinidu.lk.pmt.regex;

import java.util.regex.Pattern;

public class Regex {
    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$";
    private static final String EMAIL_REGEX = "^[\\w.-]+@[\\w-]+\\.[a-zA-Z]{2,6}$";
    private static final String PHONE_NUMBER_REGEX = "^[0-9]{10}$";

    public boolean isEmailValid(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public boolean containsUpperCase(String password) {
        return Pattern.compile("[A-Z]").matcher(password).find();
    }

    public boolean containsLowerCase(String password) {
        return Pattern.compile("[a-z]").matcher(password).find();
    }

    public boolean containsDigit(String password) {
        return Pattern.compile("\\d").matcher(password).find();
    }

    public boolean isMinLength(String password) {
        return password.length() >= 8;
    }
    public boolean containsSpecialChar(String password) {
        return Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
    }

    public boolean isPhoneNumberValid(String phoneNumber) {
        return Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber);
    }
}

package com.epam.volunteer.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final String MAIL_REGEXP = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))" +
            "@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";


    private Validator() {
    }

    public static boolean checkEmail(String email) {
        if (Optional.ofNullable(email).isPresent()) {
            Pattern pattern = Pattern.compile(MAIL_REGEXP);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return false;
    }
}

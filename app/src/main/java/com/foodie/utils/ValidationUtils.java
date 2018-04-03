package com.foodie.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Developer on 9/12/2017.
 */

public class ValidationUtils {

    /**
     * Check username is valid or not
     * @param strUsername
     * @return boolean true/false
     */
    public static boolean isUsernameValid(String strUsername) {
        boolean isValid = false;

        String expression = "^[a-zA-Z][a-zA-Z0-9_]+$";
        CharSequence inputStr = strUsername;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    /**
     * Check email is valid or not
     * @param strEmail
     * @return boolean true/false
     */
    public static boolean isEmailValid(String strEmail) {
        boolean isValid = false;

        String expression = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +"\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(" +
                "\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";
        CharSequence inputStr = strEmail;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}

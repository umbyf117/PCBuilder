package com.example.socialgaming.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class StringUtils {

    // Return true if correct
    public static boolean checkEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    // Return true if correct
    public static boolean checkPassword(String password) {
        return (!TextUtils.isEmpty(password) && password.length()>=8);
    }

    // Return true if correct
    public static boolean checkUsername(String username) {
        return (Pattern.matches("^[a-zA-Z0-9]+$", username));
    }

}

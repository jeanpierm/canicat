package com.jeanpier.canicat.core;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.regex.Pattern;

public class FieldValidators {

    public static int DNI_LENGTH = 10;
    public static int PASSWORD_MIN_LENGTH = 4;

    public static boolean isValidEmail(TextInputEditText email) {
        return Patterns.EMAIL_ADDRESS.matcher(
                Objects.requireNonNull(email.getText()).toString()
        ).matches();
    }

    public static boolean isStringContainNumber(TextInputEditText text) {
        return Pattern.compile(".*\\d.*").matcher(
                Objects.requireNonNull(text.getText()).toString()
        ).matches();
    }

    public static boolean isStringFullNumbers(TextInputEditText text) {
        return Pattern.compile("^\\d+$").matcher(
                Objects.requireNonNull(text.getText()).toString()
        ).matches();
    }

    public static boolean isLengthMoreThan(TextInputEditText text, int value) {
        return text.length() > value;
    }
    public static boolean isLengthExactly(TextInputEditText text, int value) {
        return text.length() == value;
    }

    public static boolean isTextFieldEmpty(TextInputEditText edit) {
        return Objects.requireNonNull(edit.getText())
                .toString().trim().isEmpty();
    }
}

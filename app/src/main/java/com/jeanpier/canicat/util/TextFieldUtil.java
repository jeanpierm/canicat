package com.jeanpier.canicat.util;

import com.google.android.material.textfield.TextInputEditText;

public class TextFieldUtil {

    public static String getString(TextInputEditText edit) {
        if (edit == null || edit.getText() == null) return null;
        return edit.getText().toString().trim();
    }
}

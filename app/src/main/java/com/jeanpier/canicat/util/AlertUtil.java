package com.jeanpier.canicat.util;

import android.app.AlertDialog;
import android.content.Context;

import com.jeanpier.canicat.R;

public class AlertUtil {

    public static void showErrorAlertDialog(String message, String buttonLabel, Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(buttonLabel, (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}

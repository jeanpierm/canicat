package com.jeanpier.canicat.util;

import android.app.AlertDialog;
import android.content.Context;

import com.jeanpier.canicat.R;

public class AlertUtil {

    public static void showErrorAlert(String message, Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(
                context.getString(R.string.label_ok), (dialog, which) -> dialog.dismiss()
        );
        alertDialog.show();
    }

    public static void showGenericErrorAlert(Context context) {
        showErrorAlert(
                context.getString(R.string.error_contact_it),
                context
        );
    }
}

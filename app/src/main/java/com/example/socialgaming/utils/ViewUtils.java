package com.example.socialgaming.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class ViewUtils {

    public static void displaySnackbar(View view, String msg){
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        TextView snackTextView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackTextView.setMaxLines(5);
        snackbar.show();
    }

    public static void displayErrorStatus(TextInputLayout textInputLayout, String message) {
        textInputLayout.setError(message);
    }

    public static void displayToast(Context context, String message) {
        displayToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void displayToast(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // Return true if after execution is visible
    public static boolean toggleGone(View view) {
        if(view.getVisibility()!=View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
            return true;
        }
        view.setVisibility(View.GONE);
        return false;
    }

    // Return true if after execution is visible
    public static boolean toggleInvisible(View view) {
        if(view.getVisibility()!=View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
            return true;
        }
        view.setVisibility(View.INVISIBLE);
        return false;
    }
}
package com.jeuxdevelopers.wakreadmin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static long getCurrentDateWith0Time() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String getDateFromMillies(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        return dateFormat.format(calendar.getTime());
    }

    public static double get20PercentOfAmount(double amount) {

        double amountByPercent = amount - (20 * amount) / 100.0;
        return amount - amountByPercent;
    }
}

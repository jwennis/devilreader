package com.example.dreader.devilreader;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Calendar;


public class Util {

    public static final String[] SHORT_MONTH_NAMES = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    public static long getCurrentTimestamp() {

        return Calendar.getInstance().getTimeInMillis();
    }

    public static void setTitle(Activity activity, int resId) {

        ((AppCompatActivity) activity).getSupportActionBar().setTitle(resId);
    }

    public static void isUiThread() {

        Log.v("DREADER", Looper.myLooper() == Looper.getMainLooper() ? "UI THREAD" : "BG THREAD");
    }

    /**
     * Formats an integer value to represent currency
     *
     * @param number dollars
     * @return formatted String
     */
    public static String format$(int number) {

        return "$" + String.format("%,d", number);
    }


    /**
     * Formats an integer value with appropriate suffix
     * Example: 1 -> "1st", 2 -> "2nd", etc...
     *
     * @param number the number to be formatted
     * @return formatted String
     */
    public static String formatNumSuffix(int number) {

        String[] suffix = { "th", "st", "nd", "rd" };
        int mod = number % 10;

        if (mod > 3 || (number % 100 >= 11 && number % 100 <= 13)) {

            mod = 0;
        }

        return number + suffix[mod];
    }


    public static SharedPreferences getPreferences(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getOpenExternalLinkMethod(Context context) {

        String prefkey = context.getString(R.string.pref_general_open_link_method);

        return getPreferences(context).getString(prefkey, "0");
    }

    public static String[] getPrefsSourceKeys() {

        return new String[] { "pref_sources_nhl", "pref_sources_tsn", "pref_sources_fib",
                "pref_sources_yt", "pref_sources_ppf", "pref_sources_had" };
    }
}

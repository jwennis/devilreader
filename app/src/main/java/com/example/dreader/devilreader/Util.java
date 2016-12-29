package com.example.dreader.devilreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;


public class Util {

    public static final String[] SHORT_MONTH_NAMES = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    public static long getCurrentTimestamp() {

        return Calendar.getInstance().getTimeInMillis();
    }

    public static SharedPreferences getPreferences(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getOpenExternalLinkMethod(Context context) {

        String prefkey = context.getString(R.string.pref_general_open_link_method);

        return getPreferences(context).getString(prefkey, "0");
    }
}

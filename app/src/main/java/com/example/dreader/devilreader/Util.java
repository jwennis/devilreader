package com.example.dreader.devilreader;

import java.util.Calendar;


public class Util {

    public static final String[] SHORT_MONTH_NAMES = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    public static long getCurrentTimestamp() {

        return Calendar.getInstance().getTimeInMillis();
    }
}

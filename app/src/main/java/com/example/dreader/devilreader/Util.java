package com.example.dreader.devilreader;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;


public class Util {

    public static final String[] DAY_OF_WEEK_NAMES = new String[] {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

    public static final String[] SHORT_MONTH_NAMES = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    public static final String[] LONG_MONTH_NAMES = new String[] {
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December" };


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


    public static Class getPreferredStartScreen(Context context) {

        Class[] which = new Class[] { DiscoverFragment.class, NewsFragment.class };

        String prefValue = getPreferences(context).getString("pref_general_start_screen", "0");

        return which[ Integer.parseInt(prefValue) ];
    }


    public static String getOpenExternalLinkMethod(Context context) {

        String prefkey = context.getString(R.string.pref_general_open_link_method);

        return getPreferences(context).getString(prefkey, "0");
    }


    public static String[] getPrefsSourceKeys() {

        return new String[] { "pref_sources_nhl", "pref_sources_tsn", "pref_sources_fib",
                "pref_sources_yt", "pref_sources_ppf", "pref_sources_had" };
    }


    /**
     * Gets the icon for a given team
     *
     * @param team the team whose icon to retrieve
     * @return the drawable resource ID
     */
    public static int getTeamIcon(String team) {

        HashMap<String, Integer> icons = new HashMap<>();
        icons.put("ANA", R.drawable.ic_team_ana);
        icons.put("ARI", R.drawable.ic_team_ari);
        icons.put("BOS", R.drawable.ic_team_bos);
        icons.put("BUF", R.drawable.ic_team_buf);
        icons.put("CAR", R.drawable.ic_team_car);
        icons.put("CBJ", R.drawable.ic_team_cbj);
        icons.put("CGY", R.drawable.ic_team_cgy);
        icons.put("CHI", R.drawable.ic_team_chi);
        icons.put("COL", R.drawable.ic_team_col);
        icons.put("DAL", R.drawable.ic_team_dal);
        icons.put("DET", R.drawable.ic_team_det);
        icons.put("EDM", R.drawable.ic_team_edm);
        icons.put("FLA", R.drawable.ic_team_fla);
        icons.put("LAK", R.drawable.ic_team_lak);
        icons.put("MIN", R.drawable.ic_team_min);
        icons.put("MTL", R.drawable.ic_team_mtl);
        icons.put("NJD", R.drawable.ic_team_njd);
        icons.put("NSH", R.drawable.ic_team_nsh);
        icons.put("NYI", R.drawable.ic_team_nyi);
        icons.put("NYR", R.drawable.ic_team_nyr);
        icons.put("OTT", R.drawable.ic_team_ott);
        icons.put("PHI", R.drawable.ic_team_phi);
        icons.put("PIT", R.drawable.ic_team_pit);
        icons.put("SJS", R.drawable.ic_team_sjs);
        icons.put("STL", R.drawable.ic_team_stl);
        icons.put("TBL", R.drawable.ic_team_tbl);
        icons.put("TOR", R.drawable.ic_team_tor);
        icons.put("VAN", R.drawable.ic_team_van);
        icons.put("WPG", R.drawable.ic_team_wpg);
        icons.put("WSH", R.drawable.ic_team_wsh);

        return icons.get(team);
    }


    /**
     * Gets the full name for a given team
     *
     * @param team the team whose full name to retrieve
     * @return the full name
     */
    public static String getTeamName(String team) {

        HashMap<String, String> teams = new HashMap<>();
        teams.put("ANA", "Anaheim Ducks");
        teams.put("ARI", "Arizona Coyotes");
        teams.put("BOS", "Boston Bruins");
        teams.put("BUF", "Buffalo Sabres");
        teams.put("CAR", "Carolina Hurricanes");
        teams.put("CBJ", "Columbus Blue Jackets");
        teams.put("CGY", "Calgary Flames");
        teams.put("CHI", "Chicago Blackhawks");
        teams.put("COL", "Colorado Avalanche");
        teams.put("DAL", "Dallas Stars");
        teams.put("DET", "Detroit Red Wings");
        teams.put("EDM", "Edmonton Oilers");
        teams.put("FLA", "Florida Panthers");
        teams.put("LAK", "Los Angeles Kings");
        teams.put("MIN", "Minnesota Wild");
        teams.put("MTL", "Montreal Canadiens");
        teams.put("NJD", "New Jersey Devils");
        teams.put("NSH", "Nashville Predators");
        teams.put("NYI", "New York Islanders");
        teams.put("NYR", "New York Rangers");
        teams.put("OTT", "Ottawa Senators");
        teams.put("PHI", "Philadelphia Flyers");
        teams.put("PIT", "Pittsburgh Penguins");
        teams.put("SJS", "San Jose Sharks");
        teams.put("STL", "St. Louis Blues");
        teams.put("TBL", "Tamp Bay Lightning");
        teams.put("TOR", "Toronto Maple Leafs");
        teams.put("VAN", "Vancouver Canucks");
        teams.put("WPG", "Winnipeg Jets");
        teams.put("WSH", "Washington Capitals");

        return teams.get(team);
    }


    public static String getTeamCity(String team) {

        HashMap<String, String> cities = new HashMap<>();
        cities.put("ANA", "Anaheim, California");
        cities.put("ARI", "Glendale, Arizona");
        cities.put("BOS", "Boston, Massachusetts");
        cities.put("BUF", "Buffalo, New York");
        cities.put("CAR", "Raleigh, North Carolina");
        cities.put("CBJ", "Columbus, Ohio");
        cities.put("CGY", "Calgary, Alberta");
        cities.put("CHI", "Chicago, Illinois");
        cities.put("COL", "Denver, Colorado");
        cities.put("DAL", "Dallas, Texas");
        cities.put("DET", "Detroit, Michigan");
        cities.put("EDM", "Detroit, Mishigan");
        cities.put("FLA", "Sunrise, Florida");
        cities.put("LAK", "Los Angeles, California");
        cities.put("MIN", "Saint Paul, Minnesota");
        cities.put("MTL", "Montreal, Quebec");
        cities.put("NJD", "Newark, New Jersey");
        cities.put("NSH", "Nashville, Tennessee");
        cities.put("NYI", "Brooklyn, New York");
        cities.put("NYR", "New York City, New York");
        cities.put("OTT", "Ottawa, Ontario");
        cities.put("PHI", "Philadelphia, Pennsylvania");
        cities.put("PIT", "Pittsburgh, Pennsylvania");
        cities.put("SJS", "San Jose, California");
        cities.put("STL", "St. Louis, Missouri");
        cities.put("TBL", "Tampa, Florida");
        cities.put("TOR", "Toronto, Ontario");
        cities.put("VAN", "Vancouver, British Columbia");
        cities.put("WPG", "Winnipeg, Manitoba");
        cities.put("WSH", "Washington, D.C.");

        return cities.get(team);
    }


    public static String getTeamArena(String team) {

        HashMap<String, String> arenas = new HashMap<>();
        arenas.put("ANA", "Honda Center");
        arenas.put("ARI", "Gila River Arena");
        arenas.put("BOS", "TD Garden");
        arenas.put("BUF", "KeyBank Center");
        arenas.put("CAR", "PNC Arena");
        arenas.put("CBJ", "Nationwide Arena");
        arenas.put("CGY", "Scotiabank Saddledome");
        arenas.put("CHI", "United Center");
        arenas.put("COL", "Pepsi Center");
        arenas.put("DAL", "American Airlines Center");
        arenas.put("DET", "Joe Louis Arena");
        arenas.put("EDM", "Rogers Place");
        arenas.put("FLA", "BB&T Center");
        arenas.put("LAK", "Staples Center");
        arenas.put("MIN", "Xcel Energy Center");
        arenas.put("MTL", "Bell Center");
        arenas.put("NJD", "Prudential Center");
        arenas.put("NSH", "Bridgestone Arena");
        arenas.put("NYI", "Barclays Center");
        arenas.put("NYR", "Madison Square Garden");
        arenas.put("OTT", "Canadian Tire Center");
        arenas.put("PHI", "Wells Fargo Center");
        arenas.put("PIT", "PPG Paints Arena");
        arenas.put("SJS", "SAP Center");
        arenas.put("STL", "Scottrade Center");
        arenas.put("TBL", "Amalie Arena");
        arenas.put("TOR", "Air Canada Center");
        arenas.put("VAN", "Rogers Arena");
        arenas.put("WPG", "MTS Centre");
        arenas.put("WSH", "Verizon Center");

        return arenas.get(team);
    }
}

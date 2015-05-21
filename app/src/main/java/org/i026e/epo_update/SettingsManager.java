package org.i026e.epo_update;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by pavel on 20.05.15.
 *
 * wrapper class to dealing with specific application preferences
 * from  different places
 */

public class SettingsManager {
    public static final int DAILY = 1;
    public static final int WEEKLY = 7;
    public static final int MONTHLY = 30;

    private static final String PREFERENCES_FILE_NAME = "org.i026e.epo_update.preferences";
    private static final String DATE = "date";
    private static final String UPDATE_PERIOD = "update_period";
    private static final String UPDATE_ENABLED = "update_enabled";
    private static final String WIFI_ONLY = "wifi_only";


    private static final int DEFAULT_UPDATE_PERIOD = WEEKLY;
    private static final boolean DEFAULT_UPDATE_ENABLED = false;
    private static final boolean DEFAULT_WIFI_ONLY = true;


    SharedPreferences sharedPref;

    public SettingsManager(Context context){
        this.sharedPref = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setUpdatePeriod(int days){
        sharedPref.edit().putInt(UPDATE_PERIOD, days).commit();
    }

    public int getUpdatePeriod(){
        return sharedPref.getInt(UPDATE_PERIOD, DEFAULT_UPDATE_PERIOD);
    }

    public void setLastUpdate(Date date){
        long millis = date.getTime();
        sharedPref.edit().putLong(DATE, millis).commit();
    }

    public Date getLastUpdate(){
        return new Date(sharedPref.getLong(DATE, 0));
    }

    public void setAutoUpdateEnabled(boolean autoUpdateEnabled){
        sharedPref.edit().putBoolean(UPDATE_ENABLED, autoUpdateEnabled).commit();
    }
    public boolean getAutoUpdateEnabled(){
        return sharedPref.getBoolean(UPDATE_ENABLED, DEFAULT_UPDATE_ENABLED);
    }

    public void setWifiOnly(boolean wifiOnly){
        sharedPref.edit().putBoolean(WIFI_ONLY, wifiOnly).commit();
    }
    public boolean getWifiOnly(){
        return sharedPref.getBoolean(WIFI_ONLY, DEFAULT_WIFI_ONLY);
    }


    private static SharedPreferences getSharedPrefManager(Context context){
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }
    public static void setUpdatePeriod(Context context, int days){
        getSharedPrefManager(context).edit().putInt(UPDATE_PERIOD, days).commit();
    }

    public static int getUpdatePeriod(Context context){
        return getSharedPrefManager(context).getInt(UPDATE_PERIOD, DEFAULT_UPDATE_PERIOD);
    }

    public static void setLastUpdate(Context context, Date date){
        long millis = date.getTime();
        getSharedPrefManager(context).edit().putLong(DATE, millis).commit();
    }

    public static Date getLastUpdate(Context context){
        return new Date(getSharedPrefManager(context).getLong(DATE, 0));
    }

    public static void setAutoUpdateEnabled(Context context, boolean autoUpdateEnabled){
        getSharedPrefManager(context).edit().putBoolean(UPDATE_ENABLED, autoUpdateEnabled).commit();
    }
    public static boolean getAutoUpdateEnabled(Context context){
        return getSharedPrefManager(context).getBoolean(UPDATE_ENABLED, DEFAULT_UPDATE_ENABLED);
    }

    public static void setWifiOnly(Context context, boolean wifiOnly){
        getSharedPrefManager(context).edit().putBoolean(WIFI_ONLY, wifiOnly).commit();
    }
    public static boolean getWifiOnly(Context context){
        return getSharedPrefManager(context).getBoolean(WIFI_ONLY, DEFAULT_WIFI_ONLY);
    }

}

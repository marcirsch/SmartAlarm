package com.example.smartalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;

public class PreferencesHelper {
    public static final String SHARED_PREFERENCES = "smartAlarmPefs";
    public static final String ALARM_TIME_HOURS = "alarmTimeHours";
    public static final String ALARM_TIME_MINUTES = "alarmTimeMinutes";
    public static final String ALARM_SWITCH = "alarmSwitch";
    public static final String ALARM_COFFEE_CHECKBOX = "alarmCoffeeCheckBox";

    private Context context;

    public static class Time{
        public int hours;
        public int minutes;
        Time(){hours=0;minutes=0;}
    }



    public static void put(Context context, String key, Object object) {

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES,  Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.apply();
    }

    public static Object get(Context context, String key, Object object){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES,  Context.MODE_PRIVATE);

        if (object instanceof String) {
            return preferences.getString(key, (String) object);
        } else if (object instanceof Integer) {
            return preferences.getInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            return preferences.getBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            return preferences.getFloat(key, (Float) object);
        } else if (object instanceof Long) {
            return preferences.getLong(key, (Long) object);
        } else {
            return preferences.getString(key, object.toString());
        }
    }

    public static void saveAlarmSwitch(Context context, Boolean state){
        put(context, ALARM_SWITCH, state);
    }

    public static boolean getAlarmSwitch(Context context){
        return (Boolean)get(context, ALARM_SWITCH, false);
    }

    public static void saveAlarmTime(Context context, int hours, int minutes){
        put(context, ALARM_TIME_HOURS, hours);
        put(context, ALARM_TIME_MINUTES, minutes);
    }

    public static Time getAlarmTime(Context context){
        Time time = new Time();
        time.hours = (Integer) get(context, ALARM_TIME_HOURS, time.hours);
        time.minutes = (int)get(context, ALARM_TIME_MINUTES, time.minutes);
        return time;
    }

    public static void saveAlarmCoffeeSwitch(Context context, Boolean state){
        put(context, ALARM_COFFEE_CHECKBOX, state);
    }

    public static boolean getAlarmCoffeeSwitch(Context context){
        return (boolean)get(context, ALARM_COFFEE_CHECKBOX, false);
    }




}

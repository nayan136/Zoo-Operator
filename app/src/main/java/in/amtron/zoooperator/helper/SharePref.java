package in.amtron.zoooperator.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {

    private static SharePref sharePref = new SharePref();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    public static final String DATA = "data";
    public static final String USER = "user";
    public static final String CURRENT_MODE = "current_mode";
    public static final String BOOKING = "booking";
    public static final String BOOKING_ID = "booking_id";

    private SharePref() {} //prevent creating multiple instances by making the constructor private

    //The context passed into the getInstance should be application level context.
    public static SharePref getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return sharePref;
    }

    public void put(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void put(String key, Long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public String get(String key) {
        return sharedPreferences.getString(key, "");
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key,0);
    }

    public Long getLong(String key) {
        return sharedPreferences.getLong(key,0);
    }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }

}

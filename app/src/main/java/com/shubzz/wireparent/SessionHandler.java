package com.shubzz.wireparent;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMPTY = "";
    private static final String KEY1 = "KEY1";
    private static final String KEY2 = "KEY2";
    private static final String KEY3 = "KEY3";
    private static final String KEY4 = "KEY4";
    private static final String KEY_Longitude = "Longitude";
    private static final String KEY_Latitude = "Latitude";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param username
     * @param fullName
     */

    public void loginUser(String username, String fullName, String uq_key1, String uq_key2, String uq_key3, String uq_key4) {
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_FULL_NAME, fullName);
        mEditor.putString(KEY1, uq_key1);
        mEditor.putString(KEY2, uq_key2);
        mEditor.putString(KEY3, uq_key3);
        mEditor.putString(KEY4, uq_key4);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */

    String[] getKey() {
        String[] key = new String[4];
        key[0] = mPreferences.getString(KEY1, KEY_EMPTY);
        key[1] = mPreferences.getString(KEY2, KEY_EMPTY);
        key[2] = mPreferences.getString(KEY3, KEY_EMPTY);
        key[3] = mPreferences.getString(KEY4, KEY_EMPTY);
        return key;
    }

    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setFullName(mPreferences.getString(KEY_FULL_NAME, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }

    public String[] getDetails(int loc) {
        String KEY= null;
        switch (loc){
            case 1:
                KEY = mPreferences.getString(KEY1,KEY_EMPTY);
                Log.d("request",KEY);
                break;
            case 2:
                KEY = mPreferences.getString(KEY2,KEY_EMPTY);
                break;
            case 3:
                KEY = mPreferences.getString(KEY3,KEY_EMPTY);
                break;
            case 4:
                KEY = mPreferences.getString(KEY4,KEY_EMPTY);
                break;
        }
        String[] arr = {mPreferences.getString(KEY_FULL_NAME,KEY_EMPTY),mPreferences.getString(KEY_USERNAME,KEY_EMPTY)
                ,KEY};
        return arr;
    }

    public void setLocation(String longitude, String latitude) {
        Log.d("requestL",longitude);
        mEditor.putString(KEY_Latitude, latitude);
        mEditor.putString(KEY_Longitude, longitude);
    }

    public String getLatitude() {
        Log.d("request",KEY_Latitude);
        return mPreferences.getString(KEY_Latitude,KEY_EMPTY);
    }
    public String getLongitude() {
        return mPreferences.getString(KEY_Longitude,KEY_EMPTY);
    }
}

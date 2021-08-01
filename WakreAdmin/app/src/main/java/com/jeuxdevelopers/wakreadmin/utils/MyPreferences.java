package com.jeuxdevelopers.wakreadmin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.jeuxdevelopers.wakreadmin.models.AdminModel;

public class MyPreferences {

    private static final String KEY_CURRENT_USER = "current-user";
    private static final String KEY_UID = "user-id";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static String PREF_NAME = "Wakre-admin";

    private static final String KEY_TYPE = "type";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_CITY = "KEY_CITY";


    public MyPreferences(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = preferences.edit();
    }

    public void saveCurrentUser(AdminModel userModel) {
        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        editor.putString(KEY_CURRENT_USER, json);
        try {
            editor.putString(KEY_UID, userModel.getId());
        } catch (Exception e) {

        }
        editor.commit();
    }

    public AdminModel getCurrentUser() {
        Gson gson = new Gson();
        String json = preferences.getString(KEY_CURRENT_USER, null);
        return gson.fromJson(json, AdminModel.class);
    }

    public String getUId() {
        return preferences.getString(KEY_UID, "");
    }


    public String getLanguage() {
        String language = preferences.getString(KEY_LANGUAGE, "en");
        return language;
    }

    public void setLanguage(String language) {
        editor.putString(KEY_LANGUAGE, language);
        editor.commit();
    }

}

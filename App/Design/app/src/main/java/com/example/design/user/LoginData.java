package com.example.design.user;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class LoginData {
    private String username;
    private String password;

    public LoginData(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public static void saveUser(Context context, LoginData loginData){ //store username and password on the device. Only one loginData can be stored.
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("username", loginData.getUsername());
        editor.putString("password", loginData.getPassword());
        editor.apply(); //save changes
    }

    public static LoginData loadUser(Context context){ //load the previously saved user data
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password","");
        return new LoginData(username, password);
    }
}

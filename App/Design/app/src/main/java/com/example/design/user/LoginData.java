package com.example.design.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.example.design.Home;
import com.example.design.LoginPage;

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

    public static void logout(Context context) {
        saveUser(context, new LoginData("", ""));
        UserToken.currentUser = new UserToken(-1, "");

        Intent intent = new Intent(context, LoginPage.class);
        //prevents the user from returning to the previous activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent); //open new page
    }
}

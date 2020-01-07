package com.example.design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.design.user.AccountManager;
import com.example.design.user.LoginData;
import com.example.design.user.UserToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends AppCompatActivity {
    private AccountManager manager;
    static String token;
    static int userID;
    static String username;
    static String password;
    static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        manager = new AccountManager(this);

        final EditText usernameField = findViewById(R.id.usernameField);
        final EditText passwordField = findViewById(R.id.passwordField);
        final TextView loginErrorText = findViewById(R.id.loginErrorText);
        final Context context = this; //for the onresponse function

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), register.class);
                startActivity(intent);}
        });

        tryAutoLogin();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the username and password from the fields
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();


                //run a login request
                manager.login(username, password, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        //default values of the user (in case of a failed login)
                        token = "";
                        userID = -1;
                        try{
                            JSONObject resObject = new JSONArray(response.toString()).getJSONObject(0);

                            if(resObject.has("auth_token")){ //check if it has a token field
                                token = resObject.getString("auth_token");
                                userID = resObject.getInt("id");
                                email = resObject.getString("email");
                                UserToken.currentUser = new UserToken(userID, token); //create a new token object and set it as current
                                //store the username and password
                                LoginData.saveUser(context, new LoginData(username, password));
                                startActivity(new Intent(LoginPage.this, Home.class)); //open new page
                            }
                            else{
                                loginErrorText.setVisibility(View.VISIBLE); //show the login error
                                UserToken.currentUser = new UserToken(userID, token);
                            }
                        }
                        catch (JSONException e){
                            Log.e("json","error: " + e.toString());
                            loginErrorText.setVisibility(View.VISIBLE); //show the login error
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login", error.toString());
                        loginErrorText.setVisibility(View.VISIBLE); //show the login error
                    }
                });
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        final TextView loginErrorText = findViewById(R.id.loginErrorText);
        loginErrorText.setVisibility(View.INVISIBLE); //hide tbe text
    }

    public void tryAutoLogin(){
        LoginData loginData = LoginData.loadUser(this); //load the previously stored username and password

        if(loginData.getUsername() != ""){
            manager.login(loginData.getUsername(), loginData.getPassword(), new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try{
                        JSONObject resObject = new JSONArray(response.toString()).getJSONObject(0);

                        if(resObject.has("auth_token")){ //check if it has a token field
                            String token = resObject.getString("auth_token");
                            int userId = resObject.getInt("id");

                            UserToken.currentUser = new UserToken(userId, token); //create a new token object and set it as current
                            startActivity(new Intent(LoginPage.this, Home.class)); //open new page
                        }
                    }
                    catch (JSONException e){
                        Log.e("json","error: " + e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("auto login", error.toString());
                }
            });
        }
    }
}

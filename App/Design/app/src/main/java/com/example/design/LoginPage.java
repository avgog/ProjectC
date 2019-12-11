package com.example.design;

import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.design.R;
import com.example.design.routes.Route;
import com.example.design.user.AccountManager;
import com.example.design.user.UserToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends AppCompatActivity {
    private AccountManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        manager = new AccountManager(this);

        final EditText usernameField = findViewById(R.id.usernameField);
        final EditText passwordField = findViewById(R.id.passwordField);
        final TextView loginErrorText = findViewById(R.id.loginErrorText);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the username and password from the fields
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                //run a login request
                manager.login(username, password, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        //default values of the user (in case of a failed login)
                        String token = "";
                        int userId = -1;
                        try{
                            JSONObject resObject = new JSONArray(response.toString()).getJSONObject(0);

                            if(resObject.has("auth_token")){ //check if it has a token field
                                token = resObject.getString("auth_token");
                                userId = resObject.getInt("id");

                                UserToken.currentUser = new UserToken(userId, token); //create a new token object and set it as current
                                startActivity(new Intent(LoginPage.this, Home.class)); //open new page
                            }
                            else{
                                loginErrorText.setVisibility(View.VISIBLE); //show the login error
                                UserToken.currentUser = new UserToken(userId, token);
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
}

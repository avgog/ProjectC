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

import com.example.design.R;
import com.example.design.routes.Route;

public class LoginPage extends AppCompatActivity {

    private final String correctUsername = "user1";
    private final String correctPassword = "geheim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        final EditText usernameField = findViewById(R.id.usernameField);
        final EditText passwordField = findViewById(R.id.passwordField);
        final TextView loginErrorText = findViewById(R.id.loginErrorText);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), register.class);
                startActivity(intent);}
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                if(username.equals(correctUsername) && password.equals(correctPassword)){
                    Log.i("login","User logged in");

                    //user is logged in
                    loginErrorText.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(v.getContext(), Home.class);
                    startActivity(intent);
                }
                else{
                    //login failed
                    loginErrorText.setVisibility(View.VISIBLE);
                    Log.i("login","Incorrect username/password");
                }
            }
        });
    }
}

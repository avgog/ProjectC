package com.example.design;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.design.user.LoginData;
import com.example.design.user.UserToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Settings extends AppCompatActivity implements DialogPassword.ExampleDialogListener, DialogUsername.ExampleDialogListener {

    static String language;
    static String shouldRestart;
    static String username;
    TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        TextView emailText = findViewById(R.id.emailtext);
        usernameText = findViewById(R.id.usernametext);
        emailText.setText(LoginPage.email);

        String username = LoginData.loadUser(this).getUsername();
        usernameText.setText(username);
        Button closeButton = findViewById(R.id.CloseButton);
        Button changeUsernameButton = findViewById(R.id.changeusernamebutton);
        Button changePasswordButton = findViewById(R.id.changepasswordbutton);
        ToggleButton changeLanguageButton = findViewById(R.id.changelangbutton);
        //ToggleButton themeButton = findViewById(R.id.ThemeButton);
        if(language=="nl"){
        changeLanguageButton.setChecked(true); } else {
            changeLanguageButton.setChecked(false);
        }
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Changes the app language
        changeLanguageButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    setAppLocale("en");
                    Settings.language="en";
                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);

                    Settings.shouldRestart = "yes";

                } else {
                    setAppLocale("nl");
                    Settings.language="nl";
                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);

                    Settings.shouldRestart = "yes";
                }

            }
        });
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPDialog();

            }
        });
        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUDialog();
            }
        });


    }
    //Dialog for changing password
    private void openPDialog() {
        DialogPassword Dialog = new DialogPassword();
        Dialog.show(getSupportFragmentManager(), "dialog");

    }
    //Dialog for changing username
    private void openUDialog(){
        DialogUsername Dialog = new DialogUsername();
        Dialog.show(getSupportFragmentManager(), "dialog");
    }

    //Creates http requests to the API. In the parameters variables can be passed if the variable is null it will be ignored and not passed on to the api.
    public void jsonParse(Context context, final String url, final String username, final String password, final boolean auth) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("okidoki",response);
                try {
                    JSONObject json = new JSONObject(response);
                        String auth_token = json.getString("new_token");
                        UserToken.currentUser = new UserToken(UserToken.currentUser.getUserId(), auth_token);
                        LoginData data = LoginData.loadUser(Settings.this);
                        data = new LoginData(username,data.getPassword());
                        LoginData.saveUser(Settings.this,data);

                } catch (JSONException e) {
                    Toast.makeText(Settings.this, "Please choose something else", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("okidoki",error.toString());
            }
        }){
            //Maps the parameters so they can be sent off to the API
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                Log.i("okidoki",UserToken.currentUser.getToken());
                params.put("auth_token", UserToken.currentUser.getToken());
                params.put("user_id", String.valueOf(UserToken.currentUser.getUserId()));
                if(!(username==null)){
                    params.put("username",username);
                }
                if(!(password==null)) {
                    params.put("password",password);
                }
                return params;
            }



        };
        queue.add(sr);
    }

    @Override
    public void applyTextsPassword(String password){
        jsonParse(this,"http://projectc.caslayoort.nl/public/auth/change/password",null,password,false);
    }

    @Override
    public void applyTextsUsername(String username){
        jsonParse(this,"http://projectc.caslayoort.nl/public/auth/change/username",username,null,false);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        this.overridePendingTransition(0, 0);
    }


    public void setAppLocale(String appLocale){
        Locale locale = new Locale(appLocale);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

    }

}

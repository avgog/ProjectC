package com.example.design;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Settings extends AppCompatActivity implements DialogPassword.ExampleDialogListener, DialogUsername.ExampleDialogListener {

    static String language;
    static String shouldRestart;
    static String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Button closeButton = findViewById(R.id.CloseButton);
        Button changeUsernameButton = findViewById(R.id.changeusernamebutton);
        Button changePasswordButton = findViewById(R.id.changepasswordbutton);
        ToggleButton changeLanguageButton = findViewById(R.id.changelangbutton);
        ToggleButton themeButton = findViewById(R.id.ThemeButton);
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
        changeLanguageButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    setAppLocale("en");
                    Settings.language="en";
                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    Toast.makeText(Settings.this, "ENGELS", Toast.LENGTH_SHORT).show();
                    Settings.shouldRestart = "yes";

                } else {
                    setAppLocale("nl");
                    Settings.language="nl";
                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    Toast.makeText(Settings.this, "NIEDERLANDISCH", Toast.LENGTH_SHORT).show();
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

    private void openPDialog() {
        DialogPassword Dialog = new DialogPassword();
        Dialog.show(getSupportFragmentManager(), "dialog");

    }
    private void openUDialog(){
        DialogUsername Dialog = new DialogUsername();
        Dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void jsonParse(Context context,final String url, final String username, final String password) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("okidoki",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("okidoki",error.toString());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("auth_token","081848afca7affee3e760bd18b80bf51ef1f1133ae70dbd5e26e64f553c33779");
                params.put("id","6");
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
        jsonParse(this,"http://projectc.caslayoort.nl/public/auth/change/password",null,password);
    }

    @Override
    public void applyTextsUsername(String username){
        jsonParse(this,"http://projectc.caslayoort.nl/public/auth/change/password",username,null);
    }


    public void setAppLocale(String appLocale){
        Locale locale = new Locale(appLocale);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

    }

}

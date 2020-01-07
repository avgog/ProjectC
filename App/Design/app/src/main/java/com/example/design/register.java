package com.example.design;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    int view = R.layout.activity_register;
    Button button;
    EditText emailId;
    EditText passwordId;
    EditText usernameId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[$@$!%*#?&])[A-Za-z\\\\d$@$!%*#?&]{8,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        button = findViewById(R.id.registerButton);
        emailId = findViewById(R.id.setemailField);
        usernameId = findViewById(R.id.setusernameField);
        passwordId = findViewById(R.id.setpasswordField);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailId.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"enter email address",Toast.LENGTH_SHORT).show();
                }else {
                    if (!emailId.getText().toString().trim().matches(emailPattern)) {
                        Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }
                if(passwordId.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"enter password",Toast.LENGTH_SHORT).show();
                }else {
                    if ((passwordId.getText()== null)) {
                        Toast.makeText(getApplicationContext(),"Invalid password", Toast.LENGTH_SHORT).show();
                    }
                }

                String myurl = "http://projectc.caslayoort.nl/public/auth/register";
                jsonParse(register.this,myurl);


            }
        });
    }

    public void jsonParse(Context context,final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray array = json.getJSONArray("result");
                    Log.i("okidoki", array.toString());
                    for (int i=0; i <array.length();i++) {
                        JSONObject respJson = array.getJSONObject(i);
                        String error = (respJson.getString("error"));
                        String user_id = (respJson.getString("user_id"));
                        String auth_token = (respJson.getString("auth_token"));

                    String[] jsonArray = new String[3];
                    jsonArray[0] = error;
                    jsonArray[1] = user_id;
                    jsonArray[2] = auth_token;

                    System.out.println(jsonArray);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                params.put("username",usernameId.getText().toString());
                params.put("password",passwordId.getText().toString());
                params.put("email",emailId.getText().toString());

                return params;
            }



        };
        queue.add(sr);
    }
}

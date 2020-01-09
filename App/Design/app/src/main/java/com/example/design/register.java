package com.example.design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.design.user.LoginData;
import com.example.design.user.UserToken;

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
                String username = usernameId.getText().toString();
                String email = emailId.getText().toString();
                String password = passwordId.getText().toString();

                if(username.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter username",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(email.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if (!email.trim().matches(emailPattern)) {
                        Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(password.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"enter password",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if ((password.trim().matches(passwordPattern))) {
                        Toast.makeText(getApplicationContext(),"Invalid password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(),"Registering...",Toast.LENGTH_SHORT).show();
                Log.i("register", String.format("Registering user... [username: %s, email: %s]", username,password));
                String myurl = "http://projectc.caslayoort.nl/public/auth/register";
                jsonParse(register.this,myurl);


            }
        });
    }

    public void jsonParse(final Context context, final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("register", "response: " + response);

                try {
                    JSONObject json = new JSONObject(response);
                    if(json.has("ERROR")){
                        String error = json.getString("ERROR");
                        Log.e("register", "ERROR: " + error);
                        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) { //json string doesn't contain an exception
                    try{
                        JSONArray array = new JSONArray(response);
                        Log.i("register", array.toString());
                        //for (int i=0; i <array.length();i++) {
                        if(array.length()>0){
                            JSONObject respJson = array.getJSONObject(0);
                            String user_id = (respJson.getString("id"));
                            String auth_token = (respJson.getString("auth_token"));

                            UserToken.currentUser = new UserToken(Integer.parseInt(user_id), auth_token);
                            LoginData.saveUser(context, new LoginData(usernameId.getText().toString(), passwordId.getText().toString()));
                            LoginPage.email = emailId.getText().toString();

                            startActivity(new Intent(register.this, Home.class)); //open new page
                        }
                        else{
                            Log.e("register","array is empty");
                            Toast.makeText(getApplicationContext(),"Something went wrong with registering, try again later.",Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException e2){
                        e2.printStackTrace();
                        Log.e("register", "json error: " + e2.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("register",error.toString());

                if(error.toString().contains("com.android.volley.NoConnectionError")){
                    Log.e("register","No connection");
                    Toast.makeText(getApplicationContext(),"Unable to connect to the server",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Something went wrong with registering, try again later.",Toast.LENGTH_LONG).show();
                }
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

package com.example.design.routes;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RouteManager {
    public static RequestQueue queue;

    public static String serverURL = "http://192.168.178.29:2900";

    public RouteManager(Context context){
        queue = Volley.newRequestQueue(context);
    }

    private void getRoutesByUserId(int id, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("user_id","1");
        PostRequest request = new PostRequest(serverURL + "/routes/get/from_user", params, listener, errorListener);
        queue.add(request);
    }


}

package com.example.design.routes;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PostRequest extends StringRequest {
    private Map<String,String> postParameters;

    public PostRequest(String url, Map<String,String> postParameters, Response.Listener listener, Response.ErrorListener errorListener){
        super(Method.POST, url, listener, errorListener);
    }

    @Override
    protected Map<String,String> getParams(){
        return postParameters;
    }
}

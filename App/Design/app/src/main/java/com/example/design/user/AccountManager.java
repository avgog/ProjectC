package com.example.design.user;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.design.R;
import com.example.design.request.PostRequest;

import java.util.HashMap;
import java.util.Map;

//is responsible for handling user requests
public class AccountManager {
    private RequestQueue queue;
    private String serverURL;

    public AccountManager(Context context){
        queue = Volley.newRequestQueue(context);
        serverURL = context.getResources().getString(R.string.server_url);
    }

    //Login with username + password. Use this function to get a token for using other api functions.
    public void login(String username, String password, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        PostRequest request = new PostRequest(serverURL + "/public/auth", params, listener, errorListener);
        queue.add(request);
    }

    public void register(String username, String password, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        PostRequest request = new PostRequest(serverURL + "/public/user/register", params, listener, errorListener);
        queue.add(request);
    }

    //changes the username and password of the user.
    //after executing the request, run the login function to get a new token
    public void changeUserData(String username, String password, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("token", UserToken.currentUser.getToken());
        params.put("user_id",  String.valueOf(UserToken.currentUser.getUserId()));
        params.put("username", username);
        params.put("password", password);
        PostRequest request = new PostRequest(serverURL + "/public/user/change", params, listener, errorListener);
        queue.add(request);
    }

    public void checkUsername(String username, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("username", username);
        PostRequest request = new PostRequest(serverURL + "/public/user/exists", params, listener, errorListener);
        queue.add(request);
    }
}

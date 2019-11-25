package com.example.design.routes;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.design.request.PostRequest;

import java.util.HashMap;
import java.util.Map;

public class RouteManager {
    public static RequestQueue queue;

    public static String serverURL = "http://projectc.caslayoort.nl:80/public";

    public RouteManager(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public void getRouteByRouteId(int id, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("route_id",String.valueOf(id));
        PostRequest request = new PostRequest(serverURL + "/routes/get/from_user", params, listener, errorListener);
        queue.add(request);
    }

    public void getRoutesByUserId(int id, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("user_id",String.valueOf(id));
        PostRequest request = new PostRequest(serverURL + "/routes/get/from_user", params, listener, errorListener);
        queue.add(request);
    }

    public void addRoute(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("user_id",String.valueOf(route.getUserId()));
        params.put("start_point",route.start_point);
        params.put("end_point",route.end_point);
        params.put("route_name",route.route_name);
        PostRequest request = new PostRequest(serverURL + "/routes/add", params, listener, errorListener);
        queue.add(request);
    }

    public void changeRouteStartPoint(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("route_id",String.valueOf(route.getRouteId()));
        params.put("start_point",route.start_point);
        PostRequest request = new PostRequest(serverURL + "/routes/change/start_point", params, listener, errorListener);
        queue.add(request);
    }

    public void changeRouteEndPoint(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("route_id",String.valueOf(route.getRouteId()));
        params.put("end_point",route.end_point);
        PostRequest request = new PostRequest(serverURL + "/routes/change/end_point", params, listener, errorListener);
        queue.add(request);
    }

    public void changeRouteName(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("route_id",String.valueOf(route.getRouteId()));
        params.put("route_name",route.route_name);
        PostRequest request = new PostRequest(serverURL + "/change/route_name", params, listener, errorListener);
        queue.add(request);
    }

    public void removeRoute(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("route_id",String.valueOf(route.getRouteId()));
        PostRequest request = new PostRequest(serverURL + "/routes/remove", params, listener, errorListener);
        queue.add(request);
    }
}

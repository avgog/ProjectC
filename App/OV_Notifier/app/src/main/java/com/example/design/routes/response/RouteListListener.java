package com.example.design.routes.response;

import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.example.design.routes.Route;
import com.example.design.routes.RouteManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RouteListListener implements Response.Listener {
    private ListView listView;
    RouteManager manager;

    public RouteListListener(ListView listView, RouteManager manager){
        this.listView = listView;
        this.manager = manager;
    }

    @Override
    public void onResponse(Object response) {
        try{
            JSONObject json = new JSONObject(response.toString());
            JSONArray array = json.getJSONArray("result");
            Route[] routes = Route.fromJSONRoutes(array);
            listView.setAdapter(new RouteListAdapter(listView.getContext(), routes, manager,listView));
        }
        catch (JSONException e){
            Log.e("route request", "json error: " + e.toString());
        }
    }
}

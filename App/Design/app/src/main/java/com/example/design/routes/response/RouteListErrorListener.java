package com.example.design.routes.response;

import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.design.routes.Route;
import com.example.design.routes.RouteManager;

public class RouteListErrorListener implements Response.ErrorListener {
    private ListView listView;
    private RouteManager manager;

    public RouteListErrorListener(ListView listView, RouteManager manager){
        this.manager = manager;
        this.listView = listView;
    }
    public void onErrorResponse(VolleyError error) {
        Log.e("route request", error.toString());
        listView.setAdapter(new RouteManager.RouteListAdapter(listView.getContext(), new Route[]{}, null, listView));
    }
}

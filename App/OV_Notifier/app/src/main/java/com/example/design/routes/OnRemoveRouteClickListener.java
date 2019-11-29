package com.example.design.routes;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class OnRemoveRouteClickListener implements View.OnClickListener {

    private RouteManager manager;
    public int routeId; //routeId must be set from the outside
    private ListView listView; //listview with routes

    public OnRemoveRouteClickListener(RouteManager manager, ListView listView){
        this.manager = manager;
        this.listView = listView;
    }

    @Override
    public void onClick(View view){
        manager.removeRoute(
                routeId,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        manager.getRouteByRouteId(
                                routeId,
                                new RouteListListener(listView, new OnRemoveRouteClickListener(manager, listView)),
                                new RouteListErrorListener(listView)
                                );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("remove request", error.toString());
                    }
                }
        );
    }
}

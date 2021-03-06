package com.example.design.routes;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.design.routes.response.RouteListErrorListener;
import com.example.design.routes.response.RouteListListener;

//Is used for firing an event after a press on the remove route button.
public class OnRemoveRouteClickListener implements View.OnClickListener {

    private RouteManager manager;
    private Route route;
    private ListView listView; //listview with routes

    public OnRemoveRouteClickListener(RouteManager manager, ListView listView, Route route){
        this.manager = manager;
        this.listView = listView;
        this.route = route;
    }

    @Override
    public void onClick(View view){ //executed when the user presses on the button
        manager.removeRoute( //remove the route
                route.getRouteId(),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        manager.getRoutesByUserId(
                                new RouteListListener(listView,manager),
                                new RouteListErrorListener(listView,manager)
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

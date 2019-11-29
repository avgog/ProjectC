package com.example.design.routes;

import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RouteListListener implements Response.Listener {
    private ListView listView;
    private OnRemoveRouteClickListener removeRouteListener;

    public RouteListListener(ListView listView, OnRemoveRouteClickListener removeRouteListener){
        this.listView = listView;
        this.removeRouteListener = removeRouteListener;
    }

    @Override
    public void onResponse(Object response) {
        List<Route> routes = new ArrayList<>();
        try{
            JSONObject json = new JSONObject(response.toString());
            JSONArray array = json.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonRoute = array.getJSONObject(i);

                Route route = new Route(
                        jsonRoute.getInt("route_id"),
                        jsonRoute.getInt("user_id"),
                        jsonRoute.getString("start"),
                        jsonRoute.getString("end"),
                        jsonRoute.getString("route_name")
                );

                routes.add(route);
            }
            Route[] routeArray = new Route[routes.size()];
            routes.toArray(routeArray);
            listView.setAdapter(new RouteListAdapter(listView.getContext(), routeArray, removeRouteListener));
        }
        catch (JSONException e){
            Log.e("route request", "json error: " + e.toString());
        }
    }
}

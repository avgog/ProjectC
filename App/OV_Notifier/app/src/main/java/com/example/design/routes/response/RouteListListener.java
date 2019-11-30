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
            listView.setAdapter(new RouteListAdapter(listView.getContext(), routeArray, manager,listView));
        }
        catch (JSONException e){
            Log.e("route request", "json error: " + e.toString());
        }
    }
}

package com.example.design.routes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private int route_id;
    private int user_id;
    public String start_point;
    public String end_point;
    public String route_name;

    public Route(int route_id, int user_id, String start_point, String end_point, String route_name){
        this.route_id = route_id;
        this.user_id = user_id;
        this.start_point = start_point;
        this.end_point = end_point;
        this.route_name = route_name;
    }

    public int getRouteId(){
        return route_id;
    }

    public int getUserId(){
        return  user_id;
    }

    //convert json object array to route array
    public static Route[] fromJSONRoutes(JSONArray array){
        List<Route> routes = new ArrayList<>();
        try{
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

        }
        catch (JSONException e){
            Log.e("JSON -> Routes", "json error: " + e.toString());
        }
        Route[] routeArray = new Route[routes.size()];
        routes.toArray(routeArray);
        return routeArray;
    }
}

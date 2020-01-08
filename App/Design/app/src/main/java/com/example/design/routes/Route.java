package com.example.design.routes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//responsible for holding route related data
public class Route {
    private int route_id; //this id is unique
    private int user_id;
    public String start_point; //place of departure
    public String end_point;  //destination of the route
    public String route_name; //name that should be displayed on the route list

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
        List<Route> routes = new ArrayList<>(); //create a list for storing routes
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
        catch (JSONException e){ //Log the error if something went wrong
            Log.e("JSON -> Routes", "json error: " + e.toString());
        }
        Route[] routeArray = new Route[routes.size()];
        routes.toArray(routeArray); //convert a list to an array
        return routeArray;
    }
}

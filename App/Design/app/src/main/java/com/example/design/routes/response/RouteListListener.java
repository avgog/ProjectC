package com.example.design.routes.response;

import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.example.design.routes.Route;
import com.example.design.routes.RouteManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Create an instance of this class to update the list of routes on the page.
public class RouteListListener implements Response.Listener {
    private ListView listView;
    RouteManager manager;

    public RouteListListener(ListView listView, RouteManager manager){
        this.listView = listView;
        this.manager = manager;
    }

    @Override
    public void onResponse(Object response) { //executed when the app gets a response from the server
        try{
            JSONObject json = new JSONObject(response.toString()); //convert a string to a jsonobject
            JSONArray array = json.getJSONArray("result"); //get the json array in the jsonobject
            Route[] routes = Route.fromJSONRoutes(array); //create an array of routes from a json array.
            listView.setAdapter(new RouteManager.RouteListAdapter(listView.getContext(), routes, manager,listView)); //refresh the list
        }
        catch (JSONException e){
            Log.e("route request", "json error: " + e.toString());
        }
    }
}

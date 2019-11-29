package com.example.design.routes;

import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class RouteListErrorListener implements Response.ErrorListener {
    private ListView listView;

    public RouteListErrorListener(ListView listView){
        this.listView = listView;
    }
    public void onErrorResponse(VolleyError error) {
        Log.e("route request", error.toString());
        listView.setAdapter(new RouteListAdapter(listView.getContext(), new Route[]{}, null));
    }
}

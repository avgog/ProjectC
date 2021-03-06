package com.example.design.routes;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.design.R;
import com.example.design.request.PostRequest;
import com.example.design.user.UserToken;

import java.util.HashMap;
import java.util.Map;

public class RouteManager { //responsible for executing route api functions
    private RequestQueue queue;
    private String serverURL;

    public RouteManager(Context context){
        queue = Volley.newRequestQueue(context);
        serverURL = context.getResources().getString(R.string.server_url);
    }

    //find a route in the database by the id of the route
    public void getRouteByRouteId(int id, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("token", UserToken.currentUser.getToken());
        params.put("user_id", String.valueOf(UserToken.currentUser.getUserId()));
        params.put("route_id",String.valueOf(id));
        PostRequest request = new PostRequest(serverURL + "/public/routes/get/from_id", params, listener, errorListener);
        queue.add(request); //execute the request
    }

    //find all routes in the database that belongs to a certain user.
    public void getRoutesByUserId(Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("token", UserToken.currentUser.getToken());
        params.put("user_id", String.valueOf(UserToken.currentUser.getUserId()));
        PostRequest request = new PostRequest(serverURL + "/public/routes/get/from_user", params, listener, errorListener);
        queue.add(request); //execute the request
    }

    //add a route in the database
    public void addRoute(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("token", UserToken.currentUser.getToken());
        params.put("user_id", String.valueOf(UserToken.currentUser.getUserId()));
        params.put("start_point",route.start_point);
        params.put("end_point",route.end_point);
        params.put("route_name",route.route_name);
        PostRequest request = new PostRequest(serverURL + "/public/routes/add", params, listener, errorListener);
        queue.add(request); //execute the request
    }

    //Change the place of departure of a route
    public void changeRouteStartPoint(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("token", UserToken.currentUser.getToken());
        params.put("user_id", String.valueOf(UserToken.currentUser.getUserId()));
        params.put("route_id",String.valueOf(route.getRouteId()));
        params.put("start_point",route.start_point);
        PostRequest request = new PostRequest(serverURL + "/public/routes/change/start_point", params, listener, errorListener);
        queue.add(request); //execute the request
    }

    //Change the destination of a route
    public void changeRouteEndPoint(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("token", UserToken.currentUser.getToken());
        params.put("user_id", String.valueOf(UserToken.currentUser.getUserId()));
        params.put("route_id",String.valueOf(route.getRouteId()));
        params.put("end_point",route.end_point);
        PostRequest request = new PostRequest(serverURL + "/public/routes/change/end_point", params, listener, errorListener);
        queue.add(request); //execute the request
    }

    //Change the name of a route.
    public void changeRouteName(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("token", UserToken.currentUser.getToken());
        params.put("user_id", String.valueOf(UserToken.currentUser.getUserId()));
        params.put("route_id",String.valueOf(route.getRouteId()));
        params.put("route_name",route.route_name);
        PostRequest request = new PostRequest(serverURL + "/public/routes/change/route_name", params, listener, errorListener);
        queue.add(request); //execute the request
    }

    //Remove a certain route
    public void removeRoute(int id, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("token", UserToken.currentUser.getToken());
        params.put("user_id", String.valueOf(UserToken.currentUser.getUserId()));
        params.put("route_id",String.valueOf(id));
        PostRequest request = new PostRequest(serverURL + "/public/routes/remove", params, listener, errorListener);
        queue.add(request); //execute the request
    }

    public void setRouteActiveState(int id, boolean active, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("token", UserToken.currentUser.getToken());
        params.put("user_id", String.valueOf(UserToken.currentUser.getUserId()));
        params.put("route_id",String.valueOf(id));
        params.put("state",(String.valueOf((active)? 1 : 0)));

        PostRequest request = new PostRequest(serverURL + "/public/route/state", params, listener, errorListener);
        queue.add(request); //execute the request
    }

    public static class RouteListAdapter extends ArrayAdapter<Route> { //responsible for displaying routes on the screen
        private Context context;
        private Route routes[];
        private RouteManager manager;
        private ListView listView;


        public RouteListAdapter (Context context, Route routes[], RouteManager manager, ListView listView) {
            super(context, R.layout.row, R.id.textView1,routes);
            this.context = context;
            this.routes = routes;
            this.manager = manager;
            this.listView = listView;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) { //executes on initialization and when scrolling
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            final ToggleButton bellbutton = row.findViewById(R.id.notificationbutton);
            Button removeRouteButton = row.findViewById(R.id.deleteRouteButton);
            removeRouteButton.setOnClickListener(new OnRemoveRouteClickListener(manager, listView, routes[position]));

            bellbutton.setOnCheckedChangeListener(null);
            bellbutton.setChecked(routes[position].active);
            if (routes[position].active) {
                bellbutton.setBackgroundResource(R.drawable.bell);
            } else {
                bellbutton.setBackgroundResource(R.drawable.emptybell);
            }

            bellbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        bellbutton.setBackgroundResource(R.drawable.bell);
                    } else {
                        bellbutton.setBackgroundResource(R.drawable.emptybell);
                    }
                    manager.setRouteActiveState(routes[position].getRouteId(), isChecked, new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.i("active toggle", "response: " + response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("active toggle", error.toString());
                        }
                    });
                }
            });



            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            myTitle.setTypeface(Typeface.DEFAULT_BOLD);
            myTitle.setTextSize(19);
            images.setImageResource(R.drawable.pointer);
            //myTitle.setText(routes[position].route_name + "(ID:"+routes[position].getRouteId()+")"); //voor debuggen
            myTitle.setText(routes[position].route_name);

            return row;
        }
    }
}

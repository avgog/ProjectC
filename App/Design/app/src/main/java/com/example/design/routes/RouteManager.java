package com.example.design.routes;

import android.content.Context;
import android.graphics.Typeface;
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
import com.android.volley.toolbox.Volley;
import com.example.design.R;
import com.example.design.request.PostRequest;

import java.util.HashMap;
import java.util.Map;

public class RouteManager {
    public static RequestQueue queue;

    public static String serverURL = "http://projectc.caslayoort.nl:80/public";

    public RouteManager(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public void getRouteByRouteId(int id, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("route_id",String.valueOf(id));
        PostRequest request = new PostRequest(serverURL + "/routes/get/from_id", params, listener, errorListener);
        queue.add(request);
    }

    public void getRoutesByUserId(int id, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("user_id",String.valueOf(id));
        PostRequest request = new PostRequest(serverURL + "/routes/get/from_user", params, listener, errorListener);
        queue.add(request);
    }

    public void addRoute(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("user_id",String.valueOf(route.getUserId()));
        params.put("start_point",route.start_point);
        params.put("end_point",route.end_point);
        params.put("route_name",route.route_name);
        PostRequest request = new PostRequest(serverURL + "/routes/add", params, listener, errorListener);
        queue.add(request);
    }

    public void changeRouteStartPoint(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("route_id",String.valueOf(route.getRouteId()));
        params.put("start_point",route.start_point);
        PostRequest request = new PostRequest(serverURL + "/routes/change/start_point", params, listener, errorListener);
        queue.add(request);
    }

    public void changeRouteEndPoint(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("route_id",String.valueOf(route.getRouteId()));
        params.put("end_point",route.end_point);
        PostRequest request = new PostRequest(serverURL + "/routes/change/end_point", params, listener, errorListener);
        queue.add(request);
    }

    public void changeRouteName(Route route, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("route_id",String.valueOf(route.getRouteId()));
        params.put("route_name",route.route_name);
        PostRequest request = new PostRequest(serverURL + "/routes/change/route_name", params, listener, errorListener);
        queue.add(request);
    }

    public void removeRoute(int id, Response.Listener listener, Response.ErrorListener errorListener){
        Map<String,String> params = new HashMap<>();
        params.put("route_id",String.valueOf(id));
        PostRequest request = new PostRequest(serverURL + "/routes/remove", params, listener, errorListener);
        queue.add(request);
    }

    public static class RouteListAdapter extends ArrayAdapter<Route> {
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
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            final ToggleButton bellbutton = row.findViewById(R.id.notificationbutton);
            Button removeRouteButton = row.findViewById(R.id.deleteRouteButton);
            removeRouteButton.setOnClickListener(new OnRemoveRouteClickListener(manager, listView, routes[position]));

            bellbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        bellbutton.setBackgroundResource(R.drawable.bell);
                    } else {
                        bellbutton.setBackgroundResource(R.drawable.emptybell);
                    }
                }
            });

            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            myTitle.setTypeface(Typeface.DEFAULT_BOLD);
            myTitle.setTextSize(19);
            images.setImageResource(R.drawable.pointer);
            myTitle.setText(routes[position].route_name);

            return row;
        }
    }
}

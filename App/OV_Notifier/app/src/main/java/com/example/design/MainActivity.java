package com.example.design;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.design.routes.Route;
import com.example.design.routes.RouteManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
    Button profileButton;
    ListView listView;
    //String routeTitles[] = {};
    //int images[] = {R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer};

    RouteManager routeManager;
    final int USER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.mainlistview);
        profileButton = (findViewById(R.id.profileButton));

        MyAdapter adapter = new MyAdapter(this, new Route[]{});
        listView.setAdapter(adapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), RoutePage.class);
                startActivity(intent);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Settings.class));
            }
        });


        final Response.Listener routeListListener = new Response.Listener() {
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
                                jsonRoute.getString("start_point"),
                                jsonRoute.getString("end_point"),
                                jsonRoute.getString("route_name")
                        );

                        routes.add(route);
                    }
                    Route[] routeArray = new Route[routes.size()];
                    routes.toArray(routeArray);
                    listView.setAdapter(new MyAdapter(listView.getContext(), routeArray));
                }
                catch (JSONException e){
                    Log.e("route request", "json error: " + e.toString());
                }
            }
        };

        final Response.ErrorListener routeListErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("route request", error.toString());
                listView.setAdapter(new MyAdapter(listView.getContext(), new Route[]{}));
            }
        };

        routeManager = new RouteManager(this);
        routeManager.getRoutesByUserId(USER_ID, routeListListener, routeListErrorListener);

        Button addRouteButton = findViewById(R.id.addbutton);
        /*addRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Button addButton = (Button)v;
                addButton.setEnabled(false); //temporary disables the button

                routeManager.addRoute(new Route(0, USER_ID, "[0,0]", "[0,0]", "new route"), new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        routeManager.getRoutesByUserId(USER_ID, routeListListener, routeListErrorListener);
                        addButton.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("route request", error.toString());
                        addButton.setEnabled(true);
                    }
                });
                v.setEnabled(true);
            }
        });*/
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(MainActivity.this, Home.class));
                    break;
            }
            return true;
        }
    };
    @Override
    public void onBackPressed(){
        startActivity(new Intent(MainActivity.this, Home.class));
    }

    class MyAdapter extends ArrayAdapter<Route> {
        Context context;
        Route routes[];


        MyAdapter (Context c, Route routes[]) {
            super(c, R.layout.row, R.id.textView1,routes);
            this.context = c;
            this.routes = routes;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            final ToggleButton bellbutton = row.findViewById(R.id.notificationbutton);
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


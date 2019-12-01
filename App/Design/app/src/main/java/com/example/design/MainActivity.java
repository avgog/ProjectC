package com.example.design;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.design.routes.Route;
import com.example.design.routes.response.RouteListErrorListener;
import com.example.design.routes.response.RouteListListener;
import com.example.design.routes.RouteManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
    Button profileButton;
    ListView listView;

    RouteManager routeManager;
    public static final int USER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.mainlistview);
        profileButton = (findViewById(R.id.profileButton));

        //responsible for calling route related functions of the server API
        routeManager = new RouteManager(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), RoutePage.class);
                int routeId = ((Route)parent.getItemAtPosition(position)).getRouteId();
                intent.putExtra("routeId", routeId); //store the route in the new intent
                startActivity(intent);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Settings.class));
            }
        });

        Button addRouteButton = findViewById(R.id.addRouteButton);
        addRouteButton.setOnClickListener(new View.OnClickListener() { //When clicked: adds a route to the database and reload the list of routes
            @Override
            public void onClick(View v) {
                final Button addButton = (Button)v;
                addButton.setEnabled(false); //temporary disables the button
                Route route = new Route(0, USER_ID, "-", "-", "new route");
                routeManager.addRoute(route, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        routeManager.getRoutesByUserId(
                                USER_ID,
                                new RouteListListener(listView,routeManager),
                                new RouteListErrorListener(listView,routeManager)
                        );
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
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        //create and send a request to receive all routes of an user. After receiving a response, display the routes on the listview
        routeManager.getRoutesByUserId(
                USER_ID,
                new RouteListListener(listView,routeManager),
                new RouteListErrorListener(listView,routeManager)
        );

        RouteManager.RouteListAdapter adapter = new RouteManager.RouteListAdapter(this, new Route[]{}, routeManager, listView);
        listView.setAdapter(adapter); //connect the listview with adapter which is responsible for filling the list with routes
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


}


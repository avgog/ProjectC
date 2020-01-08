package com.example.design;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.design.user.LoginData;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.design.user.UserToken;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Home extends AppCompatActivity {

    Button profileButton;
    ListView listView;

    int mImgs = R.drawable.pointer;
    static ArrayList<String> mTitle = new ArrayList<>();
    static ArrayList<String> mDate = new ArrayList<>();
    static ArrayList<String> mTime = new ArrayList<>();
    static ArrayList<String> mName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(!(Settings.language == null)){
            setAppLocale(Settings.language);
        }
        listView = findViewById(R.id.homeListview);
        MyAdapter adapter = new MyAdapter(this, mName, mImgs, mTime, mDate,mTitle);
        listView.setAdapter(adapter);
        profileButton = (findViewById(R.id.profileButton));
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        jsonParse(this, "http://projectc.caslayoort.nl/public/times/active",null,null,null, null,"startup", null);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        //Opens the settings
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Settings.class));
            }

        });

        final Context context = this;
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginData.logout(context);
            }
        });
    }


    public void setAppLocale(String appLocale){
        Locale locale = new Locale(appLocale);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

    }

    //Refreshes page when opened again
    protected void onResume(){
        super.onResume();
        try{
        if(Settings.shouldRestart == "yes"){
            Settings.shouldRestart = "no";
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            this.overridePendingTransition(0, 0);
        } } catch (Exception e) { Log.i("okidoki",e.toString()); }
    }

    //Makes sure the right activity gets opened on back press
    @Override
    public void onBackPressed(){
        startActivity(new Intent(Home.this, Home.class));
    }

    //Creates http requests to the API. In the parameters variables can be passed if the variable is null it will be ignored and not passed on to the api.
    public void jsonParse(Context context, String url, final String routeid, final String endtime, final String date,final String timeid, final String type, final String state) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("okidoki", response);
                if (type == "startup") {
                    mTime.clear();
                    mDate.clear();
                    mTitle.clear();
                    mName.clear();
                    try {
                        JSONArray array = new JSONArray(response);
                        Log.i("okidoki", array.toString());
                        for (int i=0; i <array.length();i++) {
                            JSONObject jsonTime = array.getJSONObject(i);
                            //retrieves everything from the response given by the JSON and puts it into lists so it can be initialized
                            mTitle.add(jsonTime.getString("id"));
                            mTime.add(jsonTime.getString("timeofarrival"));
                            mDate.add(jsonTime.getString("date"));
                            mName.add(jsonTime.getString("route_name"));


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Refreshes the listview
                    listView.setAdapter(null);
                    MyAdapter adapter = new Home.MyAdapter(Home.this, mName, mImgs, mTime, mDate,mTitle);
                    listView.setAdapter(adapter);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("okidoki",error.toString());
            }
        }){
            //Maps the parameters so they can be sent off to the API
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token", UserToken.currentUser.getToken());
                params.put("user_id", String.valueOf(UserToken.currentUser.getUserId()));
                if(timeid!=null){
                    params.put("time_id", timeid);
                }
                if(routeid != null) {
                    params.put("route_id",routeid);
                }
                if(endtime != null) {
                    params.put("end_time", endtime);
                    Log.i("okidoki","executed");
                }
                if (date != null) {
                    params.put("date", date);
                }
                if (state != null) {
                    params.put("state",state);
                }

                return params;
            }



        };
        queue.add(sr);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_routes:
                            startActivity(new Intent(Home.this, MainActivity.class));
                            break;
                    }
                    return true;
                }
            };

            class MyAdapter extends ArrayAdapter<String> {
                Context context;
                ArrayList<String> rTitle;
                int rImgs;
                ArrayList<String> rTime;
                ArrayList<String> rDate;
                ArrayList<String> rIds;


                MyAdapter(Context c, ArrayList<String> title, int imgs, ArrayList<String> time, ArrayList<String> date, ArrayList<String> ids) {
                    super(c, R.layout.row, R.id.textView1, title);
                    this.context = c;
                    this.rIds = ids;
                    this.rTitle = title;
                    this.rImgs = imgs;
                    this.rTime = time;
                    this.rDate = date;

                }


                @NonNull
                @Override
                public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View row = layoutInflater.inflate(R.layout.rowhome, parent, false);
                    ImageView images = row.findViewById(R.id.iconImage);
                    TextView myTitle = row.findViewById(R.id.homeTitle);
                    TextView myTime = row.findViewById(R.id.homeTime);
                    TextView myDate = row.findViewById(R.id.homeDate);
                    Switch activeSwitch = row.findViewById(R.id.ActiveSwitch);
                    // Checks if there is a change in the switch and then updates the active status to the API
                    activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked) {
                                jsonParse(Home.this, "http://projectc.caslayoort.nl/public/times/state",null,null, rIds.get(position),rIds.get(position),"state","1");
                            } else {
                                jsonParse(Home.this, "http://projectc.caslayoort.nl/public/times/state",null, null, rIds.get(position),rIds.get(position),"state","0");

                            }
                        }
                    });
                    myTitle.setTypeface(Typeface.DEFAULT_BOLD);
                    myTitle.setTextSize(19);
                    images.setImageResource(rImgs);
                    myTitle.setText(rTitle.get(position));
                    myTime.setText(rTime.get(position));
                    myDate.setText(rDate.get(position));
                    return row;
                }
            }
        }




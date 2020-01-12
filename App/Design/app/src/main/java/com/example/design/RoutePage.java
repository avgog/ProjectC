package com.example.design;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.design.routes.Route;
import com.example.design.routes.RouteManager;
import com.example.design.user.UserToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;

public class RoutePage extends AppCompatActivity implements TimePickerFragment.TimePickerListener, DatePickerDialog.OnDateSetListener, MenuItem.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {

    ListView listView;
    static ArrayList<String> emptylist = new ArrayList<>();
    static ArrayList<String> mTime = new ArrayList<>();
    static ArrayList<String> mDate = new ArrayList<>();
    static ArrayList<Integer> mTimeid = new ArrayList<>();
    static ArrayList<Integer> mActive = new ArrayList<>();
    String activity;
    TextView editTextView;
    AlertDialog alertDialog;
    EditText editText;
    Calendar calender127;
    static String date;
    static String time;
    static String locStr;
    static String desStr;
    EditText fromLocationField;
    EditText toLocationField;
    static EditText activeButton;
    static int routeId;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    RouteManager routeManager;
    Route currentRoute;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_route_page);
        RoutePage.routeId = getIntent().getIntExtra("routeId",-1); //get the routeId value that was stored by the MainActivity
        jsonParse(this, "http://projectc.caslayoort.nl:80/public/times/route_id",String.valueOf(RoutePage.routeId),null,null, null,null,"startup", null);
        Log.i("okidoki", String.valueOf(routeId)+ "start");
        Button AgendaButton = findViewById(R.id.agenda);

        locStr = getIntent().getStringExtra("locationString");
        desStr = getIntent().getStringExtra("destinationString");

        final int routeId = getIntent().getIntExtra("routeId",-1); //get the routeId value that was stored by the MainActivity

        fromLocationField = findViewById(R.id.FromLocationButton);
        toLocationField = findViewById(R.id.ToLocationButton);

        final ToggleButton Monday = (findViewById(R.id.mondaybutton));
        final ToggleButton Tuesday = (findViewById(R.id.tuesdaybutton));
        final ToggleButton Wednesday = (findViewById(R.id.wednesdaybutton));
        final ToggleButton Thursday = (findViewById(R.id.thursdaybutton));
        final ToggleButton Friday = (findViewById(R.id.fridaybutton));
        final ToggleButton Saturday = (findViewById(R.id.saterdaybutton));
        final ToggleButton Sunday = (findViewById(R.id.sundaybutton));

        listView = findViewById(R.id.listview2);
        listView.setAdapter(null);

        routeManager = new RouteManager(this);

        Button closeButton = findViewById(R.id.CloseButton);
        editTextView = findViewById(R.id.routeTitle);
        final EditText fromLocationField = findViewById(R.id.FromLocationButton);
        final EditText toLocationField = findViewById(R.id.ToLocationButton);
        Button saveButton = findViewById(R.id.Save);

        fromLocationField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RoutePage.this, Load.class ).putExtra("VALUE", 1).putExtra("destinationString",desStr ).putExtra("routeId",routeId )   );
            }
        });

        toLocationField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RoutePage.this, Load.class ).putExtra("VALUE", 2).putExtra("locationString",locStr ).putExtra("routeId",routeId ) );
            }
        });


        editTextView.setText("Unknown Route");
        if(routeId != -1){
            routeManager.getRouteByRouteId(routeId, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try{
                        JSONObject json = new JSONObject(response.toString());
                        JSONArray array = json.getJSONArray("result");
                        Route[] routes = Route.fromJSONRoutes(array);
                        if(routes.length > 0){//check if a route has been found
                            editTextView.setText(routes[0].route_name);
                            if(locStr == null){
                                fromLocationField.setText(routes[0].start_point);
                            } else { fromLocationField.setText(locStr); }
                            if(desStr == null){
                                toLocationField.setText(routes[0].end_point);
                            } else { toLocationField.setText(desStr); }
                            currentRoute = routes[0];
                        }
                    }
                    catch (JSONException e){
                        Log.e("route request", "json error: " + e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Route request", error.toString());
                }
            });
        }

        //add event to the button for storing new values on the database
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Route update", "updating route...");
                Response.Listener listener = new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.i("Route update request", response.toString());
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Route update request", "error:"+error.toString());
                    }
                };
                currentRoute.start_point = fromLocationField.getText().toString();
                currentRoute.end_point = toLocationField.getText().toString();
                currentRoute.route_name = editTextView.getText().toString();

                routeManager.changeRouteName(currentRoute, listener,errorListener);
                routeManager.changeRouteStartPoint(currentRoute, listener,errorListener);
                routeManager.changeRouteEndPoint(currentRoute, listener,errorListener);
                startActivity(new Intent(RoutePage.this, MainActivity.class ));
            }
        });



        alertDialog = new AlertDialog.Builder(this).create();
        editText = new EditText(this);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(26)});
        alertDialog.setTitle(" Edit the text ");
        alertDialog.setView(editText);
        MyAdapter adapter = new MyAdapter(this, mTime, mDate, mActive);
        listView.setAdapter(adapter);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AgendaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.setCancelable(false);
                datePicker.show(getSupportFragmentManager(),"datepicker");
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "SAVE TEXT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editTextView.setText(editText.getText());
            }
        });

        editTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editTextView.getText());
                alertDialog.show();
            }
        });
        //All the repeat Buttons that open a clock so you can chose the time of the timescheme
        Monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RoutePage.date = "Monday";
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });
        Tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RoutePage.date = "Tuesday";
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });
        Wednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RoutePage.date = "Wednesday";
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });
        Thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RoutePage.date = "Thursday";
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });
        Friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RoutePage.date = "Friday";
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });
        Saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RoutePage.date = "Saturday";
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });
        Sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RoutePage.date = "Sunday";
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });

    }
    //Creates http requests to the API. In the parameters variables can be passed if the variable is null it will be ignored and not passed on to the api.
    public void jsonParse(Context context, String url, final String routeid, final String endtime, final String date,final String timeid,final String routename, final String type, final String state) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("okidoki", response);
                if (type == "startup") {
                    mTimeid.clear();
                    mTime.clear();
                    mDate.clear();
                    mActive.clear();
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i=0; i <array.length();i++) {
                            JSONObject jsonTime = array.getJSONObject(i);
                            RoutePage.mTimeid.add(jsonTime.getInt("id"));
                            RoutePage.mTime.add(jsonTime.getString("timeofarrival"));
                            RoutePage.mDate.add(jsonTime.getString("date"));
                            RoutePage.mActive.add(jsonTime.getInt("active"));
                            Log.i("okidoki",mActive.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listView.setAdapter(null);
                    MyAdapter adapter = new MyAdapter(RoutePage.this, mTime, mDate, mActive);
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
            protected  Map<String,String> getParams(){
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

                }
                if(timeid != null) {
                    params.put("id", timeid);
                }
                if (date != null) {
                    params.put("date", date);
                }
                if (routename != null) {
                    params.put("routename", routename);
                }
                if (state != null){
                    params.put("state", state);
                }

                return params;
            }



        };
        queue.add(sr);
    }


    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popupmenu);
        popup.show();
    }
    //Retrieves the date from onDateSet or one of the repeat buttons and passes these with the time to the API so a new time scheme can be created
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        RoutePage.time = (String.format("%02d:%02d", hour, minute));
        /*mTime.add(time);
        mDate.add(RoutePage.date);*/
        boolean stop = true;
        for (int i=0; stop ;i++) {
            if(!mTimeid.contains(i)) {
                stop = false;
                mTimeid.add(i);
            }

        }
        Log.i("okidoki", String.valueOf(routeId));
        jsonParse(this, "http://projectc.caslayoort.nl:80/public/times/add",String.valueOf(RoutePage.routeId),RoutePage.time,RoutePage.date,null,null,"add", null);
        //Refreshes the layout by retrieving all the timeschemes
        jsonParse(this, "http://projectc.caslayoort.nl:80/public/times/route_id",String.valueOf(RoutePage.routeId),null,null, null,null,"startup", null);
    }

    //Allows you to pick a date for the timescheme
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        RoutePage.date = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth);
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setCancelable(false);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.beurs:
                activeButton.setText("Beurs, Rotterdam");
                break;
            case R.id.capelsebrug:
                activeButton.setText("Capelsebrug, Capelle aan den Ijssel");
                break;
        }
        return false;    }


    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> rTime;
        ArrayList<String> rDate;
        ArrayList<Integer> rActive;


        MyAdapter(Context c, ArrayList<String> time, ArrayList<String> date, ArrayList<Integer> active) {
            super(c, R.layout.rowroutepage, R.id.homeDate, date);
            this.context = c;
            this.rDate = date;
            this.rTime = time;
            this.rActive = active;

        }



        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.rowroutepage, parent, false);
            Button deletebutton =  row.findViewById(R.id.delete);
            deletebutton.setOnClickListener(new View.OnClickListener() {
                //Removes the data from the lists so when refreshing the layout the timescheme is removed.
                @Override
                public void onClick(View v) {
                    mTime.remove(position);
                    mDate.remove(position);
                    mActive.remove(position);
                    jsonParse(RoutePage.this, "http://projectc.caslayoort.nl:80/public/times/remove",String.valueOf(RoutePage.routeId),null,null,String.valueOf(mTimeid.get(position)),null,"delete",null);
                    mTimeid.remove(position);
                    jsonParse(RoutePage.this, "http://projectc.caslayoort.nl:80/public/times/route_id",String.valueOf(RoutePage.routeId),null,null, null,null,"startup",null);

                }
            });
            Switch activeSwitch = row.findViewById(R.id.ActiveSwitch);
            if(rActive.get(position) == 1){
                activeSwitch.setChecked(true);
                Log.i("okidoki","triggered if switch 1");
            } else {
                activeSwitch.setChecked(false);
                Log.i("okidoki","triggered if switch 2");
            }
            activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        jsonParse(RoutePage.this, "http://projectc.caslayoort.nl/public/times/state",String.valueOf(RoutePage.routeId),null,null, String.valueOf(mTimeid.get(position)),null,"state","1");

                        Log.i("okidoki","triggered click switch 1");
                    } else {
                        jsonParse(RoutePage.this, "http://projectc.caslayoort.nl/public/times/state",String.valueOf(RoutePage.routeId),null,null, String.valueOf(mTimeid.get(position)),null,"state","0");
                        Log.i("okidoki","triggered click switch 2");
                    }
                }
            });
            TextView myDate = row.findViewById(R.id.homeDate);
            TextView myTime = row.findViewById(R.id.homeTime);
            myDate.setText(rDate.get(position));
            myTime.setText(rTime.get(position));
            return row;
        }
    }


}
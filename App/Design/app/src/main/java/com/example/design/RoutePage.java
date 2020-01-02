package com.example.design;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
<<<<<<< HEAD
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

=======
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.design.routes.Route;
import com.example.design.routes.RouteManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
>>>>>>> remotes/origin/master
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
    String activity;
    TextView editTextView;
    AlertDialog alertDialog;
    EditText editText;
    Calendar calender127;
    static String date;
    static String time;
    static String toLocation;
    static String fromLocation;
    static Button activeButton;
    static String routeid;


<<<<<<< HEAD
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
=======
    RouteManager routeManager;
    Route currentRoute;

>>>>>>> remotes/origin/master
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_route_page);
        routeid = "34";
        jsonParse(this, "http://projectc.caslayoort.nl:80/public/times/get/from_route",routeid,null,null, null,"startup");
        Button AgendaButton = findViewById(R.id.agenda);
        Button SaveButton = findViewById(R.id.Save);

        final Button fromLocationButton = findViewById(R.id.FromLocationButton);
        final Button toLocationButton = findViewById(R.id.ToLocationButton);
        final ToggleButton Monday = (findViewById(R.id.mondaybutton));
        final ToggleButton Tuesday = (findViewById(R.id.tuesdaybutton));
        final ToggleButton Wednesday = (findViewById(R.id.wednesdaybutton));
        final ToggleButton Thursday = (findViewById(R.id.thursdaybutton));
        final ToggleButton Friday = (findViewById(R.id.fridaybutton));
        final ToggleButton Saturday = (findViewById(R.id.saterdaybutton));
        final ToggleButton Sunday = (findViewById(R.id.sundaybutton));
        listView = findViewById(R.id.listview2);
<<<<<<< HEAD
        listView.setAdapter(null);
=======

        routeManager = new RouteManager(this);
>>>>>>> remotes/origin/master
        Button closeButton = findViewById(R.id.CloseButton);
        editTextView = findViewById(R.id.routeTitle);
        final EditText fromLocationField = findViewById(R.id.FromLocationButton);
        final EditText toLocationField = findViewById(R.id.ToLocationButton);
        Button saveButton = findViewById(R.id.Save);

        int routeId = getIntent().getIntExtra("routeId",-1); //get the routeId value that was stored by the MainActivity
        editTextView.setText("Unknown Route");
        if(routeId != -1){
            routeManager.getRouteByRouteId(routeId, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try{
                        JSONObject json = new JSONObject(response.toString());
                        JSONArray array = json.getJSONArray("result");
                        Route[] routes = Route.fromJSONRoutes(array);
                        if(routes.length > 0){ //check if a route has been found
                            editTextView.setText(routes[0].route_name);
                            fromLocationField.setText(routes[0].start_point);
                            toLocationField.setText(routes[0].end_point);
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
            }
        });



        alertDialog = new AlertDialog.Builder(this).create();
        editText = new EditText(this);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(26)});
        alertDialog.setTitle(" Edit the text ");
        alertDialog.setView(editText);
        MyAdapter adapter = new MyAdapter(this, mTime, mDate);
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
              toLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoutePage.activeButton = toLocationButton;
                showPopup(v);
            }
        });
        fromLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoutePage.activeButton = fromLocationButton;
                showPopup(v);



            }
        });
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(null);
            }
        });
    }

    public void jsonParse(Context context, String url, final String routeid, final String endtime, final String date,final String timeid, final String type) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("okidoki", response);
                if (type == "startup") {
                    mTimeid.clear();
                    mTime.clear();
                    mDate.clear();
                    try {
                        JSONObject json = new JSONObject(response);
                        JSONArray array = json.getJSONArray("result");
                        for (int i=0; i <array.length();i++) {
                            JSONObject jsonTime = array.getJSONObject(i);
                            RoutePage.mTimeid.add(jsonTime.getInt("id"));
                            RoutePage.mTime.add(jsonTime.getString("timeofarrival"));
                            RoutePage.mDate.add(jsonTime.getString("date"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listView.setAdapter(null);
                    MyAdapter adapter = new MyAdapter(RoutePage.this, mTime, mDate);
                    listView.setAdapter(adapter);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("okidoki",error.toString());
            }
        }){
            @Override
            protected  Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token","081848afca7affee3e760bd18b80bf51ef1f1133ae70dbd5e26e64f553c33779");
                params.put("user_id","6");
                if(timeid!=null){
                    params.put("time_id", timeid);
                }
                if(routeid != null) {
                    params.put("route_id",routeid);
                }
                if(endtime != null) {
                    params.put("end_time", endtime);

                }
                if (date != null) {
                    params.put("date", date);
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

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String time = (String.format("%02d:%02d", hour, minute));
        /*mTime.add(time);
        mDate.add(RoutePage.date);*/
        boolean stop = true;
        for (int i=0; stop ;i++) {
            if(!mTimeid.contains(i)) {
                stop = false;
                mTimeid.add(i);
            }

        }

        jsonParse(this, "http://projectc.caslayoort.nl:80/public/times/add",routeid,time,RoutePage.date,null,"add");
        jsonParse(this, "http://projectc.caslayoort.nl:80/public/times/get/from_route",routeid,null,null, null,"startup");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        RoutePage.date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
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


        MyAdapter(Context c, ArrayList<String> time, ArrayList<String> date) {
            super(c, R.layout.rowroutepage, R.id.homeDate, date);
            this.context = c;
            this.rDate = date;
            this.rTime = time;

        }



        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.rowroutepage, parent, false);
            Button deletebutton =  row.findViewById(R.id.delete);
            deletebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("okidoki",String.valueOf("position:" + position));
                    Log.i("okidoki", String.valueOf("mTime:" + mTime));
                    Log.i("okidoki", String.valueOf("mTimeid:" + mTimeid));
                    mTime.remove(position);
                    mDate.remove(position);
                    jsonParse(RoutePage.this, "http://projectc.caslayoort.nl:80/public/times/remove",routeid,null,null,String.valueOf(mTimeid.get(position)),"delete");
                    mTimeid.remove(position);
                    jsonParse(RoutePage.this, "http://projectc.caslayoort.nl:80/public/times/get/from_route",routeid,null,null, null,"startup");

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

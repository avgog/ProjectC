package com.example.design;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.design.routes.Route;
import com.example.design.routes.RouteManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Console;
import java.util.Calendar;

public class RoutePage extends AppCompatActivity {

    ListView listView;
    String mTime[] = {"9:00","16:00","21:30"};
    String mDate[] = {"26-04-2019","Friday","Wednesday"};
    String activity;
    TextView editTextView;
    AlertDialog alertDialog;
    EditText editText;
    Calendar calender;
    String date;
    DatePickerDialog datepicker;
    static String locStr;
    static String desStr;
    EditText fromLocationField;
    EditText toLocationField;

    RouteManager routeManager;
    Route currentRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_route_page);

        locStr = getIntent().getStringExtra("locationString");
        desStr = getIntent().getStringExtra("destinationString");

        fromLocationField = findViewById(R.id.FromLocationButton);
        toLocationField = findViewById(R.id.ToLocationButton);

        fromLocationField.setText(locStr);
        toLocationField.setText(desStr);

        if(locStr == null){
            fromLocationField.setText("-");
        }
        if(desStr == null){
            toLocationField.setText("-");
        }

        fromLocationField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RoutePage.this, Load.class ).putExtra("VALUE", 1));
            }
        });

        toLocationField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RoutePage.this, Load.class ).putExtra("VALUE", 2));
            }
            });


        Button AgendaButton = findViewById(R.id.agenda);
        final ToggleButton Monday = (findViewById(R.id.mondaybutton));
        final ToggleButton Tuesday = (findViewById(R.id.tuesdaybutton));
        final ToggleButton Wednesday = (findViewById(R.id.wednesdaybutton));
        final ToggleButton Thursday = (findViewById(R.id.thursdaybutton));
        final ToggleButton Friday = (findViewById(R.id.fridaybutton));
        final ToggleButton Saturday = (findViewById(R.id.saterdaybutton));
        final ToggleButton Sunday = (findViewById(R.id.sundaybutton));
        listView = findViewById(R.id.listview2);

        routeManager = new RouteManager(this);
        Button closeButton = findViewById(R.id.CloseButton);
        editTextView = findViewById(R.id.routeTitle);



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
                calender = Calendar.getInstance();
                int day = calender.get(Calendar.DAY_OF_MONTH);
                int month = calender.get(Calendar.MONTH);
                int year = calender.get(Calendar.YEAR);

                datepicker = new DatePickerDialog(RoutePage.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        date = (mDay + "/" + mMonth + "/" + mYear);
                    }
                },day , month, year);
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
                if (isChecked) {
                    Monday.setTextColor(Color.BLACK);
                } else {
                    Monday.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        Tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Tuesday.setTextColor(Color.BLACK);
                } else {
                    Tuesday.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        Wednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Wednesday.setTextColor(Color.BLACK);
                } else {
                    Wednesday.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        Thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Thursday.setTextColor(Color.BLACK);
                } else {
                    Thursday.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        Friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Friday.setTextColor(Color.BLACK);
                } else {
                    Friday.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        Saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Saturday.setTextColor(Color.BLACK);
                } else {
                    Saturday.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        Sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Sunday.setTextColor(Color.BLACK);
                } else {
                    Sunday.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
    }


    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTime[];
        String rDate[];


        MyAdapter(Context c, String time[], String date[]) {
            super(c, R.layout.rowroutepage, R.id.homeDate, date);
            this.context = c;
            this.rDate = date;
            this.rTime = time;

        }



        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.rowroutepage, parent, false);
            TextView myDate = row.findViewById(R.id.homeDate);
            TextView myTime = row.findViewById(R.id.homeTime);
            myDate.setText(rDate[position]);
            myTime.setText(rTime[position]);
            return row;
        }
    }

}

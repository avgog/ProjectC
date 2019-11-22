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
import androidx.fragment.app.DialogFragment;

import android.text.InputFilter;
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
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;

public class RoutePage extends AppCompatActivity implements  TimePickerFragment.TimePickerListener , DatePickerDialog.OnDateSetListener {

    ListView listView;
    ArrayList<String> mTime = new ArrayList<>();
    ArrayList<String> mDate = new ArrayList<>();
    String activity;
    TextView editTextView;
    AlertDialog alertDialog;
    EditText editText;
    Calendar calender;
    static String date;
    static String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_route_page);
        Button AgendaButton = findViewById(R.id.agenda);
        final ToggleButton Monday = (findViewById(R.id.mondaybutton));
        final ToggleButton Tuesday = (findViewById(R.id.tuesdaybutton));
        final ToggleButton Wednesday = (findViewById(R.id.wednesdaybutton));
        final ToggleButton Thursday = (findViewById(R.id.thursdaybutton));
        final ToggleButton Friday = (findViewById(R.id.fridaybutton));
        final ToggleButton Saturday = (findViewById(R.id.saterdaybutton));
        final ToggleButton Sunday = (findViewById(R.id.sundaybutton));
        listView = findViewById(R.id.listview2);

        Button closeButton = findViewById(R.id.CloseButton);
        editTextView = findViewById(R.id.routeTitle);
        alertDialog = new AlertDialog.Builder(this).create();
        editText = new EditText(this);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
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
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String time = (hour + ":" + minute);
        mTime.add(time);
        mDate.add(RoutePage.date);
        MyAdapter adapter = new MyAdapter(RoutePage.this,mTime,mDate);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        RoutePage.date = String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" +String.valueOf(year);
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setCancelable(false);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }


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
                    mTime.remove(position);
                    mDate.remove(position);
                    MyAdapter adapter = new MyAdapter(RoutePage.this,mTime,mDate);
                    listView.setAdapter(adapter);
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

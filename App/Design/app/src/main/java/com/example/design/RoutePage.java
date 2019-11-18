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
import android.widget.Toast;
import android.widget.ToggleButton;

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

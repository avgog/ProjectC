package nl.hr.ov_notifier;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import nl.hr.ov_notifier.R;

public class RoutePage extends AppCompatActivity {

    ListView listView;
    String mTime[] = {"9:00","16:00","21:30"};
    String mDate[] = {"26-04-2019","Friday","Wednesday"};
    String timeSchemes[]
    Calendar calender;
    String date;
    DatePickerDialog datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_route_page);
        Button AgendaButton = findViewById(R.id.agenda);
        listView = findViewById(R.id.listview2);

        MyAdapter adapter = new MyAdapter(this, mTime, mDate);
        listView.setAdapter(adapter);

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
    }




    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTime[];
        String rDate[];


        MyAdapter(Context c, String time[], String date[]) {
            super(c, R.layout.rowroutepage, R.id.textView3, date);
            this.context = c;
            this.rDate = date;
            this.rTime = time;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.rowroutepage, parent, false);
            TextView myDate = row.findViewById(R.id.textView3);
            TextView myTime = row.findViewById(R.id.textView6);
            myDate.setText(rDate[position]);
            myTime.setText(rTime[position]);
            return row;
        }
    }

}

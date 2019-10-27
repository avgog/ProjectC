package nl.hr.ov_notifier;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import nl.hr.ov_notifier.R;

public class RoutePage extends AppCompatActivity {

    ListView listView;
    String mTime[] = {"9:00","16:00","21:30"};
    String mDate[] = {"26-04-2019","Friday","Wednesday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_route_page);

        listView = findViewById(R.id.listview2);

        MyAdapter adapter = new MyAdapter(this, mTime, mDate);
        listView.setAdapter(adapter);
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

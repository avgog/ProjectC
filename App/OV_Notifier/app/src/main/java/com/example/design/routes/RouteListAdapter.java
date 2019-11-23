package com.example.design.routes;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.design.R;

public class RouteListAdapter extends ArrayAdapter<Route> {
    Context context;
    Route routes[];


    public RouteListAdapter (Context c, Route routes[]) {
        super(c, R.layout.row, R.id.textView1,routes);
        this.context = c;
        this.routes = routes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
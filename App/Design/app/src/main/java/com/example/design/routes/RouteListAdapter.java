package com.example.design.routes;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.design.R;
import com.example.design.RoutePage;
import com.example.design.routes.OnRemoveRouteClickListener;
import com.example.design.routes.Route;
import com.example.design.routes.RouteManager;

public class RouteListAdapter extends ArrayAdapter<Route> {
    private Context context;
    private Route routes[];
    private RouteManager manager;
    private ListView listView;

    public RouteListAdapter (Context context, Route routes[], RouteManager manager, ListView listView) {
        super(context, R.layout.row, R.id.textView1,routes);
        this.context = context;
        this.routes = routes;
        this.manager = manager;
        this.listView = listView;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);
        final ToggleButton bellbutton = row.findViewById(R.id.notificationbutton);
        Button removeRouteButton = row.findViewById(R.id.deleteRouteButton);
        removeRouteButton.setOnClickListener(new OnRemoveRouteClickListener(manager, listView, routes[position]));

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
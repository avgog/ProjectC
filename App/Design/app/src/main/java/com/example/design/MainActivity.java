package com.example.design;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
    Button profileButton;
    ListView listView;
    String mTitle[] = {"Work","School","Home","Route1","Route2","Route3","Route4","Route5","Route6"};
    int images[] = {R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.mainlistview);
        profileButton = (findViewById(R.id.profileButton));
        MyAdapter adapter = new MyAdapter(this, mTitle, images);
        listView.setAdapter(adapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), RoutePage.class);
                startActivity(intent);

            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Settings.class));
            }
        });


    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(MainActivity.this, Home.class));
                    break;
            }
            return true;
        }
    };
    @Override
    public void onBackPressed(){
        startActivity(new Intent(MainActivity.this, Home.class));
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTitle[];
        int rImgs[];


        MyAdapter (Context c, String title[], int imgs[]) {
            super(c, R.layout.row, R.id.textView1,title);
            this.context = c;
            this.rTitle = title;
            this.rImgs = imgs;

        }



        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            return row;
        }


            }
        }


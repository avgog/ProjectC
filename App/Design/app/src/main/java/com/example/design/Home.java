package com.example.design;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    Button profileButton;
    ListView listView;
    String mTitle[] = {"Work"};
    int mImgs = R.drawable.pointer;
    String mDate[] = {"26/04/2019"};
    String mTime[] = {"9:00"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listView = findViewById(R.id.homeListview);
        MyAdapter adapter = new MyAdapter(this, mTitle, mImgs, mTime, mDate);
        listView.setAdapter(adapter);
        profileButton = (findViewById(R.id.profileButton));
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
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
                startActivity(new Intent(Home.this, Settings.class));
            }

        });
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(Home.this, Home.class));
        Toast toast = Toast.makeText(this, "test",Toast.LENGTH_LONG);
        toast.show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_routes:
                            startActivity(new Intent(Home.this, MainActivity.class));
                            break;
                    }
                    return true;
                }
            };

            class MyAdapter extends ArrayAdapter<String> {
                Context context;
                String rTitle[];
                int rImgs;
                String rTime[];
                String rDate[];


                MyAdapter(Context c, String title[], int imgs, String time[], String date[]) {
                    super(c, R.layout.row, R.id.textView1, title);
                    this.context = c;
                    this.rTitle = title;
                    this.rImgs = imgs;
                    this.rTime = time;
                    this.rDate = date;

                }

                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View row = layoutInflater.inflate(R.layout.rowhome, parent, false);
                    ImageView images = row.findViewById(R.id.iconImage);
                    TextView myTitle = row.findViewById(R.id.homeTitle);
                    TextView myTime = row.findViewById(R.id.homeTime);
                    TextView myDate = row.findViewById(R.id.homeDate);
                    myTitle.setTypeface(Typeface.DEFAULT_BOLD);
                    myTitle.setTextSize(19);
                    images.setImageResource(rImgs);
                    myTitle.setText(rTitle[position]);
                    myTime.setText(rTime[position]);
                    myDate.setText(rDate[position]);
                    return row;
                }
            }
        }




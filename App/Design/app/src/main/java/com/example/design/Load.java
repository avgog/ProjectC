package com.example.design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Load extends AppCompatActivity {

    static List<String> stops;
    int val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        val = getIntent().getIntExtra("VALUE", 1);

        new fetchData() {
            @Override
            protected void onPostExecute(List<String> list) {
                //               Log.i(TAG, "Items = "+ list.size());
                stops = (ArrayList<String>) list;
                System.out.println("test");
                startActivity(new Intent(Load.this, Search.class ).putStringArrayListExtra("lists", (ArrayList<String>) stops).putExtra("VALUE", val));
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(search.this, android.R.layout.simple_list_item_1, list);
                //((ListView)list1).setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }
        }.execute();
    }
}

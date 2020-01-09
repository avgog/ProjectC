package com.example.design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class Search extends AppCompatActivity {

    ArrayList<String> test;
    EditText etSearchbox;
    ListView lvFirst;
    ArrayAdapter<String> adapter1;
    // List view
    private ListView lv;
    int val;


    ArrayAdapter<String> adapter;

    // Search EditText
    EditText inputSearch;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        test = getIntent().getStringArrayListExtra("lists");
        val = getIntent().getIntExtra("VALUE", 1);


        lv = (ListView) findViewById(R.id.lvFirst);
        inputSearch = (EditText) findViewById(R.id.etSearchbox);

        // Adding items to listview
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, test);
        lv.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Search.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String stop = adapter.getItem(position);
                if (val == 1) {
                    startActivity(new Intent(Search.this, RoutePage.class).putExtra("locationString", stop));
                }
                if (val == 2) {
                    startActivity(new Intent(Search.this, RoutePage.class).putExtra("destinationString", stop));
                }
            }
        });
    }
}


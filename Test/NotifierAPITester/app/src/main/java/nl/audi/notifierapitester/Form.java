package nl.audi.notifierapitester;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static nl.audi.notifierapitester.MainActivity.serverUrlField;

public class Form {
    String header;
    String localUrl;
    String[] fieldnames;
    public int method = Request.Method.POST; //post = 1, //get = 0

    private TextView resultText;
    private EditText[] fields;


    public Form(String header, String localUrl, String[] fieldnames){
        this.header = header;
        this.localUrl = localUrl;
        this.fieldnames = fieldnames;
    }

    public void pasteFormToLayout(Context context, LinearLayout parent, int color){
        LinearLayout formContent = new LinearLayout(context);
        formContent.setBackgroundColor(color);
        formContent.setOrientation(LinearLayout.VERTICAL);

        TextView headerText = new TextView(context);
        headerText.setText(header);
        headerText.setTextSize(24.0f);
        formContent.addView(headerText);

        TextView[] fieldnameTexts = new TextView[fieldnames.length];
        fields = new EditText[fieldnames.length];


        for(int i = 0; i < fieldnames.length; i++){
            fieldnameTexts[i] = new TextView(context);
            fieldnameTexts[i].setText(fieldnames[i]);
            formContent.addView(fieldnameTexts[i]);

            fields[i] = new EditText(context);
            formContent.addView(fields[i]);
        }

        Button submit = new Button(context);
        submit.setText("Submit");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestFromServer();
            }
        });

        resultText = new TextView(context);
        resultText.setText("Result: -");
        formContent.addView(resultText);

        formContent.addView(submit);
        parent.addView(formContent);
    }

    public void requestFromServer(){
        String url = serverUrlField.getText()+localUrl;
        RequestQueue queue = MainActivity.queue;

        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Request","Got a response");
                resultText.setText("Result: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Request", "Response error");
                resultText.setText("Error: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Log.i("Request", "Mapping values");

                Map<String, String> postParams = new HashMap<String, String>();
                for(int i = 0; i < fieldnames.length; i++){
                    Log.i("Request", fieldnames[i] + " " + fields[i].getText().toString());
                    postParams.put(fieldnames[i],fields[i].getText().toString());
                }
                return postParams;
            }
        };
        queue.add(request);
    }
}

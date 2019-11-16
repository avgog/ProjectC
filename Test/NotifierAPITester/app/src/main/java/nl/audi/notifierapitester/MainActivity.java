package nl.audi.notifierapitester;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    public static EditText serverUrlField;
    public static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        serverUrlField = findViewById(R.id.urlField);
        Form[] forms = {
                new Form("test connection", "/",new String[]{}),
                new Form("add route", "/routes/add", new String[]{"user_id","start_point","end_point","route_name"}),
                new Form("get route by route id", "/routes/get/from_id", new String[]{"route_id"}),
                new Form("get routes by user id", "/routes/get/from_user", new String[]{"user_id"}),
                new Form("change start point", "/routes/change/start_point", new String[]{"route_id","start_point"}),
                new Form("change end point", "/routes/change/end_point", new String[]{"route_id","end_point"}),
                new Form("change route name", "/routes/change/route_name", new String[]{"route_id","route_name"}),
                new Form("remove route", "/routes/remove", new String[]{"route_id"}),
                new Form("get time by id","/times/get/from_id", new String[]{"time_id"}),
                new Form("get times by route","/times/get/from_route", new String[]{"route_id"}),
                new Form("add time","/times/add", new String[]{"route_id","end_time"}),
                new Form("change time","/times/change/time", new String[]{"time_id","end_time"}),
                new Form("remove time","/times/remove", new String[]{"time_id"})
        };
        forms[0].method = Request.Method.GET;

        LinearLayout layout = findViewById(R.id.content);


        for(int i = 0; i < forms.length; i++){
            forms[i].pasteFormToLayout(this, layout, (i%2==0)?0xffffffff:0xfff0f0f0);
        }
    }
}

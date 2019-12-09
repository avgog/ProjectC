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
    public static Form[] forms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        serverUrlField = findViewById(R.id.urlField);
        forms = new Form[]{
                new Form("test connection",             "/",new String[]{}),
                new Form("get authentication token",    "/public/auth", new String[]{"username","password"}),
                new Form("change user data",            "/public/user/change", new String[]{"token","user_id","username","password"}),
                new Form("add route",                   "/public/routes/add", new String[]{"token","user_id","start_point","end_point","route_name"}),
                new Form("get route by route id",       "/public/routes/get/from_id", new String[]{"token","user_id","route_id"}),
                new Form("get routes by user id",       "/public/routes/get/from_user", new String[]{"token","user_id"}),
                new Form("change start point",          "/public/routes/change/start_point", new String[]{"token","user_id","route_id","start_point"}),
                new Form("change end point",            "/public/routes/change/end_point", new String[]{"token","user_id","route_id","end_point"}),
                new Form("change route name",           "/public/routes/change/route_name", new String[]{"token","user_id","route_id","route_name"}),
                new Form("remove route",                "/public/routes/remove", new String[]{"token","user_id","route_id"}),
                new Form("get time by id",              "/public/times/get/from_id", new String[]{"token","user_id","time_id"}),
                new Form("get times by route",          "/public/times/get/from_route", new String[]{"token","user_id","route_id"}),
                new Form("add time",                    "/public/times/add", new String[]{"token","user_id","route_id","end_time","date"}),
                new Form("change time",                 "/public/times/change/time", new String[]{"token","user_id","time_id","date","end_time"}),
                new Form("remove time",                 "/public/times/remove", new String[]{"token","user_id","time_id"})
        };
        forms[0].method = Request.Method.GET;
        forms[1].isAuthenticationForm = true;
        
        LinearLayout layout = findViewById(R.id.content);


        for(int i = 0; i < forms.length; i++){
            forms[i].pasteFormToLayout(this, layout, (i%2==0)?0xffffffff:0xfff0f0f0);
        }
    }

    public static void updateTokens(String token){
        for(int i = 0; i < forms.length; i++){
            for(int j = 0; j < forms[i].fields.length; j++){
                if(forms[i].fieldnames[j] == "token"){
                    forms[i].fields[j].setText(token);
                }
            }
        }
    }
}

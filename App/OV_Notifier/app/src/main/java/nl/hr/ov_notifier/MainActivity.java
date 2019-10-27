package nl.hr.ov_notifier;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nl.hr.ov_notifier.R;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    String mTitle[] = {"Work","School","Home","Route1","Route2","Route3","Route4","Route5","Route6"};
    int images[] = {R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer,R.drawable.pointer};
    int images2[] = {R.drawable.bell, R.drawable.emptybell, R.drawable.emptybell,R.drawable.emptybell, R.drawable.emptybell,R.drawable.emptybell, R.drawable.emptybell,R.drawable.emptybell, R.drawable.emptybell};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.ListView1);

        MyAdapter adapter = new MyAdapter(this, mTitle, images,images2);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), RoutePage.class);
                startActivity(intent);
            }
        });
    }
    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTitle[];
        int rImgs[];
        int rImgs2[];

        MyAdapter (Context c, String title[], int imgs[], int imgs2[]) {
            super(c, R.layout.row, R.id.textView1,title);
            this.context = c;
            this.rTitle = title;
            this.rImgs = imgs;
            this.rImgs2 = imgs2;

        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            ImageView images2 = row.findViewById(R.id.image2);
            TextView myTitle = row.findViewById(R.id.textView1);
            myTitle.setTypeface(Typeface.DEFAULT_BOLD);
            myTitle.setTextSize(19);
            images.setImageResource(rImgs[position]);
            images2.setImageResource(rImgs2[position]);
            myTitle.setText(rTitle[position]);
            return row;
        }
    }
}

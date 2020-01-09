package com.example.design;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class fetchData extends AsyncTask<Void, Void, List<String>> {
    String data = "";
    String Parsed = "";
    static ArrayList<String> stops1;

    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> str = new ArrayList<String>();
        try {
            URL url = new URL("http://projectc.caslayoort.nl/public/all_stops");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
            fetchData.stops1 = new ArrayList<String>();
            JSONArray JA = new JSONArray(data);
            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                Parsed = JO.get("name") + ", " + JO.get("town");
                str.add(Parsed);
                Log.i(TAG, "Added to list: "+ str.get(i));
            }
            return str;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

}

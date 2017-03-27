package com.example.siddhant.pricyfy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.siddhant.pricyfy.R.id.tv;

public class splashScreen extends AppCompatActivity {

    static public Request request;
    public class links
    {
        String product_name, product_link,image_link;
    }
    ArrayList<links> linksArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView tv = (TextView)findViewById(R.id.tv);
        if(!checkConnection())
        {
            tv.setText("No internet connection available");
            tv.setBackgroundColor(Color.RED);
        }
        else new PrefetchData().execute();
    }

    public boolean checkConnection()
    {
        boolean connected=false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        return connected;
    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            /*
             * Will make http call here This call will download required data
             * before launching the app
             * example:
             * 1. Downloading and storing in SQLite
             * 2. Downloading images
             * 3. Fetching and parsing the xml / json
             * 4. Sending device information to server
             * 5. etc.,
             */
            Request request = new Request.Builder()
                    .url("https://pricyfy.com/app_all_deals.php?id=0")
                    .build();
            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();

                JSONArray array = new JSONArray(response.body().string());

                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);
                    links l = new links();
                    l.product_name=object.getString("product_name");
                    l.product_link=object.getString("product_link");
                    l.image_link=object.getString("image_link");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and lauch main activity
            Intent i = new Intent(splashScreen.this, homeScreen.class);
            i.putExtra("linksArray",linksArr);
            startActivity(i);
            finish();
        }

    }
}

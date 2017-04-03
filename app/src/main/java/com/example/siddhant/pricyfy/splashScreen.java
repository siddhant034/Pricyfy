package com.example.siddhant.pricyfy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.siddhant.pricyfy.R.id.toolbar;

public class splashScreen extends AppCompatActivity {

    private ArrayList<Deal> deals;
    private ArrayList<String> coupon_images;
    private ArrayList<String> coupon_title;
    private ArrayList<Product> mobiles;
    private ArrayList<Product> cameras;
    private ArrayList<Product> men;
    private ArrayList<Product> women;
    private ArrayList<Product> footwear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView tv = (TextView)findViewById(R.id.tv);
        if(!checkConnection())
        {
            tv.setText(R.string.no_internet);
            tv.setBackgroundColor(Color.RED);
        }
        else new PrefetchData().execute();
        deals = new ArrayList<>();
        coupon_images = new ArrayList<>();
        coupon_title = new ArrayList<>();
        mobiles = new ArrayList<>();
        cameras = new ArrayList<>();
        men = new ArrayList<>();
        women = new ArrayList<>();
        footwear = new ArrayList<>();



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
                    .url("https://pricyfy.com/app_splash_screen.php")
                    .build();
            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray array = jsonObject.getJSONArray("6");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject object = array.getJSONObject(i);
                    coupon_title.add(object.getString("title"));
                    coupon_images.add(object.getString("image_link"));
                }
                for(int i=0;i<=5;i++)
                {
                    Log.i("icici = ",Integer.toString(i));
                    if(!jsonObject.has(Integer.toString(i))) continue;
                    array = jsonObject.getJSONArray(Integer.toString(i));
                    Log.i("array Length = ",Integer.toString(array.length()));
                    Log.i("iiii=", Integer.toString(i));
                    for(int j=0;j<array.length();j++)
                    {
                        Product newProduct=null;
                        JSONObject object = array.getJSONObject(j);
                        if(i!=5)
                          newProduct = new Product(object.getString("product_name"),object.getInt("mrp"),object.getString("image_link"));
                        switch(i)
                        {
                            case 0 : mobiles.add(newProduct);break;
                            case 1 : cameras.add(newProduct);break;
                            case 2 : men.add(newProduct);break;
                            case 3 : women.add(newProduct);break;
                            case 4 : footwear.add(newProduct); break;
                            case 5 : Deal d = new Deal(1,object.getString("product_name"),object.getString("image_link"),object.getString("product_link"));
                                     deals.add(d);break;
                        }
                        Log.i("mobile.length = " ,Integer.toString(mobiles.size()));
                        Log.i("jjjjj = ",Integer.toString(j));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Bundle b = new Bundle();
            Intent i = new Intent(getApplicationContext(),homeScreen.class);
            b.putParcelableArrayList("deals",deals);
            b.putStringArrayList("coupon_title",coupon_title);
            b.putStringArrayList("coupon_images",coupon_images);
            b.putParcelableArrayList("mobiles",mobiles);
            b.putParcelableArrayList("cameras",cameras);
            b.putParcelableArrayList("men",men);
            b.putParcelableArrayList("women",women);
            b.putParcelableArrayList("footwear",footwear);
            i.putExtras(b);
            startActivity(i);
            finish();
        }
    }
}

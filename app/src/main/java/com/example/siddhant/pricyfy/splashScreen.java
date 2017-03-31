package com.example.siddhant.pricyfy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private ArrayList<String> deals_name;
    private ArrayList<String> deals_links;
    private ArrayList<String> deals_images_links;
    private ArrayList<String> coupon_images;
    private ArrayList<String> coupon_title;
    private ArrayList<String> mobile_name, mobile_mrp, mobile_image;
    private ArrayList<String> camera_name, camera_mrp, camera_image;
    private ArrayList<String> men_name, men_mrp, men_image;
    private ArrayList<String> women_name, women_mrp, women_image;
    private ArrayList<String> footwear_name, footwear_mrp, footwear_image;

    public class slider_images
    {
        String image_link, url_link;
    }

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
        deals_name = new ArrayList<>();
        deals_images_links = new ArrayList<>();
        deals_links = new ArrayList<>();
        coupon_images = new ArrayList<>();
        coupon_title = new ArrayList<>();
        mobile_image = new ArrayList<>();
        mobile_mrp = new ArrayList<>();
        mobile_name = new ArrayList<>();
        camera_image = new ArrayList<>();
        camera_mrp = new ArrayList<>();
        camera_name = new ArrayList<>();
        men_image = new ArrayList<>();
        men_mrp = new ArrayList<>();
        men_name = new ArrayList<>();
        women_image = new ArrayList<>();
        women_mrp = new ArrayList<>();
        women_name = new ArrayList<>();
        footwear_image = new ArrayList<>();
        footwear_mrp = new ArrayList<>();
        footwear_name = new ArrayList<>();
//        getSupportActionBar().hide();

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
            Request request1 = new Request.Builder()
                    .url("https://pricyfy.com/app_all_coupons.php?id=0")
                    .build();
            Request request2 = new Request.Builder()
                    .url("https://pricyfy.com/app_all_products.php?id=34&type=mobiles")
                    .build();
            Request request3 = new Request.Builder()
                    .url("https://pricyfy.com/app_all_products.php?id=34&type=cameras")
                    .build();
            Request request4 = new Request.Builder()
                    .url("https://pricyfy.com/app_all_products.php?id=34&type=men")
                    .build();
            Request request5 = new Request.Builder()
                    .url("https://pricyfy.com/app_all_products.php?id=34&type=women")
                    .build();
            Request request6 = new Request.Builder()
                    .url("https://pricyfy.com/app_all_products.php?id=34&type=footwear")
                    .build();
            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();
                Response response1 = client.newCall(request1).execute();
                Response response2 = client.newCall(request2).execute();
                Response response3 = client.newCall(request3).execute();
                Response response4 = client.newCall(request4).execute();
                Response response5 = client.newCall(request5).execute();
                Response response6 = client.newCall(request6).execute();
                JSONArray array = new JSONArray(response.body().string());
                JSONArray array1 = new JSONArray(response1.body().string());
                JSONArray array2 = new JSONArray(response2.body().string());
                JSONArray array3 = new JSONArray(response3.body().string());
                JSONArray array4 = new JSONArray(response4.body().string());
                JSONArray array5 = new JSONArray(response5.body().string());
                JSONArray array6 = new JSONArray(response6.body().string());
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject object = array.getJSONObject(i);
                    deals_name.add(object.getString("product_name"));
                    deals_links.add(object.getString("product_link"));
                    deals_images_links.add(object.getString("image_link"));
                }
                for (int i = 0; i < array1.length(); i++)
                {
                    JSONObject object = array1.getJSONObject(i);
                    coupon_title.add(object.getString("title"));
                    coupon_images.add(object.getString("image_link"));
                }
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject object = array.getJSONObject(i);
                    deals_name.add(object.getString("product_name"));
                    deals_links.add(object.getString("product_link"));
                    deals_images_links.add(object.getString("image_link"));
                }
                for (int i = 0; i < array2.length(); i++)
                {
                    JSONObject object = array2.getJSONObject(i);
                    mobile_mrp.add(object.getString("mrp"));
                    mobile_image.add(object.getString("image_link"));
                    mobile_name.add(object.getString("product_name"));
                }
                for (int i = 0; i < array3.length(); i++)
                {
                    JSONObject object = array3.getJSONObject(i);
                    camera_mrp.add(object.getString("mrp"));
                    camera_image.add(object.getString("image_link"));
                    camera_name.add(object.getString("product_name"));
                }
                for (int i = 0; i < array4.length(); i++)
                {
                    JSONObject object = array3.getJSONObject(i);
                    men_mrp.add(object.getString("mrp"));
                    men_image.add(object.getString("image_link"));
                    men_name.add(object.getString("product_name"));
                }
                for (int i = 0; i < array5.length(); i++)
                {
                    JSONObject object = array3.getJSONObject(i);
                    women_mrp.add(object.getString("mrp"));
                    women_image.add(object.getString("image_link"));
                    women_name.add(object.getString("product_name"));
                }
                for (int i = 0; i < array6.length(); i++)
                {
                    JSONObject object = array3.getJSONObject(i);
                    footwear_mrp.add(object.getString("mrp"));
                    footwear_image.add(object.getString("image_link"));
                    footwear_name.add(object.getString("product_name"));
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
            Bundle b = new Bundle();
            b.putStringArrayList("deals_names",deals_name);
            b.putStringArrayList("deals_links",deals_links);
            b.putStringArrayList("deals_images_links",deals_images_links);
            b.putStringArrayList("coupon_title",coupon_title);
            b.putStringArrayList("coupon_images",coupon_images);
            b.putStringArrayList("mobile_names",mobile_name);
            b.putStringArrayList("mobile_images",mobile_image);
            b.putStringArrayList("mobile_mrp",mobile_mrp);
            b.putStringArrayList("camera_names",camera_name);
            b.putStringArrayList("camera_images",camera_image);
            b.putStringArrayList("camera_mrp",camera_mrp);
            b.putStringArrayList("men_names",men_name);
            b.putStringArrayList("men_images",men_image);
            b.putStringArrayList("men_mrp",men_mrp);
            b.putStringArrayList("women_names",women_name);
            b.putStringArrayList("women_images",women_image);
            b.putStringArrayList("women_mrp",women_mrp);
            b.putStringArrayList("footwear_names",footwear_name);
            b.putStringArrayList("footwear_images",footwear_image);
            b.putStringArrayList("footwear_mrp",footwear_mrp);
            Intent i = new Intent(splashScreen.this, homeScreen.class);
            i.putExtras(b);
            startActivity(i);
            finish();
        }
    }
}

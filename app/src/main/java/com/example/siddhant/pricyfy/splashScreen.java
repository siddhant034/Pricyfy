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
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient.Builder;

import static com.example.siddhant.pricyfy.R.id.split_action_bar;
import static com.example.siddhant.pricyfy.R.id.toolbar;

public class splashScreen extends AppCompatActivity {

    private ArrayList<Deal> deals;
    private ArrayList<String> coupon_images, coupon_title, slider_images, slider_links, coupon_links, ad_banners, ad_banner_links;
    private ArrayList<Product> mobiles, cameras, men, women, footwear;
    private int temp = 1;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
       // Toast.makeText(getApplicationContext(),Boolean.toString(i.getExtras()==null),Toast.LENGTH_LONG).show();
        if(i.getExtras()==null) {
            setContentView(R.layout.activity_splash_screen);
        }
        else
        {
            Bundle b = getIntent().getExtras();
            if(!b.containsKey("abc"))
                setContentView(R.layout.activity_splash_screen);
            else
            {
                setContentView(R.layout.activity_splash_screen2);
                spinner = (ProgressBar) findViewById(R.id.progressBar1);
                spinner.getIndeterminateDrawable().setColorFilter(spinner.getResources().getColor(R.color.pricify_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                temp = 2;
            }
        }
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
        slider_links = new ArrayList<>();
        slider_images = new ArrayList<>();
        ad_banner_links = new ArrayList<>();
        ad_banners = new ArrayList<>();
        coupon_links = new ArrayList<>();
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

        String jsonStr;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "https://pricyfy.com/app_splash_screen.php";
            jsonStr = sh.makeServiceCall(url);
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray array = jsonObject.getJSONArray("6");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject object = array.getJSONObject(i);
                    coupon_title.add(object.getString("title"));
                    coupon_images.add(object.getString("image_link"));
                    coupon_links.add(object.getString("landing_page"));
                }
                coupon_images.set(2,coupon_images.get(3));
                coupon_title.set(2,coupon_title.get(3));
                coupon_links.set(2,coupon_title.get(3));

               array = jsonObject.getJSONArray("8");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject object = array.getJSONObject(i);
                    slider_images.add(object.getString("slider_image_link"));
                    slider_links.add(object.getString("redirect_link"));
                }

                array = jsonObject.getJSONArray("ad_banner");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject object = array.getJSONObject(i);
                    ad_banners.add(object.getString("ad_banner_link"));
                    ad_banner_links.add(object.getString("redirect_link"));
                }

                for(int i=0;i<=5;i++)
                {
                    if(!jsonObject.has(Integer.toString(i))) continue;
                    array = jsonObject.getJSONArray(Integer.toString(i));
                    for(int j=0;j<array.length();j++)
                    {
                        Product newProduct = null;
                        JSONObject object = array.getJSONObject(j);
                        if(i!=5) newProduct = new Product(object.getInt("id"),object.getString("product_name"),object.getInt("flipkart_price"),object.getString("image_link"),object.getInt("discount"),object.getInt("mrp"),object.getDouble("seller_rating"));
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
                    }
                }

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
            if(jsonStr.isEmpty())
            {
                Intent i = new Intent(getApplicationContext(),splashScreen.class);
                startActivity(i);finish();
            }
            Bundle b = new Bundle();
            Intent i = new Intent(getApplicationContext(),homeScreen.class);
            b.putParcelableArrayList("deals",deals);
            b.putStringArrayList("coupon_title",coupon_title);
            b.putStringArrayList("coupon_images",coupon_images);
            b.putStringArrayList("coupon_links",coupon_links);
            b.putStringArrayList("slider_links",slider_links);
            b.putStringArrayList("slider_images",slider_images);
            b.putStringArrayList("ad_banners",ad_banners);
            b.putStringArrayList("ad_banner_links",ad_banner_links);
            b.putParcelableArrayList("mobiles",mobiles);
            b.putParcelableArrayList("cameras",cameras);
            b.putParcelableArrayList("men",men);
            b.putParcelableArrayList("women",women);
            b.putParcelableArrayList("footwear",footwear);
            i.putExtras(b);
            startActivity(i);finish();
            if(temp==2) {
                spinner.setVisibility(View.INVISIBLE);
            }
            finish();
        }
    }
}

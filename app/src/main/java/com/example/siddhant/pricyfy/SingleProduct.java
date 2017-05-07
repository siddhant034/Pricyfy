package com.example.siddhant.pricyfy;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class SingleProduct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog progressDialog;
    private int id;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_parent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Intent i = getIntent();
        id = i.getIntExtra("id",1041930);

//        id=1041930;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        new SingleProduct.PrefetchData().execute();
        navigationView.setItemIconTintList(null);
        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCES", MODE_PRIVATE);
        Boolean isSignedInGoogle = sharedPreferences.getBoolean("signedInGoogle",false);
        Boolean isSignedInFb = sharedPreferences.getBoolean("signedInFb",false);
        if(isSignedInGoogle)
        {
            View navHeaderView = navigationView.getHeaderView(0);
            String name = sharedPreferences.getString("name","No name defined");
            String profilePicture = sharedPreferences.getString("profilePicture",null);
            TextView tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText(name);
            tv.setClickable(false);
            tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.GONE);
            ImageView iv = (ImageView)navHeaderView.findViewById(R.id.profile_image);
            Picasso.with(getApplicationContext()).load(profilePicture).into(iv);
        }
        else if(isSignedInFb)
        {
            View navHeaderView = navigationView.getHeaderView(0);
            String name = sharedPreferences.getString("name","No name defined");
            String userId = sharedPreferences.getString("profilePicture",null);
            URL imageURL = null;
            try {
                imageURL = new URL("https://graph.facebook.com/" + userId + "/picture?type=large");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //getProfilePic(userId);
            TextView tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText(name);
            tv.setClickable(false);
            tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.GONE);
            //  iv = (ImageView)navHeaderView.findViewById(R.id.imageView);
            //  iv.setImageBitmap(profilePic);
        }
        else
        {
            View navHeaderView = navigationView.getHeaderView(0);
            TextView tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.VISIBLE);
            tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText("Sign in");
            tv.setClickable(true);
        }
        ImageView iv = (ImageView)findViewById(R.id.iv_company_logo);
        Picasso.with(getApplicationContext()).load(R.drawable.company_logo).into(iv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        ImageView searchClose = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(R.color.black_overlay);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home)
        {
            Intent i = new Intent(this,homeScreen.class);
            startActivity(i);finish();
        }
        else if(id == R.id.mobiles_and_tablets)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","mobiles");
            startActivity(i);finish();
        }
        else if (id == R.id.laptops)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","laptops");
            startActivity(i);finish();
        }
        else if (id == R.id.cameras)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","cameras");
            startActivity(i);finish();
        }
        else if (id == R.id.tv_and_entertainment)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","tv");
            startActivity(i);finish();
        }
        else if (id == R.id.home_and_kitchen_appliances)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","home");
            startActivity(i);finish();
        }
        else if (id == R.id.home_decors)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","home_decors");
            startActivity(i);finish();
        }
        else if (id == R.id.men)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","men");
            startActivity(i);finish();
        }
        else if (id == R.id.women)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","women");
            startActivity(i);finish();
        }
        else if (id == R.id.mens_footwear)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","mens_footwear");
            startActivity(i);finish();
        }
        else if (id == R.id.womens_footwear)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","womens_footwear");
            startActivity(i);finish();
        }
        else if (id == R.id.coupons_and_cashbacks)
        {
            Intent i = new Intent(this,CouponsPage2.class);
            startActivity(i);finish();
        }
        else if(id == R.id.deals)
        {
            Intent i = new Intent(this,DealsPage.class);
            startActivity(i);finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        return true;
    }

    private void setData()
    {
      //  Toast.makeText(getApplicationContext(),Boolean.toString(product==null),Toast.LENGTH_LONG).show();
        ImageView iv = (ImageView)findViewById(R.id.imageView_product);
        String il = null;
        il = product.getImageLink();
        if(il!=null)
            Picasso.with(getApplicationContext()).load(product.getImageLink()).into(iv);

        TextView tv = (TextView)findViewById(R.id.textView_product_name);
        tv.setText(product.getTitle());

        tv = (TextView)findViewById(R.id.textView_bestPrice);
        tv.setText("BEST PRICE");

        tv = (TextView)findViewById(R.id.textView_bestPrice2);
        tv.setText("Rs. "+Integer.toString(product.getLowestPrice()));

        Button b = (Button)findViewById(R.id.button_goToStorebestPrice);
        b.setVisibility(View.VISIBLE);
        b.setText("Go To Store");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = product.getFlipkart_link();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);finish();
            }
        });

        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setVisibility(View.VISIBLE);
        ratingBar.setRating(5);
        ratingBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

       // tv = (TextView)findViewById(R.id.textView_ratingsCount);
        //tv.setText("156");
        tv = (TextView)findViewById(R.id.textViewComparePrices);
        tv.setText("Other prices");

        tv = (TextView)findViewById(R.id.flipkart_best_price);
        tv.setText("Rs. "+ Integer.toString(product.getLowestPrice()));

        tv = (TextView)findViewById(R.id.amazon_best_price);
        tv.setText("NA");

        tv = (TextView)findViewById(R.id.ebay_best_price);
        tv.setText("NA");


     //   iv = (ImageView)findViewById(R.id.adBanner1);
       // iv.setImageResource(R.drawable.slider1);
        tv = (TextView)findViewById(R.id.desc1);
        tv.setText("Description");

        tv = (TextView)findViewById(R.id.desc2);
        tv.setText(product.getDescription());

        iv = (ImageView)findViewById(R.id.bestPriceIcon);
        iv.setVisibility(View.VISIBLE);

        tv = (TextView)findViewById(R.id.tv_key_features);
        String features = product.getFeatures();
        features.replaceAll(";","\n");
        tv.setText(features);

        tv = (TextView)findViewById(R.id.tv_key_features1);
        tv.setVisibility(View.VISIBLE);

        LinearLayout ll = (LinearLayout)findViewById(R.id.grid);
        ll.setVisibility(View.VISIBLE);

        ll = (LinearLayout)findViewById(R.id.ll_flipkart);
        ll.setVisibility(View.VISIBLE);

        ll = (LinearLayout)findViewById(R.id.ll_rating);
        ll.setVisibility(View.VISIBLE);

        ll = (LinearLayout)findViewById(R.id.ll_amazon);
        ll.setVisibility(View.VISIBLE);

        ll = (LinearLayout)findViewById(R.id.ll_ebay);
        ll.setVisibility(View.VISIBLE);

        ll = (LinearLayout)findViewById(R.id.ll_paytm);
        ll.setVisibility(View.VISIBLE);

        ll = (LinearLayout)findViewById(R.id.ll_infibeam);
        ll.setVisibility(View.VISIBLE);

        View hor_break = (View)findViewById(R.id.hor_break1);
        hor_break.setVisibility(View.VISIBLE);

        tv = (TextView)findViewById(R.id.textView_mrp);
        tv.setText("Rs. " + product.getMrp());
        tv.setPaintFlags(tv.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        ll = (LinearLayout)findViewById(R.id.ll_prices);
        ll.setVisibility(View.VISIBLE);

        tv = (TextView)findViewById(R.id.stock);
        tv.setText("Stock : ");

        iv = (ImageView)findViewById(R.id.stock_tick);
        if(product.getStock()=="YES") iv.setImageResource(R.drawable.tick);
        else iv.setImageResource(R.drawable.cross);

        tv = (TextView)findViewById(R.id.cod);
        tv.setText("COD : ");

        iv = (ImageView)findViewById(R.id.cod_tick);
        if(product.getCod()=="YES") iv.setImageResource(R.drawable.tick);
        else iv.setImageResource(R.drawable.cross);

        tv = (TextView)findViewById(R.id.shipping_charge);
        tv.setText("Shipping charge : " + Integer.toString(product.getShippingCharge()));

        tv = (TextView)findViewById(R.id.delivery);
        tv.setText("Delivery : " + product.getDelivery());

        Double rating = product.getSeller_rating();
        tv = (TextView)findViewById(R.id.seller_ratings);
        tv.setText(String.format("%.1f ",rating));
        tv = (TextView)findViewById(R.id.total_ratings);
        tv.setText("from " + Integer.toString(product.getTotal_ratings()) + " ratings | " + Integer.toString(product.getSeller_reviews()) + " reviews");

    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(SingleProduct.this);
            progressDialog.setMessage("Loading product...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            Request request = new Request.Builder()
                    .url("https://pricyfy.com/app_single_product.php?id="+id)
                    .build();
            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                product = new Product(jsonObject.getInt("id"),jsonObject.getString("product_name"),jsonObject.getString("description"),jsonObject.getInt("flipkart_price"),jsonObject.getString("image_link"),jsonObject.getDouble("seller_rating"),jsonObject.getInt("mrp"),jsonObject.getInt("discount"),jsonObject.getString("features"),jsonObject.getString("stock"),jsonObject.getString("cod"),jsonObject.getInt("shipping_charge"),jsonObject.getString("delivery"),jsonObject.getInt("total_rating"),jsonObject.getString("flipkart_link"),jsonObject.getInt("seller_reviews"));
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
            progressDialog.dismiss();
            setData();
        }
    }

    public void goToHomePage(View view) {
        Intent i = new Intent(this,splashScreen.class);
        i.putExtra("abc","abc");
        startActivity(i);finish();
    }

    public void goToDealsPage(View view)
    {
        Intent i = new Intent(this,DealsPage.class);
        startActivity(i);finish();
    }

    public void goToCouponsPage(View view)
    {
        Intent i = new Intent(this,CouponsPage2.class);
        startActivity(i);finish();
    }

    public void goToProfilePage(View view)
    {
        Intent i = new Intent(this,ProfilePage.class);
        startActivity(i);finish();
    }
    public void goToLoginPage(View view)
    {
        Intent i = new Intent(this,LoginPage.class);
        startActivity(i);
    }
}


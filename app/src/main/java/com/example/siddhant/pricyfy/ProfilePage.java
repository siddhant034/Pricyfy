package com.example.siddhant.pricyfy;

import java.net.URL;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.siddhant.pricyfy.R.id.indicator;

public class ProfilePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private GoogleApiClient mGoogleApiClient;
    SharedPreferences.Editor editor;
    private ArrayList<Deal> deals;
    private ArrayList<Product> products;
    private ArrayList<String> coupon_images, coupon_title, coupon_links;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCES", MODE_PRIVATE);
        Boolean isSignedInGoogle = sharedPreferences.getBoolean("signedInGoogle",false);
        Boolean isSignedInFb = sharedPreferences.getBoolean("signedInFb",false);
        if(!isSignedInFb && !isSignedInGoogle) {
            Intent i = new Intent(this,LoginPage.class);
            startActivity(i);
            finish();

        }
        setContentView(R.layout.activity_profile_page_parent2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ImageView iv = (ImageView)findViewById(R.id.iv_footer_profile);
        iv.setColorFilter(getResources().getColor(R.color.pricify_blue));
        TextView tv = (TextView)findViewById(R.id.tv_footer_profile);
        tv.setTextColor(getResources().getColor(R.color.pricify_blue));

        iv = (ImageView)findViewById(R.id.iv_company_logo);
        Picasso.with(getApplicationContext()).load(R.drawable.company_logo).into(iv);

        editor = sharedPreferences.edit();
        if(isSignedInGoogle)
        {
            String name = sharedPreferences.getString("name","No name defined");
            String profilePicture = sharedPreferences.getString("profilePicture",null);
            tv = (TextView)findViewById(R.id.tv2);
            tv.setText(name);
            tv.setClickable(false);
            iv = (ImageView)findViewById(R.id.profile_image);
            Picasso.with(getApplicationContext()).load(profilePicture).into(iv);
        }
        else if(isSignedInFb)
        {
            String name = sharedPreferences.getString("name","No name defined");
            tv = (TextView)findViewById(R.id.tv2);
            tv.setText(name);
            tv.setClickable(false);
            Bundle params = new Bundle();
            params.putString("fields", "id,email,gender,cover,picture.type(large)");
            new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                    new GraphRequest.Callback()
                    {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            if (response != null) {
                                try {
                                    JSONObject data = response.getJSONObject();
                                    if (data.has("picture")) {
                                        String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                        URL ss = new URL(profilePicUrl);
                                        Bitmap profilePic= BitmapFactory.decodeStream(ss.openConnection().getInputStream());
                                        View navHeaderView = navigationView.getHeaderView(0);
                                        ImageView iv = (ImageView)navHeaderView.findViewById(R.id.profile_image);
                                        iv.setImageBitmap(profilePic);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).executeAsync();
        }

        if(isSignedInGoogle)
        {
            View navHeaderView = navigationView.getHeaderView(0);
            String name = sharedPreferences.getString("name","No name defined");
            String profilePicture = sharedPreferences.getString("profilePicture",null);
            tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.GONE);
            tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText(name);
            tv.setClickable(false);
            iv = (ImageView)navHeaderView.findViewById(R.id.profile_image);
            Picasso.with(getApplicationContext()).load(profilePicture).into(iv);
        }
        else if(isSignedInFb)
        {
            View navHeaderView = navigationView.getHeaderView(0);
            String name = sharedPreferences.getString("name","No name defined");
            tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.GONE);
            tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText(name);
            tv.setClickable(false);
            Bundle params = new Bundle();
            params.putString("fields", "id,email,gender,cover,picture.type(large)");
            new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                    new GraphRequest.Callback()
                    {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            if (response != null) {
                                try {
                                    JSONObject data = response.getJSONObject();
                                    if (data.has("picture")) {
                                        String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                        URL ss = new URL(profilePicUrl);
                                        Bitmap profilePic= BitmapFactory.decodeStream(ss.openConnection().getInputStream());
                                        View navHeaderView = navigationView.getHeaderView(0);
                                        ImageView iv = (ImageView)navHeaderView.findViewById(R.id.profile_image);
                                        iv.setImageBitmap(profilePic);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).executeAsync();
        }
        else
        {
            View navHeaderView = navigationView.getHeaderView(0);
            tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.VISIBLE);
            tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText("Sign In / ");
            tv.setClickable(true);
        }
        deals = new ArrayList<>();
        coupon_links = new ArrayList<>();
        coupon_images = new ArrayList<>();
        coupon_title = new ArrayList<>();
        products = new ArrayList<>();
        if(isSignedInGoogle || isSignedInFb)
            new ProfilePage.PrefetchData().execute();
        if(!isSignedInFb && !isSignedInGoogle)
            LogoutButton();    }

    public void signOut(MenuItem view)
    {
        editor.putBoolean("signedInFb", false);
        editor.putBoolean("signedInGoogle",false);
        editor.commit();
        LoginManager.getInstance().logOut();
        Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,splashScreen.class);
        i.putExtra("abc","abc");
        startActivity(i);
        finish();
    }

    public void signOut(View view)
    {
        editor.putBoolean("signedInFb", false);
        editor.putBoolean("signedInGoogle",false);
        editor.commit();
        LoginManager.getInstance().logOut();
        Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,splashScreen.class);
        i.putExtra("abc","abc");
        LoginManager.getInstance().logOut();
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home_screen, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home)
        {
            onBackPressed();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        drawer.closeDrawers();
        return true;
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
    }

    public void goToLoginPage(View view)
    {
        Intent i = new Intent(this,LoginPage.class);
        startActivity(i);finish();
    }

    public void openDealsPage(View view)
    {
        Intent i = new Intent(this,DealsPage.class);
        startActivity(i);finish();
    }

    public void openCouponsPage(View view)
    {
        Intent i = new Intent(this,CouponsPage2.class);
        startActivity(i);finish();
    }

    public void setCouponsButtons()
    {
        LinearLayout ll = (LinearLayout)findViewById(R.id.coupon_ll1);
        ImageView iv = (ImageView)findViewById(R.id.iv1_coupons);
        TextView tv = (TextView)findViewById(R.id.tv1_coupons);
        Glide.with(getApplicationContext()).load(coupon_images.get(0)).into(iv);
        tv.setText(coupon_title.get(0));
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = coupon_links.get(0);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        iv = (ImageView)findViewById(R.id.iv3_coupons);
        tv = (TextView)findViewById(R.id.tv3_coupons);
        Glide.with(getApplicationContext()).load(coupon_images.get(2)).into(iv);
        tv.setText(coupon_title.get(2));
        ll = (LinearLayout)findViewById(R.id.coupon_ll2);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = coupon_links.get(2);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        iv = (ImageView)findViewById(R.id.iv2_coupons);
        tv = (TextView)findViewById(R.id.tv2_coupons);
        Glide.with(getApplicationContext()).load(coupon_images.get(1)).into(iv);
        tv.setText(coupon_title.get(1));
        ll = (LinearLayout)findViewById(R.id.coupon_ll2);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = coupon_links.get(0);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    public void setDealsButtons()
    {
        RecyclerView rv_deals = (RecyclerView) findViewById(R.id.rv_deals);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_deals.setLayoutManager(linearLayout);
        DealsAdapter dAdapter = new DealsAdapter(this,deals,1);
        rv_deals.setAdapter(dAdapter);
    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner = (ProgressBar)findViewById(R.id.progressBar1);
            spinner.getIndeterminateDrawable().setColorFilter(spinner.getResources().getColor(R.color.pricify_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "https://pricyfy.com/app_profile_page.php";
            String jsonStr = sh.makeServiceCall(url);
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray dealsArray = jsonObject.getJSONArray("deals");
                for(int i=0;i<dealsArray.length();i++)
                {
                    JSONObject object = dealsArray.getJSONObject(i);
                    Deal d = new Deal(1,object.getString("product_name"),object.getString("image_link"),object.getString("product_link"));
                    deals.add(d);
                }

                JSONArray productsJa = jsonObject.getJSONArray("products");
                for(int i=0;i<productsJa.length();i++)
                {
                    JSONObject object = productsJa.getJSONObject(i);
                    Product newProduct = new Product(object.getInt("id"),object.getString("product_name"),object.getInt("flipkart_price"),object.getString("image_link"),object.getInt("discount"),object.getInt("mrp"),object.getDouble("seller_rating"));
                    products.add(newProduct);
                }

                JSONArray couponsJA = jsonObject.getJSONArray("coupons");
                for(int i=0;i<couponsJA.length();i++)
                {
                    JSONObject object = couponsJA.getJSONObject(i);
                    coupon_title.add(object.getString("title"));
                    coupon_images.add(object.getString("image_link"));
                    coupon_links.add(object.getString("landing_page"));
                }
                coupon_images.set(2,coupon_images.get(3));
                coupon_title.set(2,coupon_title.get(3));
                coupon_links.set(2,coupon_title.get(3));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            spinner.setVisibility(View.INVISIBLE);
            setDealsButtons();
            setCouponsButtons();
        }
    }

    public void LogoutButton()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCES", MODE_PRIVATE);
        Boolean isSignedInGoogle = sharedPreferences.getBoolean("signedInGoogle",false);
        Boolean isSignedInFb = sharedPreferences.getBoolean("signedInFb",false);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        if(isSignedInFb || isSignedInGoogle)
        {
            navigationView.getMenu().getItem(R.id.logout).setVisible(true);
        }
    }

}

package com.example.siddhant.pricyfy;

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
import android.support.design.widget.NavigationView;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class CouponsPage2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private ArrayList<Coupons> coupons;
    private ViewPager viewPager;
    private CouponsPage2.MyViewPagerAdapter myViewPagerAdapter;
    private ProgressDialog progressDialog;
    private CircleIndicator indicator;
    private ArrayList<String> slider_images, slider_links;
    private RecyclerView recyclerView;
    private CouponsAdapter couponsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_page_parent2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        slider_links = new ArrayList<>();
        slider_images = new ArrayList<>();
        coupons = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview1);
        new PrefetchData().execute();
        navigationView.setItemIconTintList(null);

        ImageView iv = (ImageView)findViewById(R.id.iv_footer_coupons);
        iv.setColorFilter(getResources().getColor(R.color.pricify_blue));
        TextView tv = (TextView)findViewById(R.id.tv_footer_coupons);
        tv.setTextColor(getResources().getColor(R.color.pricify_blue));

        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCES", MODE_PRIVATE);
        Boolean isSignedInGoogle = sharedPreferences.getBoolean("signedInGoogle",false);
        Boolean isSignedInFb = sharedPreferences.getBoolean("signedInFb",false);
        if(isSignedInGoogle)
        {
            View navHeaderView = navigationView.getHeaderView(0);
            String name = sharedPreferences.getString("name","No name defined");
            String profilePicture = sharedPreferences.getString("profilePicture",null);
            tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText(name);
            tv.setClickable(false);
            tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.GONE);
            iv = (ImageView)navHeaderView.findViewById(R.id.profile_image);
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
            // getProfilePic(userId);
            tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText(name);
            tv.setClickable(false);
            tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.GONE);
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
        iv = (ImageView)findViewById(R.id.iv_company_logo);
        Picasso.with(getApplicationContext()).load(R.drawable.company_logo).into(iv);
        if(!isSignedInFb && !isSignedInGoogle)
            LogoutButton();
        navigationView.post(new Runnable() {
            @Override
            public void run() {
                navigationView.setCheckedItem(R.id.coupons_and_cashbacks);
            }
        });

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
        //searchView.setIconifiedByDefault(false);
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

    public void setLayouts()
    {
        FrameLayout fm = (FrameLayout)findViewById(R.id.viewPagerFrameLayout);
        fm.setVisibility(View.VISIBLE);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        myViewPagerAdapter = new CouponsPage2.MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        final Handler handler = new Handler();
        final Runnable Update = new Runnable()
        {
            public void run()
            {
                if(viewPager.getCurrentItem()==slider_images.size()-1)
                    viewPager.setCurrentItem(0);
                else
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                handler.post(Update);
            }
        },500,3000);
        indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        GridLayoutManager gridLayout = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayout);
        CouponsAdapter couponsAdapter = new CouponsAdapter(this, coupons,2);
        recyclerView.setAdapter(couponsAdapter);
        couponsAdapter.notifyDataSetChanged();

    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CouponsPage2.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Loading coupons...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "https://pricyfy.com/app_all_coupons2.php";
            String jsonStr = sh.makeServiceCall(url);
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray slidersJsonArray = jsonObject.getJSONArray("slider_images");
                for(int i=0;i<slidersJsonArray.length();i++)
                {
                    JSONObject object = slidersJsonArray.getJSONObject(i);
                    slider_images.add(object.getString("slider_image_link"));
                    slider_links.add(object.getString("redirect_link"));
                }
                JSONArray couponsJsonArray = jsonObject.getJSONArray("coupons");
                for(int i=0;i<couponsJsonArray.length();i++)
                {
                    JSONObject object = couponsJsonArray.getJSONObject(i);
                    Coupons newCoupon = new Coupons(object.getInt("id"),object.getString("company_name"),object.getString("logo_link"));
                    coupons.add(newCoupon);
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
            progressDialog.dismiss();
            setLayouts();
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        @Override
        public Object instantiateItem(ViewGroup container, final int position)
        {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slide1, container, false);
            ImageView iv = (ImageView)view.findViewById(R.id.slider1);
            if(slider_images!=null && slider_images.size()!=0)
                Picasso.with(getApplicationContext()).load(slider_images.get(position)).into(iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    String url="https://pricyfy.com/";
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            });
            container.addView(view);
            return view;
            //    return null;
        }

        @Override
        public int getCount()
        {
            return slider_images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
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

    public void signOut(MenuItem view)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();
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

    public void LogoutButton()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.logout).setVisible(false);
    }
}

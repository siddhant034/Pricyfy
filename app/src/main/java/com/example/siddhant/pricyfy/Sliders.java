package com.example.siddhant.pricyfy;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.siddhant.pricyfy.R.id.indicator;

public class Sliders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private ProgressBar spinner;
    private ViewPager viewPager;
    private Sliders.MyViewPagerAdapter myViewPagerAdapter;
    private ProgressDialog progressDialog;
    private CircleIndicator indicator;
    private ArrayList<ArrayList<Product> > products  = new ArrayList<>(10);
    private String typeOfProducts;
    private int category_name[] = {R.id.tv1a,R.id.tv2a,R.id.tv3a,R.id.tv4a,R.id.tv5a, R.id.tv6a, R.id.tv6a, R.id.tv7a, R.id.tv8a, R.id.tv9a, R.id.tv10a};
    private int view_all[] = {R.id.tv1b,R.id.tv2b,R.id.tv3b,R.id.tv4b,R.id.tv5b, R.id.tv6b, R.id.tv7b, R.id.tv8b, R.id.tv9b, R.id.tv10b};
    private int recycler_view[] = {R.id.rv_content_sliders1,R.id.rv_content_sliders2,R.id.rv_content_sliders3,R.id.rv_content_sliders4,R.id.rv_content_sliders5,R.id.rv_content_sliders6,R.id.rv_content_sliders7,R.id.rv_content_sliders8,R.id.rv_content_sliders9,R.id.rv_content_sliders10};
    private ArrayList<String> names, slugs;
    private ArrayList<String> slider_images, slider_links;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliders_parent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
            typeOfProducts=extras.getString("URL");




        slider_links = new ArrayList<>();
        slider_images = new ArrayList<>();

        names = new ArrayList<>();
        slugs = new ArrayList<>();
        new PrefetchData().execute();
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
        //Toast.makeText(getApplicationContext(),Integer.toString(products.siz
        // e()),Toast.LENGTH_LONG).show();
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        myViewPagerAdapter = new Sliders.MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"hello world",Toast.LENGTH_LONG).show();
            }
        });

        final Handler handler = new Handler();
        final Runnable Update = new Runnable()
        {
            public void run()
            {
                if(viewPager.getCurrentItem()==slider_links.size()-1)
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
        for(int i=0;i<products.size();i++)
        {
            if(products.get(i).size()==0)
                continue;
            TextView tv = (TextView) findViewById(category_name[i]);
            final String n = slugs.get(i);
            tv.setText(names.get(i));
            tv = (TextView) findViewById(view_all[i]);
            tv.setText("View all");
            tv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(getApplicationContext(),allProducts.class);
                    i.putExtra("URL",n);
                    startActivity(i);finish();
                }
            });

            RecyclerView rv1 = (RecyclerView) findViewById(recycler_view[i]);
            LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            rv1.setLayoutManager(linearLayout);
            ProductsAdapterHomePage pAdapter = new ProductsAdapterHomePage(this, products.get(i));
            rv1.setAdapter(pAdapter);
        }
    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Sliders.this);
            progressDialog.setMessage("Loading categories...");
            progressDialog.show();
           /* spinner = (ProgressBar)findViewById(R.id.progressBar1);
            spinner.getIndeterminateDrawable().setColorFilter(spinner.getResources().getColor(R.color.pricify_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
            spinner.setVisibility(View.VISIBLE);    */
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "https://pricyfy.com/app_sliders_products.php?type="+typeOfProducts;
            String jsonStr = sh.makeServiceCall(url);
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray namesJsonArray = jsonObject.getJSONArray("index");
                JSONArray ja = namesJsonArray.getJSONArray(0);
                JSONArray sliders = jsonObject.getJSONArray("sliders");
                for(int i=0;i<sliders.length();i++)
                {
                    JSONObject object = sliders.getJSONObject(i);
                    slider_images.add(object.getString("slider_image_link"));
                    slider_links.add(object.getString("redirect_link"));
                }
                JSONArray array;
                for(int i=0;i<10;i++)
                {
                    if(!jsonObject.has(Integer.toString(i))) continue;
                    JSONObject jo = ja.getJSONObject(i);
                    names.add(jo.getString("name"));
                    slugs.add(jo.getString("slug"));
                    ArrayList<Product> p = new ArrayList<>();
                    products.add(p);
                    array = jsonObject.getJSONArray(Integer.toString(i));
                    for(int j=0;j<array.length();j++)
                    {
                        Product newProduct = null;
                        JSONObject object = array.getJSONObject(j);
                        newProduct = new Product(object.getInt("id"),object.getString("product_name"),object.getInt("flipkart_price"),object.getString("image_link"),object.getInt("discount"),object.getInt("mrp"),object.getDouble("seller_rating"));
                        products.get(i).add(newProduct);
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
          //  Toast.makeText(getApplicationContext(), "len" + Integer.toString(products.size()), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            setLayouts();  
            //spinner.setVisibility(View.GONE);
            //  finish();
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

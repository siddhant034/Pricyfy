package com.example.siddhant.pricyfy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class homeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private CircleIndicator indicator;
    private ArrayList<String> deals_names;
    private ArrayList<String> deals_images_links;
    private ArrayList<String> deals_links;
    private ArrayList<String> coupon_images;
    private ArrayList<String> coupon_title;
    private ArrayList<String> mobile_names;
    private ArrayList<String> mobile_images;
    private ArrayList<String> mobile_mrp;
    private ArrayList<String> camera_names;
    private ArrayList<String> camera_images;
    private ArrayList<String> camera_mrp;

    private ArrayList<String> men_names;
    private ArrayList<String> men_images;
    private ArrayList<String> men_mrp;

    private ArrayList<String> women_names;
    private ArrayList<String> women_images;
    private ArrayList<String> women_mrp;

    private ArrayList<String> footwear_names;
    private ArrayList<String> footwear_images;
    private ArrayList<String> footwear_mrp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //slider start
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        // slider end

        //to disable tinting of the navigation view icons
        NavigationView mNavigationView = (NavigationView)findViewById(R.id.nav_view);
        mNavigationView.setItemIconTintList(null);

        indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        deals_names=this.getIntent().getStringArrayListExtra("deals_names");
        deals_links=this.getIntent().getStringArrayListExtra("deals_links");
        deals_images_links=this.getIntent().getStringArrayListExtra("deals_images_links");
        coupon_images = this.getIntent().getStringArrayListExtra("coupon_images");
        coupon_title = this.getIntent().getStringArrayListExtra("coupon_title");
        mobile_images = this.getIntent().getStringArrayListExtra("mobile_images");
        mobile_mrp = this.getIntent().getStringArrayListExtra("mobile_mrp");
        mobile_names = this.getIntent().getStringArrayListExtra("mobile_names");
        men_images = this.getIntent().getStringArrayListExtra("men_images");
        men_mrp = this.getIntent().getStringArrayListExtra("men_mrp");
        men_names = this.getIntent().getStringArrayListExtra("men_names");
        camera_images = this.getIntent().getStringArrayListExtra("camera_images");
        camera_mrp = this.getIntent().getStringArrayListExtra("camera_mrp");
        camera_names = this.getIntent().getStringArrayListExtra("camera_names");
        women_images = this.getIntent().getStringArrayListExtra("women_images");
        women_mrp = this.getIntent().getStringArrayListExtra("women_mrp");
        women_names = this.getIntent().getStringArrayListExtra("women_names");
        footwear_images = this.getIntent().getStringArrayListExtra("footwear_images");
        footwear_mrp = this.getIntent().getStringArrayListExtra("footwear_mrp");
        footwear_names = this.getIntent().getStringArrayListExtra("footwear_names");
        setDealsButtons();
        setCouponsButtons();
        setMobileRanges();
        setCameraRanges();
        setMenRanges();
        setWomenRanges();
        setFootwearRanges();
    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

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
            startActivity(i);
        }
        else if (id == R.id.dealandOffers)
        {
            Intent i = new Intent(this,homeScreen.class);
            startActivity(i);
        }
        else if (id == R.id.mobilesandTablets)
        {
            Intent i = new Intent(this,allProducts.class);
            i.putExtra("URL","mobiles");
            startActivity(i);
        }
        else if (id == R.id.homeDecor)
        {
            Intent i = new Intent(this,allProducts.class);
            i.putExtra("URL","others");
            startActivity(i);
        }
        else if (id == R.id.men)
        {
            Intent i = new Intent(this,allProducts.class);
            i.putExtra("URL","men");
            startActivity(i);
        }
        else if (id == R.id.women)
        {
            Intent i = new Intent(this,allProducts.class);
            i.putExtra("URL","women");
            startActivity(i);
        }
        else if (id == R.id.laptops)
        {
            Intent i = new Intent(this,allProducts.class);
            i.putExtra("URL","laptops");
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slide1, container, false);
//           // Glide.with(getApplicationContext()).load("https://pricyfy.com/slider-img/slide-1.jpg").into(iv);
            //Glide.with(getApplicationContext()).load(imageLinks[position]).into(iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    String url="";
                    //url = urlLinks[position];
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return 4;
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

    public void openDealsPage(View view)
    {
        Intent i = new Intent(this,dealsScreen.class);
        startActivity(i);
    }

    public void openAllProducts(View view)
    {
      /*  Intent i = new Intent(this,allProducts.class);
        switch(view.getId())
        {
            case R.id.mobiles :
                case R.id.mobile_ranges_button :
                i.putExtra("URL","mobiles"); break;
            case R.id.laptops :
                i.putExtra("URL","laptops"); break;
            case R.id.cameras :
                i.putExtra("URL","cameras"); break;
            case R.id.tv :
                i.putExtra("URL","tv"); break;
            case R.id.men :
                i.putExtra("URL","men"); break;
            case R.id.women :
                i.putExtra("URL","women"); break;
            case R.id.others :
                i.putExtra("URL","others"); break;
        }
        startActivity(i);*/
    }

    public void setCouponsButtons()
    {
        ImageView iv = (ImageView)findViewById(R.id.iv1_coupons);
        TextView tv = (TextView)findViewById(R.id.tv1_coupons);
        Glide.with(getApplicationContext()).load(coupon_images.get(0)).into(iv);
        tv.setText(coupon_title.get(0));

        iv = (ImageView)findViewById(R.id.iv3_coupons);
        tv = (TextView)findViewById(R.id.tv3_coupons);
        Glide.with(getApplicationContext()).load(coupon_images.get(2)).into(iv);
        tv.setText(coupon_title.get(2));

        iv = (ImageView)findViewById(R.id.iv2_coupons);
        tv = (TextView)findViewById(R.id.tv2_coupons);
        Glide.with(getApplicationContext()).load(coupon_images.get(1)).into(iv);
        tv.setText(coupon_title.get(1));
    }

    public void setDealsButtons()
    {
        ImageView iv = (ImageView)findViewById(R.id.iv1);
        TextView tv = (TextView)findViewById(R.id.tv1);
        Glide.with(getApplicationContext()).load(deals_images_links.get(0)).into(iv);
        tv.setText(deals_names.get(0));

        iv = (ImageView)findViewById(R.id.iv2);
        tv = (TextView)findViewById(R.id.tv2);
        Glide.with(getApplicationContext()).load(deals_images_links.get(1)).into(iv);
        tv.setText(deals_names.get(1));

        iv = (ImageView)findViewById(R.id.iv3);
        tv = (TextView)findViewById(R.id.tv3);
        Glide.with(getApplicationContext()).load(deals_images_links.get(2)).into(iv);
        tv.setText(deals_names.get(2));

        iv = (ImageView)findViewById(R.id.iv4);
        tv = (TextView)findViewById(R.id.tv4);
        Glide.with(getApplicationContext()).load(deals_images_links.get(3)).into(iv);
        tv.setText(deals_names.get(3));

        iv = (ImageView)findViewById(R.id.iv5);
        tv = (TextView)findViewById(R.id.tv5);
        Glide.with(getApplicationContext()).load(deals_images_links.get(4)).into(iv);
        tv.setText(deals_names.get(4));

        iv = (ImageView)findViewById(R.id.iv6);
        tv = (TextView)findViewById(R.id.tv6);
        Glide.with(getApplicationContext()).load(deals_images_links.get(5)).into(iv);
        tv.setText(deals_names.get(5));

        iv = (ImageView)findViewById(R.id.iv7);
        tv = (TextView)findViewById(R.id.tv7);
        Glide.with(getApplicationContext()).load(deals_images_links.get(6)).into(iv);
        tv.setText(deals_names.get(6));

        iv = (ImageView)findViewById(R.id.iv8);
        tv = (TextView)findViewById(R.id.tv8);
        Glide.with(getApplicationContext()).load(deals_images_links.get(7)).into(iv);
        tv.setText(deals_names.get(7));

        iv = (ImageView)findViewById(R.id.iv9);
        tv = (TextView)findViewById(R.id.tv9);
        Glide.with(getApplicationContext()).load(deals_images_links.get(8)).into(iv);
        tv.setText(deals_names.get(8));

        iv = (ImageView)findViewById(R.id.iv10);
        tv = (TextView)findViewById(R.id.tv10);
        Glide.with(getApplicationContext()).load(deals_images_links.get(9)).into(iv);
        tv.setText(deals_names.get(9));

        iv = (ImageView)findViewById(R.id.iv11);
        tv = (TextView)findViewById(R.id.tv11);
        Glide.with(getApplicationContext()).load(deals_images_links.get(10)).into(iv);
        tv.setText(deals_names.get(10));

        iv = (ImageView)findViewById(R.id.iv12);
        tv = (TextView)findViewById(R.id.tv12);
        Glide.with(getApplicationContext()).load(deals_images_links.get(11)).into(iv);
        tv.setText(deals_names.get(11));
    }

    public void setMobileRanges()
    {
        ImageView iv = (ImageView)findViewById(R.id.iv1_mobiles);
        TextView tv = (TextView)findViewById(R.id.tv1_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(0)).into(iv);
        tv.setText(mobile_names.get(0));
        tv = (TextView)findViewById(R.id.mobile_mrp1);
        tv.setText("Rs. "+mobile_mrp.get(0));

        iv = (ImageView)findViewById(R.id.iv2_mobiles);
        tv = (TextView)findViewById(R.id.tv2_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(1)).into(iv);
        tv.setText(mobile_names.get(1));
        tv = (TextView)findViewById(R.id.mobile_mrp2);
        tv.setText("Rs. "+mobile_mrp.get(1));

        iv = (ImageView)findViewById(R.id.iv3_mobiles);
        tv = (TextView)findViewById(R.id.tv3_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(2)).into(iv);
        tv.setText(mobile_names.get(2));
        tv = (TextView)findViewById(R.id.mobile_mrp3);
        tv.setText("Rs. "+mobile_mrp.get(2));

        iv = (ImageView)findViewById(R.id.iv4_mobiles);
        tv = (TextView)findViewById(R.id.tv4_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(3)).into(iv);
        tv.setText(mobile_names.get(3));
        tv = (TextView)findViewById(R.id.mobile_mrp4);
        tv.setText("Rs. "+mobile_mrp.get(3));

        iv = (ImageView)findViewById(R.id.iv5_mobiles);
        tv = (TextView)findViewById(R.id.tv5_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(4)).into(iv);
        tv.setText(mobile_names.get(4));
        tv = (TextView)findViewById(R.id.mobile_mrp5);
        tv.setText("Rs. "+mobile_mrp.get(4));

        iv = (ImageView)findViewById(R.id.iv6_mobiles);
        tv = (TextView)findViewById(R.id.tv6_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(5)).into(iv);
        tv.setText(mobile_names.get(5));
        tv = (TextView)findViewById(R.id.mobile_mrp6);
        tv.setText("Rs. "+mobile_mrp.get(5));

        iv = (ImageView)findViewById(R.id.iv7_mobiles);
        tv = (TextView)findViewById(R.id.tv7_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(6)).into(iv);
        tv.setText(mobile_names.get(6));
        tv = (TextView)findViewById(R.id.mobile_mrp7);
        tv.setText("Rs. "+mobile_mrp.get(6));

        iv = (ImageView)findViewById(R.id.iv8_mobiles);
        tv = (TextView)findViewById(R.id.tv8_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(7)).into(iv);
        tv.setText(mobile_names.get(7));
        tv = (TextView)findViewById(R.id.mobile_mrp8);
        tv.setText("Rs. "+mobile_mrp.get(7));

        iv = (ImageView)findViewById(R.id.iv9_mobiles);
        tv = (TextView)findViewById(R.id.tv9_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(8)).into(iv);
        tv.setText(mobile_names.get(8));
        tv = (TextView)findViewById(R.id.mobile_mrp9);
        tv.setText("Rs. "+mobile_mrp.get(8));

        iv = (ImageView)findViewById(R.id.iv10_mobiles);
        tv = (TextView)findViewById(R.id.tv10_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(9)).into(iv);
        tv.setText(mobile_names.get(9));
        tv = (TextView)findViewById(R.id.mobile_mrp10);
        tv.setText("Rs. "+mobile_mrp.get(9));

        iv = (ImageView)findViewById(R.id.iv11_mobiles);
        tv = (TextView)findViewById(R.id.tv11_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(10)).into(iv);
        tv.setText(mobile_names.get(10));
        tv = (TextView)findViewById(R.id.mobile_mrp11);
        tv.setText("Rs. "+mobile_mrp.get(10));

        iv = (ImageView)findViewById(R.id.iv12_mobiles);
        tv = (TextView)findViewById(R.id.tv12_mobiles);
        Glide.with(getApplicationContext()).load(mobile_images.get(11)).into(iv);
        tv.setText(mobile_names.get(11));
        tv = (TextView)findViewById(R.id.mobile_mrp12);
        tv.setText("Rs. "+mobile_mrp.get(11));
    }

    public void setCameraRanges()
    {
        ImageView iv = (ImageView)findViewById(R.id.iv1_cameras);
        TextView tv = (TextView)findViewById(R.id.tv1_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(0)).into(iv);
        tv.setText(camera_names.get(0));
        tv = (TextView)findViewById(R.id.cameras_mrp1);
        tv.setText("Rs. "+camera_mrp.get(0));

        iv = (ImageView)findViewById(R.id.iv2_cameras);
        tv = (TextView)findViewById(R.id.tv2_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(1)).into(iv);
        tv.setText(camera_names.get(1));
        tv = (TextView)findViewById(R.id.cameras_mrp2);
        tv.setText("Rs. "+camera_mrp.get(1));

        iv = (ImageView)findViewById(R.id.iv3_cameras);
        tv = (TextView)findViewById(R.id.tv3_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(2)).into(iv);
        tv.setText(camera_names.get(2));
        tv = (TextView)findViewById(R.id.cameras_mrp3);
        tv.setText("Rs. "+camera_mrp.get(2));

        iv = (ImageView)findViewById(R.id.iv4_cameras);
        tv = (TextView)findViewById(R.id.tv4_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(3)).into(iv);
        tv.setText(camera_names.get(3));
        tv = (TextView)findViewById(R.id.cameras_mrp4);
        tv.setText("Rs. "+camera_mrp.get(3));

        iv = (ImageView)findViewById(R.id.iv5_cameras);
        tv = (TextView)findViewById(R.id.tv5_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(4)).into(iv);
        tv.setText(camera_names.get(4));
        tv = (TextView)findViewById(R.id.cameras_mrp5);
        tv.setText("Rs. "+camera_mrp.get(4));

        iv = (ImageView)findViewById(R.id.iv6_cameras);
        tv = (TextView)findViewById(R.id.tv6_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(5)).into(iv);
        tv.setText(camera_names.get(5));
        tv = (TextView)findViewById(R.id.cameras_mrp6);
        tv.setText("Rs. "+camera_mrp.get(5));

        iv = (ImageView)findViewById(R.id.iv7_cameras);
        tv = (TextView)findViewById(R.id.tv7_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(6)).into(iv);
        tv.setText(camera_names.get(6));
        tv = (TextView)findViewById(R.id.cameras_mrp7);
        tv.setText("Rs. "+camera_mrp.get(6));

        iv = (ImageView)findViewById(R.id.iv8_cameras);
        tv = (TextView)findViewById(R.id.tv8_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(7)).into(iv);
        tv.setText(camera_names.get(7));
        tv = (TextView)findViewById(R.id.cameras_mrp8);
        tv.setText("Rs. "+camera_mrp.get(7));

        iv = (ImageView)findViewById(R.id.iv9_cameras);
        tv = (TextView)findViewById(R.id.tv9_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(8)).into(iv);
        tv.setText(camera_names.get(8));
        tv = (TextView)findViewById(R.id.cameras_mrp9);
        tv.setText("Rs. "+camera_mrp.get(8));

        iv = (ImageView)findViewById(R.id.iv10_cameras);
        tv = (TextView)findViewById(R.id.tv10_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(9)).into(iv);
        tv.setText(camera_names.get(9));
        tv = (TextView)findViewById(R.id.cameras_mrp10);
        tv.setText("Rs. "+camera_mrp.get(9));

        iv = (ImageView)findViewById(R.id.iv11_cameras);
        tv = (TextView)findViewById(R.id.tv11_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(10)).into(iv);
        tv.setText(camera_names.get(10));
        tv = (TextView)findViewById(R.id.cameras_mrp11);
        tv.setText("Rs. "+camera_mrp.get(10));

        iv = (ImageView)findViewById(R.id.iv12_cameras);
        tv = (TextView)findViewById(R.id.tv12_cameras);
        Glide.with(getApplicationContext()).load(camera_images.get(11)).into(iv);
        tv.setText(camera_names.get(11));
        tv = (TextView)findViewById(R.id.cameras_mrp12);
        tv.setText("Rs. "+camera_mrp.get(11));
    }


    public void setMenRanges()
    {
        ImageView iv = (ImageView)findViewById(R.id.iv1_men);
        TextView tv = (TextView)findViewById(R.id.tv1_men);
        Glide.with(getApplicationContext()).load(men_images.get(0)).into(iv);
        tv.setText(men_names.get(0));
        tv = (TextView)findViewById(R.id.men_mrp1);
        tv.setText("Rs. "+men_mrp.get(0));

        iv = (ImageView)findViewById(R.id.iv2_men);
        tv = (TextView)findViewById(R.id.tv2_men);
        Glide.with(getApplicationContext()).load(men_images.get(1)).into(iv);
        tv.setText(men_names.get(1));
        tv = (TextView)findViewById(R.id.men_mrp2);
        tv.setText("Rs. "+men_mrp.get(1));

        iv = (ImageView)findViewById(R.id.iv3_men);
        tv = (TextView)findViewById(R.id.tv3_men);
        Glide.with(getApplicationContext()).load(men_images.get(2)).into(iv);
        tv.setText(men_names.get(2));
        tv = (TextView)findViewById(R.id.men_mrp3);
        tv.setText("Rs. "+men_mrp.get(2));

        iv = (ImageView)findViewById(R.id.iv4_men);
        tv = (TextView)findViewById(R.id.tv4_men);
        Glide.with(getApplicationContext()).load(men_images.get(3)).into(iv);
        tv.setText(men_names.get(3));
        tv = (TextView)findViewById(R.id.men_mrp4);
        tv.setText("Rs. "+men_mrp.get(3));

        iv = (ImageView)findViewById(R.id.iv5_men);
        tv = (TextView)findViewById(R.id.tv5_men);
        Glide.with(getApplicationContext()).load(men_images.get(4)).into(iv);
        tv.setText(men_names.get(4));
        tv = (TextView)findViewById(R.id.men_mrp5);
        tv.setText("Rs. "+men_mrp.get(4));

        iv = (ImageView)findViewById(R.id.iv6_men);
        tv = (TextView)findViewById(R.id.tv6_men);
        Glide.with(getApplicationContext()).load(men_images.get(5)).into(iv);
        tv.setText(men_names.get(5));
        tv = (TextView)findViewById(R.id.men_mrp6);
        tv.setText("Rs. "+men_mrp.get(5));

        iv = (ImageView)findViewById(R.id.iv7_men);
        tv = (TextView)findViewById(R.id.tv7_men);
        Glide.with(getApplicationContext()).load(men_images.get(6)).into(iv);
        tv.setText(men_names.get(6));
        tv = (TextView)findViewById(R.id.men_mrp7);
        tv.setText("Rs. "+men_mrp.get(6));

        iv = (ImageView)findViewById(R.id.iv8_men);
        tv = (TextView)findViewById(R.id.tv8_men);
        Glide.with(getApplicationContext()).load(men_images.get(7)).into(iv);
        tv.setText(men_names.get(7));
        tv = (TextView)findViewById(R.id.men_mrp8);
        tv.setText("Rs. "+men_mrp.get(7));

        iv = (ImageView)findViewById(R.id.iv9_men);
        tv = (TextView)findViewById(R.id.tv9_men);
        Glide.with(getApplicationContext()).load(men_images.get(8)).into(iv);
        tv.setText(men_names.get(8));
        tv = (TextView)findViewById(R.id.men_mrp9);
        tv.setText("Rs. "+men_mrp.get(8));

        iv = (ImageView)findViewById(R.id.iv10_men);
        tv = (TextView)findViewById(R.id.tv10_men);
        Glide.with(getApplicationContext()).load(men_images.get(9)).into(iv);
        tv.setText(men_names.get(9));
        tv = (TextView)findViewById(R.id.men_mrp10);
        tv.setText("Rs. "+men_mrp.get(9));

        iv = (ImageView)findViewById(R.id.iv11_men);
        tv = (TextView)findViewById(R.id.tv11_men);
        Glide.with(getApplicationContext()).load(men_images.get(10)).into(iv);
        tv.setText(men_names.get(10));
        tv = (TextView)findViewById(R.id.men_mrp11);
        tv.setText("Rs. "+men_mrp.get(10));

        iv = (ImageView)findViewById(R.id.iv12_men);
        tv = (TextView)findViewById(R.id.tv12_men);
        Glide.with(getApplicationContext()).load(men_images.get(11)).into(iv);
        tv.setText(men_names.get(11));
        tv = (TextView)findViewById(R.id.men_mrp12);
        tv.setText("Rs. "+mobile_mrp.get(11));
    }

    public void setWomenRanges()
    {
        ImageView iv = (ImageView)findViewById(R.id.iv1_women);
        TextView tv = (TextView)findViewById(R.id.tv1_women);
        Glide.with(getApplicationContext()).load(women_images.get(0)).into(iv);
        tv.setText(women_names.get(0));
        tv = (TextView)findViewById(R.id.women_mrp1);
        tv.setText("Rs. "+women_mrp.get(0));

        iv = (ImageView)findViewById(R.id.iv2_women);
        tv = (TextView)findViewById(R.id.tv2_women);
        Glide.with(getApplicationContext()).load(women_images.get(1)).into(iv);
        tv.setText(women_names.get(1));
        tv = (TextView)findViewById(R.id.women_mrp2);
        tv.setText("Rs. "+women_mrp.get(1));

        iv = (ImageView)findViewById(R.id.iv3_women);
        tv = (TextView)findViewById(R.id.tv3_women);
        Glide.with(getApplicationContext()).load(women_images.get(2)).into(iv);
        tv.setText(women_names.get(2));
        tv = (TextView)findViewById(R.id.women_mrp3);
        tv.setText("Rs. "+women_mrp.get(2));

        iv = (ImageView)findViewById(R.id.iv4_women);
        tv = (TextView)findViewById(R.id.tv4_women);
        Glide.with(getApplicationContext()).load(women_images.get(3)).into(iv);
        tv.setText(women_names.get(3));
        tv = (TextView)findViewById(R.id.women_mrp4);
        tv.setText("Rs. "+women_mrp.get(3));

        iv = (ImageView)findViewById(R.id.iv5_women);
        tv = (TextView)findViewById(R.id.tv5_women);
        Glide.with(getApplicationContext()).load(women_images.get(4)).into(iv);
        tv.setText(women_names.get(4));
        tv = (TextView)findViewById(R.id.women_mrp5);
        tv.setText("Rs. "+women_mrp.get(4));

        iv = (ImageView)findViewById(R.id.iv6_women);
        tv = (TextView)findViewById(R.id.tv6_women);
        Glide.with(getApplicationContext()).load(women_images.get(5)).into(iv);
        tv.setText(women_names.get(5));
        tv = (TextView)findViewById(R.id.women_mrp6);
        tv.setText("Rs. "+women_mrp.get(5));

        iv = (ImageView)findViewById(R.id.iv7_women);
        tv = (TextView)findViewById(R.id.tv7_women);
        Glide.with(getApplicationContext()).load(women_images.get(6)).into(iv);
        tv.setText(women_names.get(6));
        tv = (TextView)findViewById(R.id.women_mrp7);
        tv.setText("Rs. "+women_mrp.get(6));

        iv = (ImageView)findViewById(R.id.iv8_women);
        tv = (TextView)findViewById(R.id.tv8_women);
        Glide.with(getApplicationContext()).load(women_images.get(7)).into(iv);
        tv.setText(women_names.get(7));
        tv = (TextView)findViewById(R.id.women_mrp8);
        tv.setText("Rs. "+women_mrp.get(7));

        iv = (ImageView)findViewById(R.id.iv9_women);
        tv = (TextView)findViewById(R.id.tv9_women);
        Glide.with(getApplicationContext()).load(women_images.get(8)).into(iv);
        tv.setText(women_names.get(8));
        tv = (TextView)findViewById(R.id.women_mrp9);
        tv.setText("Rs. "+women_mrp.get(8));

        iv = (ImageView)findViewById(R.id.iv10_women);
        tv = (TextView)findViewById(R.id.tv10_women);
        Glide.with(getApplicationContext()).load(women_images.get(9)).into(iv);
        tv.setText(women_names.get(9));
        tv = (TextView)findViewById(R.id.women_mrp10);
        tv.setText("Rs. "+women_mrp.get(9));

        iv = (ImageView)findViewById(R.id.iv11_women);
        tv = (TextView)findViewById(R.id.tv11_women);
        Glide.with(getApplicationContext()).load(women_images.get(10)).into(iv);
        tv.setText(women_names.get(10));
        tv = (TextView)findViewById(R.id.women_mrp11);
        tv.setText("Rs. "+women_mrp.get(10));

        iv = (ImageView)findViewById(R.id.iv12_women);
        tv = (TextView)findViewById(R.id.tv12_women);
        Glide.with(getApplicationContext()).load(women_images.get(11)).into(iv);
        tv.setText(women_names.get(11));
        tv = (TextView)findViewById(R.id.women_mrp12);
        tv.setText("Rs. "+mobile_mrp.get(11));
    }

    public void setFootwearRanges()
    {
        ImageView iv = (ImageView)findViewById(R.id.iv1_footwear);
        TextView tv = (TextView)findViewById(R.id.tv1_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(0)).into(iv);
        tv.setText(footwear_names.get(0));
        tv = (TextView)findViewById(R.id.footwear_mrp1);
        tv.setText("Rs. "+footwear_mrp.get(0));

        iv = (ImageView)findViewById(R.id.iv2_footwear);
        tv = (TextView)findViewById(R.id.tv2_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(1)).into(iv);
        tv.setText(footwear_names.get(1));
        tv = (TextView)findViewById(R.id.footwear_mrp2);
        tv.setText("Rs. "+footwear_mrp.get(1));

        iv = (ImageView)findViewById(R.id.iv3_footwear);
        tv = (TextView)findViewById(R.id.tv3_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(2)).into(iv);
        tv.setText(footwear_names.get(2));
        tv = (TextView)findViewById(R.id.footwear_mrp3);
        tv.setText("Rs. "+footwear_mrp.get(2));

        iv = (ImageView)findViewById(R.id.iv4_footwear);
        tv = (TextView)findViewById(R.id.tv4_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(3)).into(iv);
        tv.setText(footwear_names.get(3));
        tv = (TextView)findViewById(R.id.footwear_mrp4);
        tv.setText("Rs. "+footwear_mrp.get(3));

        iv = (ImageView)findViewById(R.id.iv5_footwear);
        tv = (TextView)findViewById(R.id.tv5_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(4)).into(iv);
        tv.setText(footwear_names.get(4));
        tv = (TextView)findViewById(R.id.footwear_mrp5);
        tv.setText("Rs. "+footwear_mrp.get(4));

        iv = (ImageView)findViewById(R.id.iv6_footwear);
        tv = (TextView)findViewById(R.id.tv6_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(5)).into(iv);
        tv.setText(footwear_names.get(5));
        tv = (TextView)findViewById(R.id.footwear_mrp6);
        tv.setText("Rs. "+footwear_mrp.get(5));

        iv = (ImageView)findViewById(R.id.iv7_footwear);
        tv = (TextView)findViewById(R.id.tv7_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(6)).into(iv);
        tv.setText(footwear_names.get(6));
        tv = (TextView)findViewById(R.id.footwear_mrp7);
        tv.setText("Rs. "+footwear_mrp.get(6));

        iv = (ImageView)findViewById(R.id.iv8_footwear);
        tv = (TextView)findViewById(R.id.tv8_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(7)).into(iv);
        tv.setText(footwear_names.get(7));
        tv = (TextView)findViewById(R.id.footwear_mrp8);
        tv.setText("Rs. "+footwear_mrp.get(7));

        iv = (ImageView)findViewById(R.id.iv9_footwear);
        tv = (TextView)findViewById(R.id.tv9_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(8)).into(iv);
        tv.setText(footwear_names.get(8));
        tv = (TextView)findViewById(R.id.footwear_mrp9);
        tv.setText("Rs. "+footwear_mrp.get(8));

        iv = (ImageView)findViewById(R.id.iv10_footwear);
        tv = (TextView)findViewById(R.id.tv10_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(9)).into(iv);
        tv.setText(footwear_names.get(9));
        tv = (TextView)findViewById(R.id.footwear_mrp10);
        tv.setText("Rs. "+footwear_mrp.get(9));

        iv = (ImageView)findViewById(R.id.iv11_footwear);
        tv = (TextView)findViewById(R.id.tv11_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(10)).into(iv);
        tv.setText(footwear_names.get(10));
        tv = (TextView)findViewById(R.id.footwear_mrp11);
        tv.setText("Rs. "+footwear_mrp.get(10));

        iv = (ImageView)findViewById(R.id.iv12_footwear);
        tv = (TextView)findViewById(R.id.tv12_footwear);
        Glide.with(getApplicationContext()).load(footwear_images.get(11)).into(iv);
        tv.setText(footwear_names.get(11));
        tv = (TextView)findViewById(R.id.footwear_mrp12);
        tv.setText("Rs. "+mobile_mrp.get(11));
    }
    
    public void openDeal(View view)
    {
        String url="";
        switch(view.getId())
        {
            case R.id.ll1 : url=deals_links.get(0);break;
            case R.id.ll2 : url=deals_links.get(1);break;
            case R.id.ll3 : url=deals_links.get(2);break;
            case R.id.ll4 : url=deals_links.get(3);break;
            case R.id.ll5 : url=deals_links.get(4);break;
            case R.id.ll6 : url=deals_links.get(5);break;
            case R.id.ll7 : url=deals_links.get(6);break;
            case R.id.ll8 : url=deals_links.get(7);break;
            case R.id.ll9 : url=deals_links.get(8);break;
            case R.id.ll10 : url=deals_links.get(9);break;
            case R.id.ll11 : url=deals_links.get(10);break;
            case R.id.ll12 : url=deals_links.get(11);break;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void openCouponsPage(View view)
    {

    }

    public void openMobilePage(View view)
    {

    }

    public void openAdBanner1(View view)
    {

    }
}

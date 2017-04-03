package com.example.siddhant.pricyfy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class homeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private CircleIndicator indicator;
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
        navigationView.setItemIconTintList(null);

        indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        deals = new ArrayList<>();
        mobiles = new ArrayList<>();
        men = new ArrayList<>();
        women = new ArrayList<>();
        cameras = new ArrayList<>();
        footwear = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        deals = extras.getParcelableArrayList("deals");
        coupon_images = this.getIntent().getStringArrayListExtra("coupon_images");
        coupon_title = this.getIntent().getStringArrayListExtra("coupon_title");
        mobiles = extras.getParcelableArrayList("mobiles");
        men = extras.getParcelableArrayList("men");
        women = extras.getParcelableArrayList("women");
        cameras = extras.getParcelableArrayList("cameras");
        footwear = extras.getParcelableArrayList("footwear");
        //mobiles=(ArrayList<Product>)getIntent().getSerializableExtra("mobiles");
        //men=(ArrayList<Product>)getIntent().getSerializableExtra("men");
        //cameras=(ArrayList<Product>)getIntent().getSerializableExtra("cameras");
        //women=(ArrayList<Product>)getIntent().getSerializableExtra("women");
        //footwear=(ArrayList<Product>)getIntent().getSerializableExtra("footwear");

        if(!(deals==null))
            setDealsButtons();
        if(!coupon_images.isEmpty() && !coupon_title.isEmpty())
            setCouponsButtons();
        if(!mobiles.isEmpty())
           setMobileRanges();
        if(!cameras.isEmpty())
            setCameraRanges();
        if(!men.isEmpty())
            setMenRanges();
        if(!women.isEmpty())
           setWomenRanges();
        if(!footwear.isEmpty())
           setFootwearRanges();
        loadAdBanners();
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
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
        RecyclerView rv_deals = (RecyclerView) findViewById(R.id.rv_deals);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_deals.setLayoutManager(linearLayout);
        DealsAdapter dAdapter = new DealsAdapter(this,deals,0);
        rv_deals.setAdapter(dAdapter);
    }

    public void setMobileRanges()
    {
        RecyclerView rv_mobiles = (RecyclerView) findViewById(R.id.rv_mobiles);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_mobiles.setLayoutManager(linearLayout);
        ProductsAdapter pAdapter = new ProductsAdapter(this,mobiles,0);
        rv_mobiles.setAdapter(pAdapter);
    }

    public void setCameraRanges()
    {
        RecyclerView rv_cameras = (RecyclerView) findViewById(R.id.rv_cameras);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_cameras.setLayoutManager(linearLayout);
        ProductsAdapter pAdapter = new ProductsAdapter(this,cameras,0);
        rv_cameras.setAdapter(pAdapter);
    }


    public void setMenRanges()
    {
        RecyclerView rv_men = (RecyclerView) findViewById(R.id.rv_men);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_men.setLayoutManager(linearLayout);
        ProductsAdapter pAdapter = new ProductsAdapter(this,men,0);
        rv_men.setAdapter(pAdapter);
    }

    public void setWomenRanges()
    {
        RecyclerView rv_women = (RecyclerView) findViewById(R.id.rv_women);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_women.setLayoutManager(linearLayout);
        ProductsAdapter pAdapter = new ProductsAdapter(this,women,0);
        rv_women.setAdapter(pAdapter);
    }

    public void setFootwearRanges()
    {
        RecyclerView rv_footwear = (RecyclerView) findViewById(R.id.rv_footwear);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_footwear.setLayoutManager(linearLayout);
        ProductsAdapter pAdapter = new ProductsAdapter(this,footwear,0);
        rv_footwear.setAdapter(pAdapter);
    }
    
    public void openDeal(View view)
    {
        String url="https://pricyfy.com/";
       /* switch(view.getId())
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
        }*/
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void loadAdBanners()
    {
        ImageView iv;

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout1);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner1.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout2);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner2.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout3);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner3.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout4);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner4.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout5);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner5.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout6);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner6.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout7);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner7.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout8);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner8.jpg").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout9);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner9.jpg").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout10);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner10.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout11);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner11.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout12);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner12.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout13);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner13.png").into(iv);

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout14);
        Picasso.with(getApplicationContext()).load("https://pricyfy.com/app_ad_banners/ad_banner14.png").into(iv);
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

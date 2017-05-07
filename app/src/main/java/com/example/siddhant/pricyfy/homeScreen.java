package com.example.siddhant.pricyfy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.method.SingleLineTransformationMethod;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

import me.relex.circleindicator.CircleIndicator;

public class homeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private CircleIndicator indicator;
    private ArrayList<Deal> deals;
    private ArrayList<String> coupon_images, coupon_title, coupon_links, slider_images, slider_links, ad_banners, ad_banner_links;
    private ArrayList<Product> mobiles, cameras, men, women, footwear;
    private Bitmap profilePic;

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

        // slider end

        //to disable tinting of the navigation view icons
        navigationView.setItemIconTintList(null);

        deals = new ArrayList<>();
        mobiles = new ArrayList<>();
        men = new ArrayList<>();
        women = new ArrayList<>();
        cameras = new ArrayList<>();
        footwear = new ArrayList<>();
        slider_links = new ArrayList<>();
        slider_images = new ArrayList<>();
        ad_banner_links = new ArrayList<>();
        ad_banners = new ArrayList<>();
        coupon_links = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            deals = extras.getParcelableArrayList("deals");
            mobiles = extras.getParcelableArrayList("mobiles");
            cameras = extras.getParcelableArrayList("cameras");
            men = extras.getParcelableArrayList("men");
            women = extras.getParcelableArrayList("women");
            footwear = extras.getParcelableArrayList("footwear");
            coupon_images = this.getIntent().getStringArrayListExtra("coupon_images");
            coupon_title = this.getIntent().getStringArrayListExtra("coupon_title");
            coupon_links = this.getIntent().getStringArrayListExtra("coupon_links");
            slider_images = this.getIntent().getStringArrayListExtra("slider_images");
            slider_links = this.getIntent().getStringArrayListExtra("slider_links");
            ad_banners = this.getIntent().getStringArrayListExtra("ad_banners");
            ad_banner_links = this.getIntent().getStringArrayListExtra("ad_banner_links");
            if (!(deals == null)) setDealsButtons();
            if (!coupon_images.isEmpty() && !coupon_title.isEmpty()) setCouponsButtons();
            if (!mobiles.isEmpty()) setMobileRanges();
            if (!cameras.isEmpty()) setCameraRanges();
            if (!men.isEmpty()) setMenRanges();
            if (!women.isEmpty()) setWomenRanges();
            if (!footwear.isEmpty()) setFootwearRanges();
            loadAdBanners();
            viewPager = (ViewPager)findViewById(R.id.view_pager);
            myViewPagerAdapter = new MyViewPagerAdapter();
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
                    if(viewPager.getCurrentItem()==slider_images.size())
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
        }

        ImageView iv = (ImageView)findViewById(R.id.iv_footer_home);
        iv.setColorFilter(getResources().getColor(R.color.pricify_blue));
        TextView tv = (TextView)findViewById(R.id.tv_footer_home);
        tv.setTextColor(getResources().getColor(R.color.pricify_blue));

        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCES", MODE_PRIVATE);
        Boolean isSignedInGoogle = sharedPreferences.getBoolean("signedInGoogle",false);
        Boolean isSignedInFb = sharedPreferences.getBoolean("signedInFb",false);
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
            String userId = sharedPreferences.getString("profilePicture",null);
            URL imageURL = null;
            try {
                imageURL = new URL("https://graph.facebook.com/" + userId + "/picture?type=large");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            getProfilePic(userId);
            tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.GONE);
            tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText(name);
            tv.setClickable(false);
          //  iv = (ImageView)navHeaderView.findViewById(R.id.imageView);
          //  iv.setImageBitmap(profilePic);
        }
        else
        {
            View navHeaderView = navigationView.getHeaderView(0);
            tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.VISIBLE);
            tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText("Sign in");
            tv.setClickable(true);
        }
        iv = (ImageView)findViewById(R.id.iv_company_logo);
        Picasso.with(getApplicationContext()).load(R.drawable.company_logo).into(iv);
    }

    public void getProfilePic(final String userId)
    {
        AsyncTask<String, Void, Void> asyncTask = new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                try {
                    URL imageURL = new URL("https://graph.facebook.com/" + userId + "/picture?type=large");
                    profilePic = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        asyncTask.execute();
    }

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
            DrawerLayout mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();
        }
        else if(id == R.id.mobiles_and_tablets)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","mobiles");
            startActivity(i);
        }
        else if (id == R.id.laptops)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","laptops");
            startActivity(i);
        }
        else if (id == R.id.cameras)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","cameras");
            startActivity(i);
        }
        else if (id == R.id.tv_and_entertainment)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","tv");
            startActivity(i);
        }
        else if (id == R.id.home_and_kitchen_appliances)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","home");
            startActivity(i);
        }
        else if (id == R.id.home_decors)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","home_decors");
            startActivity(i);
        }
        else if (id == R.id.men)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","men");
            startActivity(i);
        }
        else if (id == R.id.women)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","women");
            startActivity(i);
        }
        else if (id == R.id.mens_footwear)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","mens_footwear");
            startActivity(i);
        }
        else if (id == R.id.womens_footwear)
        {
            Intent i = new Intent(this,Sliders.class);
            i.putExtra("URL","womens_footwear");
            startActivity(i);
        }
        else if (id == R.id.coupons_and_cashbacks)
        {
            Intent i = new Intent(this,CouponsPage2.class);
            startActivity(i);
        }
        else if(id == R.id.deals)
        {
            Intent i = new Intent(this,DealsPage.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                    String url=slider_links.get(position);
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

    public void openDealsPage(View view)
    {
        Intent i = new Intent(this,DealsPage.class);
        startActivity(i);
    }

    public void openCouponsPage(View view)
    {
        Intent i = new Intent(this,CouponsPage2.class);
        startActivity(i);
    }

    public void openAllProducts(View view)
    {
        Intent i = new Intent(this,allProducts.class);
        switch(view.getId())
        {
            case R.id.mobiles :
                i.putExtra("URL","mobiles"); break;
            case R.id.smartwatches :
                i.putExtra("URL","smartwatches"); break;
            case R.id.cameras :
                i.putExtra("URL","cameras"); break;
            case R.id.furnitures :
                i.putExtra("URL","furnitures"); break;
            case R.id.men :
                i.putExtra("URL","men"); break;
            case R.id.mens_accessories :
                i.putExtra("URL","mens_accessories"); break;
            case R.id.women :
                i.putExtra("URL","women"); break;
        }
        startActivity(i);
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

    public void setMobileRanges()
    {
        RecyclerView rv_mobiles = (RecyclerView) findViewById(R.id.rv_mobiles);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_mobiles.setLayoutManager(linearLayout);
        ProductsAdapterHomePage pAdapter = new ProductsAdapterHomePage(this,mobiles);
        rv_mobiles.setAdapter(pAdapter);
    }

    public void setCameraRanges()
    {
        RecyclerView rv_cameras = (RecyclerView) findViewById(R.id.rv_cameras);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_cameras.setLayoutManager(linearLayout);
        ProductsAdapterHomePage pAdapter = new ProductsAdapterHomePage(this,cameras);
        rv_cameras.setAdapter(pAdapter);
    }


    public void setMenRanges()
    {
        RecyclerView rv_men = (RecyclerView) findViewById(R.id.rv_men);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_men.setLayoutManager(linearLayout);
        ProductsAdapterHomePage pAdapter = new ProductsAdapterHomePage(this,men);
        rv_men.setAdapter(pAdapter);
    }

    public void setWomenRanges()
    {
        RecyclerView rv_women = (RecyclerView) findViewById(R.id.rv_women);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_women.setLayoutManager(linearLayout);
        ProductsAdapterHomePage pAdapter = new ProductsAdapterHomePage(this,women);
        rv_women.setAdapter(pAdapter);
    }

    public void setViewAllButtons(View view)
    {
        Intent i = new Intent(this,allProducts.class);
        switch(view.getId())
        {
            case R.id.tvLL1 : i.putExtra("URL","deals");break;
            case R.id.tvLL2 : i.putExtra("URL","coupons");break;
            case R.id.tvLL3 : i.putExtra("URL","mobiles");break;
            case R.id.tvLL4 : i.putExtra("URL","cameras");break;
            case R.id.tvLL5 : i.putExtra("URL","men");break;
            case R.id.tvLL6 : i.putExtra("URL","women");break;
            case R.id.tvLL7 : i.putExtra("URL","womens_footwear");break;
        }
        startActivity(i);
    }

    public void setFootwearRanges()
    {
        RecyclerView rv_footwear = (RecyclerView) findViewById(R.id.rv_footwear);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_footwear.setLayoutManager(linearLayout);
        ProductsAdapterHomePage pAdapter = new ProductsAdapterHomePage(this,footwear);
        rv_footwear.setAdapter(pAdapter);
    }

    public void loadAdBanners()
    {
        ImageView iv;

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout);
        Picasso.with(getApplicationContext()).load(ad_banners.get(0)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(0);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout1);
        Picasso.with(getApplicationContext()).load(ad_banners.get(1)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(1);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout2);
        Picasso.with(getApplicationContext()).load(ad_banners.get(2)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(2);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout3);
        Picasso.with(getApplicationContext()).load(ad_banners.get(3)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(3);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });


        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout4);
        Picasso.with(getApplicationContext()).load(ad_banners.get(4)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(4);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout5);
        Picasso.with(getApplicationContext()).load(ad_banners.get(5)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),allProducts.class);
                i.putExtra("URL","mens_shirts");
                startActivity(i);
            }
        });

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout6);
        Picasso.with(getApplicationContext()).load(ad_banners.get(6)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),allProducts.class);
                i.putExtra("URL","mens_footwear");
                startActivity(i);
            }
        });

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout7);
        Picasso.with(getApplicationContext()).load(ad_banners.get(7)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(7);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });


        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout8);
        Picasso.with(getApplicationContext()).load(ad_banners.get(8)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(8);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout9);
        Picasso.with(getApplicationContext()).load(ad_banners.get(9)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(9);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout10);
        Picasso.with(getApplicationContext()).load(ad_banners.get(10)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(10);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout11);
        Picasso.with(getApplicationContext()).load(ad_banners.get(11)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),allProducts.class);
                i.putExtra("URL","mens_shirts");
                startActivity(i);
            }
        });


        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout12);
        Picasso.with(getApplicationContext()).load(ad_banners.get(12)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(12);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout13);
        Picasso.with(getApplicationContext()).load(ad_banners.get(13)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(13);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        iv = (ImageView)findViewById(R.id.ad_banner_relative_layout14);
        Picasso.with(getApplicationContext()).load(ad_banners.get(14)).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ad_banner_links.get(14);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
    }

    public void goToHomePage(View view) {
    }

    public void goToDealsPage(View view)
    {
        Intent i = new Intent(this,DealsPage.class);
        startActivity(i);
    }

    public void goToCouponsPage(View view)
    {
        Intent i = new Intent(this,CouponsPage2.class);
        startActivity(i);
    }

    public void goToProfilePage(View view)
    {
        Intent i = new Intent(this,ProfilePage.class);
        startActivity(i);
    }

    public void goToLoginPage(View view)
    {
        Intent i = new Intent(this,LoginPage.class);
        startActivity(i);
    }
}

package com.example.siddhant.pricyfy;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class allProducts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int layout=1;
    private static final String TAG = allProducts.class.getSimpleName();
    private List<Product> products;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayout;
    private ProductsAdapterAllProductsPage allProductsadapter;
    private ProductsAdapterHomePage homeAdapter;
    private ProgressBar spinner;
    private String typeOfProducts;
    private String url  = "https://pricyfy.com/app_all_products.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products_parent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(null);

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
            typeOfProducts=extras.getString("URL");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        products = new ArrayList<>();
        getProductsFromDB(0);
       /* switch(typeOfProducts)
        {
            case "mobiles" : case "mobile-phones": getProductsFromDB(810493);break;
            case "laptops" : getProductsFromDB(5306);break;
            case "cameras" : getProductsFromDB(538);break;
            case "tv"      : getProductsFromDB(0);break;
            case "men"     : getProductsFromDB(95698);break;
            case "women"   : getProductsFromDB(78908);break;
            case "others"  : getProductsFromDB(99852);break;
            case "smartwatches" : getProductsFromDB(0);break;
            case "furnitures" : getProductsFromDB(0);break;
            case "mens_accessories" : getProductsFromDB(0);break;
            case "tablets" : getProductsFromDB(0);break;
        }*/

        gridLayout = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayout);
        allProductsadapter = new ProductsAdapterAllProductsPage(this, products);

        recyclerView.setAdapter(allProductsadapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (gridLayout.findLastCompletelyVisibleItemPosition() == products.size() - 1)
                {
                    getProductsFromDB(products.get(products.size() - 1).getId());

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_products, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return super.onCreateOptionsMenu(menu);

    }

    private void getProductsFromDB(int id) {

        AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                spinner = (ProgressBar)findViewById(R.id.progressBar1);
                spinner.getIndeterminateDrawable().setColorFilter(spinner.getResources().getColor(R.color.pricify_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                spinner.setVisibility(View.VISIBLE);

            }

            @Override
            protected Void doInBackground(Integer... productIds) {

                HttpHandler sh = new HttpHandler();
                url=url+"?type="+typeOfProducts+"&id="+productIds[0];
                String jsonStr = sh.makeServiceCall(url);
                //Request request = new Request.Builder()
                 //       .url(url + productIds[0] + "&type=" + typeOfProducts)
                 //       .build();
              //  OkHttpClient client = new OkHttpClient();
                try {
                    //Response response = client.newCall(request).execute();

    //                JSONArray array = new JSONArray(response.body().string());
                    JSONArray array = new JSONArray(jsonStr);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        Product product = new Product(object.getInt("id"), object.getString("product_name"),
                                object.getString("features"), object.getInt("flipkart_price"), object.getString("image_link"),object.getDouble("seller_rating"),object.getInt("mrp"),object.getInt("discount"));
                        allProducts.this.products.add(product);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                spinner.setVisibility(View.GONE);
                if(layout==1)
                {
                    allProductsadapter.notifyDataSetChanged();
                }
                else homeAdapter.notifyDataSetChanged();
            }
        };
        asyncTask.execute(id);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home)
        {
            onBackPressed();
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
            Intent i = new Intent(this,CouponsPage.class);
            startActivity(i);
        }
        else if(id == R.id.deals)
        {
            Intent i = new Intent(this,DealsPage.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        drawer.closeDrawers();
        return true;
    }


    public void changeLayout(MenuItem item)
    {
        if(layout==1) {
            gridLayout = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(gridLayout);

            homeAdapter = new ProductsAdapterHomePage(this, products);

            recyclerView.setAdapter(homeAdapter);
            item.setIcon(R.drawable.list_icon);
            layout=2;

        }
        else
        {
            gridLayout = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(gridLayout);

            allProductsadapter = new ProductsAdapterAllProductsPage(this, products);

            recyclerView.setAdapter(allProductsadapter);

            item.setIcon(R.drawable.grid_icon);
            layout=1;
        }
    }

    public void goToHomePage(View view) {
        Intent i = new Intent(this,splashScreen.class);
        i.putExtra("abc","abc");
        startActivity(i);
    }

    public void goToDealsPage(View view)
    {
        Intent i = new Intent(this,DealsPage.class);
        startActivity(i);
    }

    public void goToCouponsPage(View view)
    {
        Intent i = new Intent(this,CouponsPage.class);
        startActivity(i);
    }

    public void goToProfilePage(View view)
    {
        Intent i = new Intent(this,ProfilePage.class);
        startActivity(i);
    }
}
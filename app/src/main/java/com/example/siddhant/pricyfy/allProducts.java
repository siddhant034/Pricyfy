package com.example.siddhant.pricyfy;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
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
import android.view.Menu;
import android.view.MenuItem;
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

    private static final String TAG = allProducts.class.getSimpleName();
    private List<Product> products;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayout;
    private ProductsAdapter adapter;
    private ProgressDialog pDialog;
    private String typeOfProducts;
    private String url  = "https://pricyfy.com/app_all_products.php?id=";

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


        Bundle extras = getIntent().getExtras();
        if(extras!=null)
            typeOfProducts=extras.getString("URL");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        products = new ArrayList<>();
        switch(typeOfProducts)
        {
            case "mobiles" : getProductsFromDB(810493);break;
            case "laptops" : getProductsFromDB(5306);break;
            case "cameras" : getProductsFromDB(538);break;
            case "tv"      : getProductsFromDB(0);break;
            case "men"     : getProductsFromDB(95698);break;
            case "women"   : getProductsFromDB(78908);break;
            case "others"  : getProductsFromDB(99852);
        }

        gridLayout = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayout);

        adapter = new ProductsAdapter(this, products,1);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (gridLayout.findLastCompletelyVisibleItemPosition() == products.size() - 1) {
                    getProductsFromDB(products.get(products.size() - 1).getId());
                }

            }
        });
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

    private void getProductsFromDB(int id) {

        AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(allProducts.this);
                pDialog.setMessage("Loading products. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Integer... productIds) {

                Request request = new Request.Builder()
                        .url(url + productIds[0] + "&type=" + typeOfProducts)
                        .build();
                OkHttpClient client = new OkHttpClient();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        Product product = new Product(object.getInt("id"), object.getString("product_name"),
                                object.getString("features"), object.getInt("mrp"), object.getString("image_link"),object.getDouble("seller_rating"));
                        allProducts.this.products.add(product);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                pDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        };
        asyncTask.execute(id);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}



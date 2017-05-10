package com.example.siddhant.pricyfy;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private String query;
    private GridLayoutManager gridLayout;
    private RecyclerView recyclerView;
    private ArrayList<Product> products;
    private ProductsAdapterAllProductsPage allProductsadapter;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable_parent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        products = new ArrayList<>();
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
            TextView tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.GONE);
            tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText(name);
            tv.setClickable(false);
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
            TextView tv = (TextView)navHeaderView.findViewById(R.id.tv3);
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
            TextView tv = (TextView)navHeaderView.findViewById(R.id.tv3);
            tv.setVisibility(View.VISIBLE);
            tv = (TextView)navHeaderView.findViewById(R.id.tv2);
            tv.setText("Sign In / ");
            tv.setClickable(true);
        }

        ImageView iv = (ImageView)findViewById(R.id.iv_company_logo);
        Picasso.with(getApplicationContext()).load(R.drawable.company_logo).into(iv);
        if(!isSignedInFb && !isSignedInGoogle)
            LogoutButton();    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        ImageView searchClose = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(R.color.black_overlay);
        return true;
    }
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

    void doMySearch(String q)
    {

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
            String url = "https://pricyfy.com/app_search.php?query="+query;
            String jsonStr = sh.makeServiceCall(url);
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Product newProduct = new Product(object.getInt("id"), object.getString("product_name"), object.getString("features"), object.getInt("flipkart_price"), object.getString("image_link"),object.getDouble("seller_rating"),object.getInt("mrp"),object.getInt("discount"));
                    products.add(newProduct);
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
            spinner.setVisibility(View.INVISIBLE);
            gridLayout = new GridLayoutManager(getApplicationContext(),1);
            recyclerView.setLayoutManager(gridLayout);
            allProductsadapter = new ProductsAdapterAllProductsPage(getApplicationContext(), products);
            recyclerView.setAdapter(allProductsadapter);
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

    public void signOut(MenuItem view)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();
        editor.putBoolean("signedInFb", false);
        editor.putBoolean("signedInGoogle",false);
        editor.commit();
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



package com.example.siddhant.pricyfy;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class allProducts extends AppCompatActivity {

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
        setContentView(R.layout.activity_all_products);

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

        adapter = new ProductsAdapter(this, products);

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
                adapter.notifyDataSetChanged();
                pDialog.dismiss();
            }
        };
        asyncTask.execute(id);
    }
}



package com.example.siddhant.pricyfy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class dealsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deals_elements);
    //    WebView browser = (WebView)findViewById(R.id.wv);
//        browser.setWebViewClient(new MyBrowser());
  //      browser.loadUrl("http://m.pricyfy.com/single-product.html");
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }
}

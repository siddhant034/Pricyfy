package com.example.siddhant.pricyfy;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import static android.widget.Toast.*;

public class ProductsAdapterHomePage extends RecyclerView.Adapter<ProductsAdapterHomePage.ViewHolder>{

    private Context context;
    private List<Product> products;
    private int id;

    public ProductsAdapterHomePage(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_elements_home_screen, parent, false);
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        itemView.setLayoutParams(new RecyclerView.LayoutParams(width/2,ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        Picasso.with(context).load(products.get(position).getImageLink()).into(holder.imageView);
        holder.lowest_price.setText("Rs. " + String.format("%d",products.get(position).getLowestPrice()) + "(" + Integer.toString(products.get(position).getDiscount()) + "% off)" );
        String title = products.get(position).getTitle();
        if(title.length()<=25) holder.productTitle.setText(products.get(position).getTitle());
        else holder.productTitle.setText(products.get(position).getTitle().substring(0,24) + "...");
        holder.mrp.setText("Rs. " + Integer.toString(products.get(position).getMrp()));
        holder.mrp.setPaintFlags(holder.mrp.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.ratingBar.setRating(((float) products.get(position).getSeller_rating()));
        holder.lowest_price.setText("Rs. " + Integer.toString(products.get(position).getLowestPrice()));
        holder.discount.setText(Integer.toString(products.get(position).getDiscount()) + "% Off");
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView productTitle;
        public ImageView imageView;
        public RatingBar ratingBar;
        public TextView mrp;
        public TextView lowest_price;
        public TextView discount;
        public CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            productTitle = (TextView) itemView.findViewById(R.id.itemName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rating);
            mrp = (TextView) itemView.findViewById(R.id.mrp);
            lowest_price = (TextView)itemView.findViewById(R.id.lowest_price);
            discount = (TextView)itemView.findViewById(R.id.discount);
            cv = (CardView)itemView.findViewById(R.id.cardview_home);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            Intent i = new Intent(context,SingleProduct.class);
            int pos = getAdapterPosition();
            Log.i("pos=",Integer.toString(pos));
            id=products.get(pos).getId();
            Log.i("id=",Integer.toString(id));
            i.putExtra("id",id);
            context.startActivity(i);
        }
    }
}

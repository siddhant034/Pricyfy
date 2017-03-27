package com.example.siddhant.pricyfy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private Context context;
    private List<Product> products;

    public ProductsAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_elements,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.productTitle.setText(products.get(position).getTitle());
        String desc = products.get(position).getDescription();
        if(desc.length()>50) holder.productDescription.setText(desc.substring(0,50));
        else holder.productDescription.setText(desc);
        holder.productPrice.setText("Rs. " + String.format("%d",products.get(position).getPrice()));
        holder.ratingBar.setRating(((float) products.get(position).getSeller_rating()));
        Glide.with(context).load(products.get(position).getImageLink()).into(holder.imageView);
    }



    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView productTitle;
        public TextView productDescription;
        public ImageView imageView;
        public TextView productPrice;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            productTitle = (TextView) itemView.findViewById(R.id.itemName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            productDescription = (TextView) itemView.findViewById(R.id.itemDesc);
            productPrice = (TextView)itemView.findViewById(R.id.cost);
            imageView.setOnClickListener(this);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rating);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
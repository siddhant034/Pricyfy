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

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<Coupons> coupons;
    private int id;

    public CouponsAdapter(Context context, ArrayList<Coupons> coupons) {
        this.context = context;
        this.coupons = coupons;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupons_elements, parent, false);
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        itemView.setLayoutParams(new RecyclerView.LayoutParams(width/2,height/4));
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        String str = coupons.get(position).getImage_link();
        if(!str.isEmpty())
            Picasso.with(context).load(coupons.get(position).getImage_link()).into(holder.imageView);
        String title = coupons.get(position).getTitle();
        holder.couponTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView couponTitle;
        public ImageView imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            couponTitle = (TextView) itemView.findViewById(R.id.tv_coupons);
            imageView = (ImageView) itemView.findViewById(R.id.iv_coupons);
        }

        @Override
        public void onClick(View v)
        {
        }
    }
}

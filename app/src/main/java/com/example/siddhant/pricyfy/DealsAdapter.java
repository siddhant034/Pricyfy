package com.example.siddhant.pricyfy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by siddhant on 2/4/17.
 */

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {

        private Context context;
        private List<Deal> deals;
        private int type;

        public DealsAdapter(Context context, List<Deal> deals, int type) {
            this.context = context;
            this.deals = deals;
            this.type = type;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView;
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_elements_home_screen, parent, false);
            WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            int width = windowManager.getDefaultDisplay().getWidth();
            int height = windowManager.getDefaultDisplay().getHeight();
            if(type==0)
                itemView.setLayoutParams(new RecyclerView.LayoutParams(width/2,height/5));
            else
                itemView.setLayoutParams(new RecyclerView.LayoutParams(width/2,height/3+10));
            return new DealsAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final DealsAdapter.ViewHolder holder, int position) {

            holder.dealTitle.setText(deals.get(position).getDeal_name());
            Glide.with(context).load(deals.get(position).getDeal_image()).into(holder.imageView);
        }


        @Override
        public int getItemCount() {
            return deals.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public TextView dealTitle;
            public ImageView imageView;
            public CardView cv;


            public ViewHolder(View itemView) {
                super(itemView);
                dealTitle = (TextView) itemView.findViewById(R.id.text_view_deals);
                imageView = (ImageView) itemView.findViewById(R.id.image_view_deals);
                cv = (CardView)itemView.findViewById(R.id.cardview_allProducts);
                cv.setOnClickListener(this);
            }

            @Override
            public void onClick(View v)
            {
                int pos = getAdapterPosition();
                String url = deals.get(pos).getDeal_link();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        }
    }

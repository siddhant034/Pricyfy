package com.example.siddhant.pricyfy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        int type;           //0 for homescreen 1 for dealsPage

        public DealsAdapter(Context context, List<Deal> deals, int type) {
            this.context = context;
            this.deals = deals;
            this.type = type;
        }

        @Override
        public DealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView;
            if(type==0)
            {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_elements_home_screen, parent, false);
            }
            else
            {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_elements_home_screen, parent, false);
            }
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

            public ViewHolder(View itemView) {
                super(itemView);
                dealTitle = (TextView) itemView.findViewById(R.id.text_view_deals);
                imageView = (ImageView) itemView.findViewById(R.id.image_view_deals);
            }

            @Override
            public void onClick(View v) {
            }
        }
    }

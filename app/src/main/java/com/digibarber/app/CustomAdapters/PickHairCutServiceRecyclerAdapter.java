package com.digibarber.app.CustomAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.digibarber.app.Beans.ServiceList;
import com.digibarber.app.R;
import com.digibarber.app.activities.PickServiceActivity;

import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 18/9/17.
 */

public class PickHairCutServiceRecyclerAdapter extends RecyclerView.Adapter<PickHairCutServiceRecyclerAdapter.MyViewHolder> {

    public interface HairServiceListner {
        public void onClick(int position);
    }
    HairServiceListner hairServiceListner;
    ArrayList<ServiceList> alServiceHair;
    Context context;
    ArrayList<Boolean> alServiceHairTemp;



    public PickHairCutServiceRecyclerAdapter(Context context, ArrayList<ServiceList> alServiceHair, ArrayList<Boolean> alServiceHairTemp, HairServiceListner hairServiceListner) {
        this.context = context;
        this.alServiceHair = alServiceHair;
        this.alServiceHairTemp = alServiceHairTemp;
        this.hairServiceListner = hairServiceListner;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pick_haircut_service, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        holder.name.setText(alServiceHair.get(position).sub_category_name);
        holder.itemView.post(new Runnable() {
            @Override
            public void run() {

                int cellWidth = holder.itemView.getWidth();// this will give you cell width dynamically
                int cellHeight = holder.itemView.getHeight();// this will give you cell height dynamically
                if (context instanceof PickServiceActivity) {
                    ((PickServiceActivity) context).WidthOfAdpterItem(cellWidth);
                }


            }
        });

        //holder.image.setImageResource((int)personImages.get(position));
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.name.setBackgroundResource(R.mipmap.pick_service_blue_selc);
                holder.name.setTextColor(context.getResources().getColor(R.color.colorWhite));
                hairServiceListner.onClick(position);
            }
        });

        if (alServiceHairTemp.get(position)) {
            holder.name.setBackgroundResource(R.mipmap.pick_service_blue_selc);
            holder.name.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.name.setBackgroundResource(R.mipmap.pick_service_blue_unsel);
            holder.name.setTextColor(context.getResources().getColor(R.color.doneTextColor));
        }


    }


    @Override
    public int getItemCount() {
        return alServiceHair.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;

        //ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);


        }
    }

}

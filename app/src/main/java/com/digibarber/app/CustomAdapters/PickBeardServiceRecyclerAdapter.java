package com.digibarber.app.CustomAdapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digibarber.app.Beans.ServiceList;
import com.digibarber.app.R;
import com.digibarber.app.activities.PickServiceActivity;

import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 19/9/17.
 */

public class PickBeardServiceRecyclerAdapter extends RecyclerView.Adapter<PickBeardServiceRecyclerAdapter.MyHolderView> {

    ArrayList<ServiceList> alServiceBeard;
    Context context;
    public interface PickBreadServiceListner {
        public void onClick(int position);
    }
    PickBreadServiceListner pickBreadServiceListner;
    ArrayList<Boolean> alServiceBeardTemp;

    public PickBeardServiceRecyclerAdapter(PickServiceActivity pickServiceActivity, ArrayList<ServiceList> alServiceBeard, ArrayList<Boolean> alServiceBeardTemp,PickBreadServiceListner pickBreadServiceListner) {

        this.context = pickServiceActivity;
        this.alServiceBeard = alServiceBeard;
        this.alServiceBeardTemp = alServiceBeardTemp;
        this.pickBreadServiceListner=pickBreadServiceListner;

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
    public MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View vv = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pick_beard_service, parent, false);
        MyHolderView mhv = new MyHolderView(vv);

        return mhv;
    }

    @Override
    public void onBindViewHolder(final MyHolderView holder,final int position) {
        holder.name.setText(alServiceBeard.get(position).sub_category_name);
        //holder.image.setImageResource((int)persImg.get(position));
        holder.itemView.post(new Runnable()
        {
            @Override
            public void run()
            {

                int cellWidth = holder.itemView.getWidth();// this will give you cell width dynamically
                int cellHeight = holder.itemView.getHeight();// this will give you cell height dynamically
                if(context instanceof PickServiceActivity){
                    ((PickServiceActivity)context).WidthOfAdpterItemBeaRD(cellWidth);
                }


            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.name.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.name.setBackgroundResource(R.mipmap.pick_service_grey_selec);
                pickBreadServiceListner.onClick(position);
            }
        });

        if (alServiceBeardTemp.get(position)) {
            holder.name.setBackgroundResource(R.mipmap.pick_service_grey_selec);
            holder.name.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.name.setBackgroundResource(R.mipmap.pick_service_grey_unse);
            holder.name.setTextColor(context.getResources().getColor(R.color.loginTextColor));
        }

    }


    @Override
    public int getItemCount() {
        return alServiceBeard.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        TextView name;
        //ImageView image;

        public MyHolderView(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameSec);
            //image = itemView.findViewById(R.id.imageSec);
        }
    }
}

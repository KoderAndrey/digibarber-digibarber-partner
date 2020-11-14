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
 * Created by DIGIBARBER LTD on 20/9/17.
 */

public class PickPamertreatmentServiceRecyclerAdapter extends RecyclerView.Adapter<PickPamertreatmentServiceRecyclerAdapter.MyHolderV> {
    private Context context;
    ArrayList<ServiceList> alServicePamper;
    ArrayList<Boolean> alServicePamperTemp;
    public interface PickPamertreatmentServiceListener {
        public void onClick(int position);
    }
    PickPamertreatmentServiceListener pickPamertreatmentServiceListener;
    public PickPamertreatmentServiceRecyclerAdapter(PickServiceActivity pickServiceActivity, ArrayList<ServiceList> alServicePamper, ArrayList<Boolean> alServicePamperTemp,PickPamertreatmentServiceListener pickPamertreatmentServiceListener) {

        this.context = pickServiceActivity;
        this.alServicePamper = alServicePamper;
        this.alServicePamperTemp = alServicePamperTemp;
        this.pickPamertreatmentServiceListener=pickPamertreatmentServiceListener;

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
    public MyHolderV onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pick_pamper_treatment, parent, false);
        MyHolderV mhv = new MyHolderV(view);

        return mhv;
    }

    @Override
    public void onBindViewHolder(final MyHolderV holder, final int position) {
        holder.itemView.post(new Runnable()
        {
            @Override
            public void run()
            {

                int cellWidth = holder.itemView.getWidth();// this will give you cell width dynamically
                int cellHeight = holder.itemView.getHeight();// this will give you cell height dynamically
                if(context instanceof PickServiceActivity){
                    ((PickServiceActivity)context).WidthOfAdpterItemParmertment(cellWidth);
                }


            }
        });

        holder.name.setText(alServicePamper.get(position).sub_category_name);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.name.setBackgroundResource(R.mipmap.pick_service_blue_selc);
                holder.name.setTextColor(context.getResources().getColor(R.color.colorWhite));
                pickPamertreatmentServiceListener.onClick(position);
            }
        });
        if (alServicePamperTemp.get(position)) {
            holder.name.setBackgroundResource(R.mipmap.pick_service_blue_selc);
            holder.name.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.name.setBackgroundResource(R.mipmap.pick_service_blue_unsel);
            holder.name.setTextColor(context.getResources().getColor(R.color.doneTextColor));
        }
    }

    @Override
    public int getItemCount() {
        return alServicePamper.size();
    }

    public class MyHolderV extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;

        //ImageView image;
        public MyHolderV(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.nameThrd);
            //image = (ImageView) itemView.findViewById(R.id.image);


        }
    }

}

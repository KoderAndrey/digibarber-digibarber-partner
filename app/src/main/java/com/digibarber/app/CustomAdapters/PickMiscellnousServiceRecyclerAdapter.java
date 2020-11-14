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

public class PickMiscellnousServiceRecyclerAdapter extends RecyclerView.Adapter<PickMiscellnousServiceRecyclerAdapter.MyViewhold> {
    private Context context;
    private ArrayList<ServiceList> alServiceMecellnous;

    ArrayList<Boolean> alServiceMecellnousTemp;
    public interface PickMiscellnousServiceListner {
        public void onClick(int position);
    }
    PickMiscellnousServiceListner pickMiscellnousServiceListner;
    public PickMiscellnousServiceRecyclerAdapter(PickServiceActivity pickServiceActivity, ArrayList<ServiceList> alServiceMecellnous, ArrayList<Boolean> alServiceMecellnousTemp,PickMiscellnousServiceListner pickMiscellnousServiceListner) {

        this.context = pickServiceActivity;
        this.alServiceMecellnous = alServiceMecellnous;
        this.alServiceMecellnousTemp=alServiceMecellnousTemp;
        this.pickMiscellnousServiceListner=pickMiscellnousServiceListner;
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
    public MyViewhold onCreateViewHolder(ViewGroup parent, int viewType) {

        View vv = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pick_miscellnous_service, parent, false);
        MyViewhold mvh = new MyViewhold(vv);

        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewhold holder, final int position) {

        holder.name.setText(alServiceMecellnous.get(position).sub_category_name);
        holder.itemView.post(new Runnable()
        {
            @Override
            public void run()
            {

                int cellWidth = holder.itemView.getWidth();// this will give you cell width dynamically
                int cellHeight = holder.itemView.getHeight();// this will give you cell height dynamically
                if(context instanceof PickServiceActivity){
                    ((PickServiceActivity)context).WidthOfAdpterItemMiscellnous(cellWidth);
                }


            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.name.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.name.setBackgroundResource(R.mipmap.pick_service_grey_selec);
                pickMiscellnousServiceListner.onClick(position);
            }
        });
        if (alServiceMecellnousTemp.get(position)) {
            holder.name.setBackgroundResource(R.mipmap.pick_service_grey_selec);
            holder.name.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.name.setBackgroundResource(R.mipmap.pick_service_grey_unse);
            holder.name.setTextColor(context.getResources().getColor(R.color.loginTextColor));
        }
    }

    @Override
    public int getItemCount() {
        return alServiceMecellnous.size();
    }

    public class MyViewhold extends RecyclerView.ViewHolder
    {
        TextView name;
        public MyViewhold(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.nameFour);
        }
    }

}

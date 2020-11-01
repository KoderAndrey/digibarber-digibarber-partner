package com.digibarber.app.CustomAdapters;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digibarber.app.Beans.BookTimeChart;
import com.digibarber.app.Interfaces.SelectedPositionBookingTIme;
import com.digibarber.app.R;

import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 3/17/2017.
 */

public class BookTimeChartRecyclerAdapter extends RecyclerView.Adapter<BookTimeChartRecyclerAdapter.ViewHolder> {


    public int selectedPos = -5;
    public String isConfirmation = "";

    //private final List<DummyItem> mValues;
    public ArrayList<BookTimeChart> Service_list;
    public Context context;
    public SelectedPositionBookingTIme objSelectedPositionBookingTIme;

    public BookTimeChartRecyclerAdapter(Context con, ArrayList<BookTimeChart> Service_list, SelectedPositionBookingTIme objSelectedPositionBookingTIme) {
        context = con;
        this.Service_list = Service_list;
        this.objSelectedPositionBookingTIme = objSelectedPositionBookingTIme;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_book_time_chart, parent, false);
        return new ViewHolder(view);
    }

    public void ChangeSelectedSlot(int pos, String isConfirmation) {
        this.selectedPos = pos;
        this.isConfirmation = isConfirmation;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //System.out.println(" ** Holder Position ** "+holder.getAdapterPosition());

        if (position == selectedPos) {
            holder.iv_slot_disabled.setVisibility(View.GONE);
            holder.tv_time_available.setVisibility(View.VISIBLE);
            if (isConfirmation.equalsIgnoreCase("ConfTimeOn")) {
                holder.tv_time_available.setBackgroundResource(R.drawable.selected_confrim);
                holder.tv_time_available.setTextColor(Color.parseColor("#ffffff"));
            } else if (isConfirmation.equalsIgnoreCase("ConfTimeOff")) {
                holder.tv_time_available.setBackgroundResource(R.drawable.selected_autoconfirm);
                holder.tv_time_available.setTextColor(Color.parseColor("#ffffff"));
            } else if (isConfirmation.equalsIgnoreCase("PreTimeOn")) {
                holder.tv_time_available.setBackgroundResource(R.drawable.premuim_selected);
                holder.tv_time_available.setTextColor(Color.parseColor("#ffffff"));
            } else {

            }
        } else {
            if (Service_list.get(position).LunchTime.equalsIgnoreCase("LunchTimeOn")) {
                holder.iv_slot_disabled.setVisibility(View.VISIBLE);
                holder.tv_time_available.setBackgroundResource(0);
                holder.tv_time_available.setTextColor(Color.parseColor("#8031363B"));
                if (position == Service_list.size() - 1) {
                    holder.iv_slot_disabled.setVisibility(View.GONE);
                    holder.tv_time_available.setVisibility(View.GONE);
                }
            } else {
                holder.iv_slot_disabled.setVisibility(View.GONE);
                if (Service_list.get(position).ConfirmationTime.equalsIgnoreCase("ConfTimeOn")) {
                    holder.tv_time_available.setBackgroundResource(R.drawable.time_availble_image);
                    holder.tv_time_available.setTextColor(Color.parseColor("#8031363B"));
                } else if (Service_list.get(position).ConfirmationTime.equalsIgnoreCase("ConfTimeOff")) {
                    holder.tv_time_available.setBackgroundResource(R.drawable.autoconfirm_time_slot_image);
                    holder.tv_time_available.setTextColor(Color.parseColor("#118FEB"));
                }else if (Service_list.get(position).ConfirmationTime.equalsIgnoreCase("PreTimeOn")) {
                    holder.tv_time_available.setBackgroundResource(R.drawable.premium_slot);
                    holder.tv_time_available.setTextColor(Color.parseColor("#FFF1CE41"));
                } else {

                }
            }
        }
        holder.tv_time_available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) view.getTag();
                if (Service_list.get(pos).LunchTime.equalsIgnoreCase("LunchTimeOn")) {
                } else {
                    String isConfirmation = Service_list.get(pos).ConfirmationTime;
                    objSelectedPositionBookingTIme.SelectedPos(pos, isConfirmation);
                }
            }
        });
        holder.tv_time_available.setTag(position);
        holder.tv_time_available.setText(Service_list.get(position).time);

    }

    @Override
    public int getItemCount() {
        return Service_list.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_time_available;
        public final ImageView iv_slot_disabled;

        ViewHolder(View view) {
            super(view);
            tv_time_available = (TextView) view.findViewById(R.id.tv_time_available);
            iv_slot_disabled = (ImageView) view.findViewById(R.id.iv_slot_disabled);
            //text_miles.setTypeface(custom_font1);

        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }

}

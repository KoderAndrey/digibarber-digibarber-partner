package com.digibarber.app.CustomAdapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digibarber.app.Beans.DayWise;
import com.digibarber.app.R;
import com.digibarber.app.activities.UnconfermedBookingActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by DIGIBARBER LTD on 10/10/17.
 */

public class UnconfirmedBookingAdapter extends RecyclerView.Adapter<UnconfirmedBookingAdapter.MyViewHolder> {
    ArrayList<DayWise> arrayList;
    Context context;
    private DateFormat formater;
    String curentdate = "";

    public UnconfirmedBookingAdapter(UnconfermedBookingActivity unconfermedBookingActivity, ArrayList<DayWise> daywise_array_list) {
        context = unconfermedBookingActivity;
        arrayList = daywise_array_list;
        formater = new SimpleDateFormat("EEEE, dd MMM yyyy");
        Calendar calCurent = Calendar.getInstance();
        curentdate = formater.format(calCurent.getTime());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_unconfirmedbooking, parent, false);
        MyViewHolder mvh = new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        try {
            if (curentdate != null && !curentdate.equalsIgnoreCase("")) {
                if (curentdate.equalsIgnoreCase(arrayList.get(position).date)) {
                    holder.tv_today.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_today.setVisibility(View.GONE);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            holder.tv_today.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            holder.tv_today.setVisibility(View.GONE);
        }
        if (position == 0) {
            holder.tv_date.setVisibility(View.VISIBLE);
            holder.tv_today.setVisibility(View.VISIBLE);
            holder.tv_date.setText(arrayList.get(position).date);
        } else {
            String currentdate = arrayList.get(position).date;
            String previous = arrayList.get(position - 1).date;
            if (currentdate.equalsIgnoreCase(previous)) {
                holder.tv_date.setVisibility(View.GONE);
                holder.tv_today.setVisibility(View.GONE);
            } else {
                holder.tv_date.setVisibility(View.VISIBLE);
                holder.tv_today.setVisibility(View.VISIBLE);
                holder.tv_date.setText(arrayList.get(position).date);
            }
        }
        holder.tv_time.setText(arrayList.get(position).getBooking_time());
        holder.tv_serviceprovide.setText(arrayList.get(position).getServices());
        holder.tv_client_name.setText(arrayList.get(position).getUser_name());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_serviceprovide, tv_time, tv_client_name, tv_today;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_serviceprovide = (TextView) itemView.findViewById(R.id.tv_serviceprovide);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_client_name = (TextView) itemView.findViewById(R.id.tv_client_name);
            tv_today = (TextView) itemView.findViewById(R.id.tv_today);

        }
    }
}
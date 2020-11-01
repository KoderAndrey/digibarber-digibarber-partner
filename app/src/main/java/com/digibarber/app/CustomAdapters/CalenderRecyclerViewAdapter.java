package com.digibarber.app.CustomAdapters;

import android.app.AlertDialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digibarber.app.Beans.BookingListDayWise;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.Interfaces.CalenderListCallbacks;
import com.digibarber.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Handler;

/**
 * Created by DIGIBARBER LTD on 29/9/17.
 */

public class CalenderRecyclerViewAdapter extends RecyclerView.Adapter<CalenderRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BookingListDayWise> aryList;
    CalenderListCallbacks calenderListCallbacks;
    private String selectedDate;
    private String currentDate;
    Date dateSelected = null;
    Date dateCurrent = null;
    SimpleDateFormat Cal_Format = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");

    public CalenderRecyclerViewAdapter(Context context, ArrayList<BookingListDayWise> aryList, CalenderListCallbacks calenderListCallbacks, String selectedDate, String currentDate) {
        this.context = context;
        this.aryList = aryList;
        this.calenderListCallbacks = calenderListCallbacks;
        this.selectedDate = selectedDate;
        this.currentDate = currentDate;
        String currentTime = Constants.getCurrentTime_hh_mm(Constants.getCalenderInstance());
        try {
            dateSelected = Cal_Format.parse(selectedDate + " " + currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);
        MyViewHolder mvh = new MyViewHolder(view);
        return mvh;
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_service_name.setText(aryList.get(position).service_name);
        holder.tv_time.setText(aryList.get(position).booking_time);
        holder.tv_client_name.setText(aryList.get(position).user_name);

        if (aryList.get(position).is_confirmed.equalsIgnoreCase("0")) {
            //Unconfrimed
            holder.ll_layout.setEnabled(true);
            holder.iv_disable.setVisibility(View.GONE);
            holder.iv_roller.setVisibility(View.VISIBLE);
            holder.ll_background_rec.setVisibility(View.VISIBLE);
            holder.iv_roller.setBackgroundResource(R.mipmap.slidergrey);
            holder.ll_background_rec.setBackgroundResource(R.mipmap.upcoming_rec_grey);
        } else if (aryList.get(position).is_confirmed.equalsIgnoreCase("1")) {
            //Confimred
            holder.iv_roller.setBackgroundResource(R.mipmap.slidergreen);
            holder.ll_background_rec.setBackgroundResource(R.mipmap.upcoming_rec_green);
            holder.ll_layout.setEnabled(true);
            holder.iv_disable.setVisibility(View.GONE);
            holder.iv_roller.setVisibility(View.VISIBLE);
        } else if (aryList.get(position).is_confirmed.equalsIgnoreCase("2")) {
            //Deleted
            holder.ll_layout.setEnabled(true);
            holder.iv_disable.setVisibility(View.GONE);
            holder.iv_roller.setVisibility(View.VISIBLE);
        } else if (aryList.get(position).is_confirmed.equalsIgnoreCase("3")) {
            //Completed
            holder.ll_layout.setEnabled(false);
            holder.iv_disable.setVisibility(View.VISIBLE);
            holder.iv_action.setVisibility(View.GONE);
            holder.iv_roller.setVisibility(View.GONE);
            holder.ll_background_rec.setBackgroundResource(R.drawable.drawable_completed);

        } else if (aryList.get(position).is_confirmed.equalsIgnoreCase("4")) {
            //Active
            holder.ll_layout.setEnabled(true);
            holder.iv_roller.setVisibility(View.VISIBLE);
            holder.iv_roller.setBackgroundResource(R.mipmap.sliderblue);
            holder.ll_background_rec.setBackgroundResource(R.mipmap.upcoming_rec_blue);
        }

        holder.iv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aryList.get(position).is_confirmed.equalsIgnoreCase("0")  || aryList.get(position).is_confirmed.equalsIgnoreCase("1")) {
                    calenderListCallbacks.onclickAction(position);
                }else if (aryList.get(position).is_confirmed.equalsIgnoreCase("4")){
                    new AlertDialog.Builder(context)
                            .setTitle("Booking Started")
                            .setMessage("As you have started the booking it cannot be amended from this screen. Try amending in the booking viewer…")
                            .setNegativeButton("Ok",null)
                            .show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return aryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_service_name, tv_time, tv_client_name;
        ImageView iv_action;
        LinearLayout ll_layout;
        ImageView iv_roller;
        ImageView iv_disable;
        LinearLayout ll_background_rec;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_service_name = (TextView) itemView.findViewById(R.id.tv_service_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_client_name = (TextView) itemView.findViewById(R.id.tv_client_name);
            iv_action = (ImageView) itemView.findViewById(R.id.iv_action);
            ll_layout = (LinearLayout) itemView.findViewById(R.id.ll_layout);
            iv_roller = (ImageView) itemView.findViewById(R.id.iv_roller);
            iv_disable = (ImageView) itemView.findViewById(R.id.iv_disable);
            ll_background_rec = (LinearLayout) itemView.findViewById(R.id.ll_background_rec);
        }
    }
}
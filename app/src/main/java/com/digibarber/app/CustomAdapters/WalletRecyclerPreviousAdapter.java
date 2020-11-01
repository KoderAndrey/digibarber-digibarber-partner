package com.digibarber.app.CustomAdapters;

/**
 * Created by DIGIBARBER LTD on 3/10/17.
 */


import android.content.Context;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digibarber.app.Interfaces.CallBackWalletSubList;
import com.digibarber.app.R;
import com.digibarber.app.models.Payout;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by DIGIBARBER LTD on 16/9/17.
 */

public class WalletRecyclerPreviousAdapter extends RecyclerView.Adapter<WalletRecyclerPreviousAdapter.ViewHolder> {


    ArrayList<Payout> list;
    Context context;
    CallBackWalletSubList objCallBackWalletSubList;
    private int focusedItem = 0;
    Payout payout;

//    public WalletRecyclerPreviousAdapter(ArrayList<WalletDataRequest> cat_list, Context con, CallBackWalletSubList objCallBackWalletSubList) {
//        list = cat_list;
//        context = con;
//        this.objCallBackWalletSubList = objCallBackWalletSubList;
//    }

    public WalletRecyclerPreviousAdapter(ArrayList<Payout> cat_list, Context con, CallBackWalletSubList objCallBackWalletSubList) {
        list = cat_list;
        context = con;
        this.objCallBackWalletSubList = objCallBackWalletSubList;
    }
    public void clearData(){
        list.clear();
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_listing1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setSelected(focusedItem == position);
        holder.pendingCompletedLayout.setVisibility(View.VISIBLE);
          payout = list.get(position);

        Date date = new Date(payout.getCreated()*1000L);
        String pattern =  "dd MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String formattedDate = simpleDateFormat.format(date);
        String arrivalDate= formattedDate;
        String getstatus=payout.getStatus();
        holder.tv_status.setText("("+getstatus.substring(0,1).toUpperCase() + getstatus.substring(1).toLowerCase()+")");
        holder.tv_time_wirhdraws.setText("Expected date paid into bank: "+arrivalDate);
        holder.tv_date.setText(formattedDate);

        try {
                DecimalFormat format = new DecimalFormat("0.00");
            if(payout.getPrice()<0){
                holder.tv_paid_amount.setText("£ 0.00");
            }else{
                holder.tv_paid_amount.setText("£"+format.format(payout.getPrice()/*/100*/));
            }

        } catch (NumberFormatException e) {
            //holder.tv_paid_amount.setText(list.get(position).total_amount);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objCallBackWalletSubList.sonItemClick(position,list.get(position).getTransactionDetails());
            }
        });

    }
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        // Handle key up and key down and attempt to move selection
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                // Return false if scrolled to the bounds and allow focus to move off the list
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return tryMoveSelection(lm, -1);
                    }
                }

                return false;
            }
        });
    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int tryFocusItem = focusedItem + direction;

        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
            notifyItemChanged(focusedItem);
            lm.scrollToPosition(focusedItem);
            return true;
        }

        return false;
    }
    @Override
    public int getItemCount() {

        return list.size();

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;


        LinearLayout pendingCompletedLayout;
        final TextView tv_date, tv_status, tv_time_wirhdraws, tv_paid_amount,tv_payout_id;


        ViewHolder(View view) {
            super(view);
            mView = view;
            pendingCompletedLayout = (LinearLayout) view.findViewById(R.id.pendingCompletedLayout);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_payout_id = (TextView) view.findViewById(R.id.tv_payout_id);
            tv_time_wirhdraws = (TextView) view.findViewById(R.id.tv_time_wirhdraws);
            tv_paid_amount = (TextView) view.findViewById(R.id.tv_paid_amount);


        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
    public  String convertseconds(long totalSecs) {
        Date d = new Date(totalSecs * 1000);
        DateFormat df = new SimpleDateFormat("hh:mm");
        return df.format(d);

    }
    public void swapeItem(int fromPosition,int toPosition){
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }
}

package com.digibarber.app.CustomAdapters;

/**
 * Created by DIGIBARBER LTD on 3/10/17.
 */


import android.content.Context;
import android.graphics.Color;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digibarber.app.Beans.WalletRequestListResponse;
import com.digibarber.app.Interfaces.CallBackWalletSubList;
import com.digibarber.app.R;
import com.digibarber.app.models.Payout;
import com.digibarber.app.models.transactionDetails;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DIGIBARBER LTD on 16/9/17.
 */

public class WalletRecyclerAdapter extends RecyclerView.Adapter<WalletRecyclerAdapter.ViewHolder> {

    //private final List<DummyItem> mValues;

    ArrayList<transactionDetails> list = new ArrayList<transactionDetails>();
    /* ArrayList<SalesList> sales_list = new ArrayList<SalesList>();
     ArrayList<BuyersList> buyers_list = new ArrayList<BuyersList>();*/
    Context context;
    String tag;
    CallBackWalletSubList objCallBackWalletSubList;


    public WalletRecyclerAdapter(ArrayList<transactionDetails> cat_list, Context con, CallBackWalletSubList objCallBackWalletSubList) {
        list = cat_list;
        context = con;
        this.objCallBackWalletSubList = objCallBackWalletSubList;
    }

    public void setData(ArrayList<transactionDetails> data){
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false);


        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
         final transactionDetails payout = list.get(position);
        holder.tv_date.setText(payout.getBookingDate());
        holder.tv_service.setText(payout.getServices());
        holder.idd.setText("[ ID: "+payout.getTransactionId()+" ]");
        holder.tv_status.setText("£ "+payout.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objCallBackWalletSubList.sonItemClick(position,list);
            }
        });
    }
    public void clearData(){
        list.clear();
        notifyDataSetChanged();
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
        final TextView tv_date, tv_service, tv_status,idd;


        ViewHolder(View view) {
            super(view);
            mView = view;
            pendingCompletedLayout = (LinearLayout) view.findViewById(R.id.pendingCompletedLayout);
            tv_date = (TextView) view.findViewById(R.id.tv_book_date);
            tv_service = (TextView) view.findViewById(R.id.tv_service);
            //tv_payout_id = (TextView) view.findViewById(R.id.tv_payout_id);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            idd = (TextView) view.findViewById(R.id.idd);


          //  tv_paid_amount = (TextView) view.findViewById(R.id.tv_paid_amount);


        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  String convertseconds(long totalSecs) {
        return LocalTime.MIN.plusSeconds(totalSecs).toString();
    }
}

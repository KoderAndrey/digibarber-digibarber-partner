package com.digibarber.app.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.digibarber.app.Beans.WalletHistory;
import com.digibarber.app.Beans.walletHistoryItems;
import com.digibarber.app.R;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.zip.Inflater;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;



public class WalletHostoryAdapter extends RecyclerView.Adapter<WalletHostoryAdapter.ViewHolder> {

        ArrayList<WalletHistory> arrayList;
        Context context;
        boolean isHistory;

        ArrayList<String> monthAdded = new ArrayList<>();
        ArrayList<Integer> monthIndex = new ArrayList<>();

        public WalletHostoryAdapter(Context context, ArrayList<WalletHistory> arrayList,boolean isHistory) {
            this.arrayList = arrayList;
            this.isHistory = isHistory;
            this.context = context;
            }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = null;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
                return new ViewHolder(view);
                }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final WalletHistory walletHistory = arrayList.get(position);
            String id = walletHistory.item_values.get(0).booking_id;
            if (id != null){
                holder.idd.setText(id);
            }else {
                holder.idd.setText("");
            }
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isHistory && (!monthAdded.contains(walletHistory.month) || monthIndex.contains(position))  ){
                        holder.tv_month.setVisibility(View.VISIBLE);
                        if(!monthIndex.contains(position)){
                            monthIndex.add(position);
                        }
                        holder.tv_month.setText(walletHistory.month);
                        monthAdded.add(walletHistory.month);
                    }else{
                        holder.tv_month.setVisibility(View.GONE);
                    }
                }
            });
            holder.tv_book_date.setText(walletHistory.week);
            holder.tv_service.setText(walletHistory.item_values.get(0).services);
            holder.tv_status.setText("£" +walletHistory.item_values.get(0).price);
        }
        public void clearData(){
            arrayList.clear();
            notifyDataSetChanged();
        }
        @Override
        public int getItemCount() {
         return arrayList.size();
         }

        @Override
        public int getItemViewType(int position) {
                return position;
                }


        class ViewHolder extends RecyclerView.ViewHolder
        {
            final View mView;
            TextView tv_book_date, tv_service, tv_status, tv_deucted,idd,tv_month;



    ViewHolder(View view) {
        super(view);
        mView = view;
        tv_book_date = (TextView) view. findViewById(R.id.tv_book_date);
        tv_service = (TextView)  view.findViewById(R.id.tv_service);
        tv_month   = (TextView) view.findViewById(R.id.month);
        tv_status = (TextView)  view.findViewById(R.id.tv_status);
        tv_deucted = (TextView) view.findViewById(R.id.tv_deucted);
        idd = (TextView) view.findViewById(R.id.idd);
    }

    @Override
    public String toString() {
        return super.toString() + " '";
    }
}




}

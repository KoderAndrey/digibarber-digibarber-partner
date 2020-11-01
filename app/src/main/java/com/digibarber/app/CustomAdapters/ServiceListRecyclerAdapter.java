package com.digibarber.app.CustomAdapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.digibarber.app.Beans.ServicePriceTime;
import com.digibarber.app.Interfaces.EditServiceCallback;
import com.digibarber.app.R;

import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 3/17/2017.
 */

public class ServiceListRecyclerAdapter extends RecyclerView.Adapter<ServiceListRecyclerAdapter.ViewHolder> {

    ArrayList<ServicePriceTime> alImages;
    Context context;
    EditServiceCallback objEditServiceCallback;
    String Status = "Empty";

    public ServiceListRecyclerAdapter(ArrayList<ServicePriceTime> alImages, Context con, EditServiceCallback objEditServiceCallback) {
        context = con;
        this.alImages = alImages;
        this.objEditServiceCallback = objEditServiceCallback;
    }

    public ServiceListRecyclerAdapter(ArrayList<ServicePriceTime> alImages, Context con, String Status) {
        context = con;
        this.alImages = alImages;
        this.Status = Status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_service_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_service_name.setText(alImages.get(position).subName);
        holder.tv_price.setText("£" + alImages.get(position).subPrice.replace("£", ""));
        holder.tv_des.setText(alImages.get(position).subDes);

        if (Status.equalsIgnoreCase("Empty")) {
            holder.iv_edit.setVisibility(View.VISIBLE);
            holder.iv_cross.setVisibility(View.VISIBLE);
        } else {
            holder.iv_edit.setVisibility(View.GONE);
            holder.iv_cross.setVisibility(View.GONE);
        }

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objEditServiceCallback.callClick(position);
            }
        });

        holder.iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showPopupDialogChecking(position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return alImages.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        public final ImageView iv_edit;
        public final ImageView iv_cross;
        public final TextView tv_service_name;
        public final TextView tv_price;
        public final TextView tv_des;

        ViewHolder(View view) {
            super(view);
            mView = view;
            iv_edit = (ImageView) view.findViewById(R.id.iv_edit);
            iv_cross = (ImageView) view.findViewById(R.id.iv_cross);
            tv_service_name = (TextView) view.findViewById(R.id.tv_service_name);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_des = (TextView) view.findViewById(R.id.tv_des);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }

    public void showPopupDialogChecking(final int pos) {
        final Dialog dialog_first = new Dialog(context);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_delete_service);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView iv_yes = (ImageView) dialog_first.findViewById(R.id.iv_yes);
        ImageView iv_no = (ImageView) dialog_first.findViewById(R.id.iv_no);

        iv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();
            }
        });

        iv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alImages.remove(pos);
                notifyDataSetChanged();
                dialog_first.dismiss();
            }
        });
        dialog_first.show();
    }


}
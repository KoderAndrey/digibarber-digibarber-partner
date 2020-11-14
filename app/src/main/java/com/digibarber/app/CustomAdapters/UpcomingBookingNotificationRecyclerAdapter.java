package com.digibarber.app.CustomAdapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.digibarber.app.Beans.UpcomingBookingNotification;
import com.digibarber.app.Interfaces.InnerNotificationClickCallback;
import com.digibarber.app.R;

import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 3/17/2017.
 */

public class UpcomingBookingNotificationRecyclerAdapter extends RecyclerView.Adapter<UpcomingBookingNotificationRecyclerAdapter.ViewHolder> {

    //private final List<DummyItem> mValues;

    public ArrayList<UpcomingBookingNotification> Service_list;
    Context context;
    InnerNotificationClickCallback obj;

    public UpcomingBookingNotificationRecyclerAdapter(ArrayList<UpcomingBookingNotification> Service_list, Context con, InnerNotificationClickCallback obj) {
        context = con;
        this.Service_list = Service_list;
        this.obj = obj;
    }

    public void setItems(ArrayList<UpcomingBookingNotification> Service_list) {
        this.Service_list = Service_list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_upcoming_booking_notifiaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //System.out.println(" ** Holder Position ** "+holder.getAdapterPosition());
        holder.tv_notification_text.setText(Service_list.get(position).msg);

        holder.tv_time.setText(Service_list.get(position).dates);


        if (Service_list.get(position).is_read.equalsIgnoreCase("0")) {
            holder.iv_messages.setVisibility(View.VISIBLE);
        } else {
            holder.iv_messages.setVisibility(View.GONE);
        }

        if (Service_list.get(position).type.equalsIgnoreCase("confirm_reschedule")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.resh_confirm);
        } else if (Service_list.get(position).type.equalsIgnoreCase("cancel_reschedule")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.reshed_decline);
        } else if (Service_list.get(position).type.equalsIgnoreCase("addIn_whitelist")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.go_live);
        } else if (Service_list.get(position).type.equalsIgnoreCase("activate_soon")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.contact_you);
        } else if (Service_list.get(position).type.equalsIgnoreCase("questions")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.questions);
        } else if (Service_list.get(position).type.equalsIgnoreCase("auto_cancel")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.time_out);
        } else if (Service_list.get(position).type.equalsIgnoreCase("new_barber_uncon_booking")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.unconfirmed_notification);
        } else if (Service_list.get(position).type.equalsIgnoreCase("reschedule_customer_confirm")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.drawable.customer_res);
        } else if (Service_list.get(position).type.equalsIgnoreCase("new_user")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.welcome_barber);
        } else if (Service_list.get(position).type.equalsIgnoreCase("first_review_barber")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.star_new_booking_notification);
        } else if (Service_list.get(position).type.equalsIgnoreCase("view_profile")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.profile_notification);
        } else if (Service_list.get(position).type.equalsIgnoreCase("customer_cancel")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.cancelled);
        } else if (Service_list.get(position).type.equalsIgnoreCase("auto_confirm_barber")) {
            holder.iv_notification_icon.setVisibility(View.GONE);
            holder.iv_imagebackshadow.setImageResource(R.mipmap.group_2);
        } else if (Service_list.get(position).type.equalsIgnoreCase("direct_message")) {
            //    holder.iv_notification_icon.setImageResource(R.mipmap.group_2);
            UpcomingBookingNotification upcomingBookingNotification = Service_list.get(position);
//            Glide.with(context).load(upcomingBookingNotification.image).into(holder.iv_notification_icon);
            Picasso.get().load(upcomingBookingNotification.image).fit().placeholder(R.mipmap.welcome_barber).error(R.mipmap.welcome_barber).into(holder.iv_notification_icon);
            holder.iv_imagebackshadow.setBackgroundResource(R.drawable.notification_picture_white);
        }
        holder.rl_click_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obj.notificationClickListerner(position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
        final View mView;
        public final TextView tv_notification_text;
        public final TextView tv_time;
        public final ImageView iv_notification_icon, iv_imagebackshadow;
        public final RelativeLayout rl_click_notification;
        public final ImageView iv_messages;

        ViewHolder(View view) {
            super(view);
            mView = view;
            iv_messages = (ImageView) view.findViewById(R.id.iv_messages);
            tv_notification_text = (TextView) view.findViewById(R.id.tv_notification_text);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            iv_notification_icon = (ImageView) view.findViewById(R.id.iv_notification_icon);
            rl_click_notification = (RelativeLayout) view.findViewById(R.id.rl_click_notification);
            iv_imagebackshadow = view.findViewById(R.id.iv_imagebackshadow);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }


}

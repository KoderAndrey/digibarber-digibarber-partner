package com.digibarber.app.CustomAdapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.digibarber.app.Beans.BookingList;
import com.digibarber.app.CustomClasses.BothSideCoordinatorLayout;
import com.digibarber.app.Interfaces.BookingListCallbacks;
import com.digibarber.app.R;
import com.digibarber.app.activities.HomeActivity;
import com.xenione.libs.swipemaker.SwipeLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by DIGIBARBER LTD on 3/17/2017.
 */

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {
    ArrayList<BookingList> Service_list = new ArrayList<>();
    Context context;
    BookingListCallbacks objBookingListCallbacks;
    String curentdate = "";
    private DateFormat formater;
    private int openPosition = -1;
    private int lastPosition = -1;
    boolean istodayshowed = true;

    public HomeRecyclerAdapter(ArrayList<BookingList> Service_list, Context con, BookingListCallbacks objBookingListCallbacks) {
        context = con;
        this.Service_list = Service_list;
        this.objBookingListCallbacks = objBookingListCallbacks;
        formater = new SimpleDateFormat("EEEE, dd MMM yyyy");
        Calendar calCurent = Calendar.getInstance();
        curentdate = formater.format(calCurent.getTime());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_swipe_layout, parent, false);
        return new ViewHolder(view);
    }

    /* @Override
     public void onViewRecycled(ViewHolder holder) {
         super.onViewRecycled(holder);
         holder.coordinatorLayout.sync();
     }*/
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        setAnimation(holder.coordinatorLayout, position);

        holder.tag = position;
        holder.tv_service_name.setText(Service_list.get(position).services);
        String[] separated = Service_list.get(position).user_name.split(" ");
        holder.tv_client_name.setText(separated[0]);
        holder.tv_post_code.setText(Service_list.get(position).customerPostCode);
        holder.tv_time.setText(Service_list.get(position).booking_time);
        try {
            if (curentdate != null && !curentdate.equalsIgnoreCase("")) {
                if (curentdate.equalsIgnoreCase(Service_list.get(position).date)) {
                    if (istodayshowed) {
                        istodayshowed = false;
                        holder.tv_today.setVisibility(View.VISIBLE);
                    }
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
            holder.ll_dates.setVisibility(View.VISIBLE);
            //   holder.ll_dates.setVisibility(View.VISIBLE);
            holder.tv_date.setText(Service_list.get(position).date);
            // holder.tv_today.setVisibility(View.VISIBLE);
        } else {
            String currentdate = Service_list.get(position).date;
            String previous = Service_list.get(position - 1).date;
            if (currentdate.equalsIgnoreCase(previous)) {
                //       holder.ll_dates.setVisibility(View.INVISIBLE);

                if (holder.tv_today.getVisibility() == View.VISIBLE) {
                    holder.tv_date.setVisibility(View.INVISIBLE);
                    holder.ll_dates.setVisibility(View.INVISIBLE);
                } else {
                    holder.tv_date.setVisibility(View.GONE);
                    holder.ll_dates.setVisibility(View.GONE);


                }
                //    holder.tv_today.setVisibility(View.GONE);
            } else {
                holder.tv_date.setVisibility(View.VISIBLE);
                holder.ll_dates.setVisibility(View.INVISIBLE);

                holder.tv_date.setText(Service_list.get(position).date);
            }
        }
        holder.tv_start_close_booking.setVisibility(View.INVISIBLE);

     /*   if (Service_list.get(position).is_confirmed.equalsIgnoreCase("0")) {
            //Unconfrimed
            holder.iv_roller.setBackgroundResource(R.mipmap.slidergrey);
            holder.ll_background_rec.setBackgroundResource(R.mipmap.upcoming_rec_grey);
            holder.tv_resc.setVisibility(View.GONE);
            holder.tv_cancel.setVisibility(View.GONE);
            holder.tv_accept.setVisibility(View.VISIBLE);
            holder.tv_cancel.setText("Decline");
            holder.tv_start_close_booking.setVisibility(View.INVISIBLE);
            holder.iv_reschdule_icon.setVisibility(View.GONE);

        } else*/
        if (Service_list.get(position).is_confirmed.equalsIgnoreCase("1")) {
            //Confimred
            holder.iv_roller.setBackgroundResource(R.mipmap.slidergreen);
            holder.ll_background_rec.setBackgroundResource(R.mipmap.upcoming_rec_green);
            holder.tv_resc.setVisibility(View.GONE);
            holder.tv_cancel.setText("Cancel");
            holder.tv_start_close_booking.setImageResource(R.mipmap.start_bookibg);
            if (Service_list.get(position).date.equalsIgnoreCase(curentdate)) {
                if (Service_list.get(position).isfirstConfBooking.equalsIgnoreCase("1")) {
                    holder.tv_start_close_booking.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_start_close_booking.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.tv_start_close_booking.setVisibility(View.INVISIBLE);
            }
            holder.iv_reschdule_icon.setVisibility(View.GONE);

        } /*else if (Service_list.get(position).is_confirmed.equalsIgnoreCase("2")) {
            //Deleted
            holder.tv_resc.setVisibility(View.VISIBLE);
            holder.tv_cancel.setVisibility(View.VISIBLE);
            holder.tv_accept.setVisibility(View.VISIBLE);
            holder.tv_cancel.setText("Decline");
            holder.iv_reschdule_icon.setVisibility(View.GONE);
        } else if (Service_list.get(position).is_confirmed.equalsIgnoreCase("3")) {
            //Completed
            holder.tv_resc.setVisibility(View.VISIBLE);
            holder.tv_cancel.setText("Decline");
            holder.tv_cancel.setVisibility(View.VISIBLE);
            holder.tv_accept.setVisibility(View.VISIBLE);
            holder.iv_reschdule_icon.setVisibility(View.GONE);

        }*/ else if (Service_list.get(position).is_confirmed.equalsIgnoreCase("4")) {
            //Active
            holder.tv_resc.setVisibility(View.GONE);
            holder.tv_cancel.setVisibility(View.GONE);
            holder.tv_accept.setVisibility(View.GONE);
            holder.tv_start_close_booking.setImageResource(R.mipmap.close_booking);
            holder.tv_start_close_booking.setVisibility(View.VISIBLE);

            holder.iv_roller.setBackgroundResource(R.mipmap.sliderblue);
            holder.iv_reschdule_icon.setVisibility(View.GONE);
            holder.ll_background_rec.setBackgroundResource(R.mipmap.upcoming_rec_blue);

        } else {
            holder.iv_roller.setBackgroundResource(R.mipmap.slidergrey);
            holder.ll_background_rec.setBackgroundResource(R.mipmap.upcoming_rec_grey);
            holder.tv_resc.setVisibility(View.VISIBLE);
            holder.tv_cancel.setVisibility(View.VISIBLE);
            holder.tv_accept.setVisibility(View.VISIBLE);
            holder.tv_cancel.setText("Decline");
            holder.tv_start_close_booking.setVisibility(View.INVISIBLE);
            holder.iv_reschdule_icon.setVisibility(View.GONE);
        }
        if (Service_list.get(position).reschedule.equalsIgnoreCase("1")) {
            holder.tv_cancel.setText("Decline");
            holder.tv_start_close_booking.setVisibility(View.INVISIBLE);
            holder.tv_resc.setVisibility(View.GONE);
            holder.iv_reschdule_icon.setVisibility(View.VISIBLE);
            holder.tv_cancel.setVisibility(View.VISIBLE);
            holder.tv_accept.setVisibility(View.VISIBLE);
            holder.tv_time.setText(Service_list.get(position).rescdule_time);
            holder.time_icon.setImageResource(R.drawable.reschdule_small);
            holder.time_icon.setAlpha(0.75f);
        } else {
            holder.time_icon.setImageResource(R.mipmap.home_time_icon);
            holder.time_icon.setAlpha(1f);
        }


        holder.tv_resc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objBookingListCallbacks.rescheduleBookingListener(position);

            }
        });

        holder.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                objBookingListCallbacks.cancelDeclineBookingListener(position, "");

            }
        });

        holder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objBookingListCallbacks.confirmBookingListener(position);

            }
        });


        holder.tv_start_close_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objBookingListCallbacks.startCloseBookingListener(position);
            }
        });


        holder.sp.setOnTranslateChangeListener(new SwipeLayout.OnTranslateChangeListener() {
            @Override
            public void onTranslateChange(float globalPercent, int index, float relativePercent) {



                if ((index == 0 && relativePercent == 0) || (index == 1 && relativePercent == 1)) {
                    //holder.sp.translateTo(0);
                    HomeActivity homeActivity = (HomeActivity) context;
                    openPosition = position;
                    holder.isOpen = true;
                    homeActivity.translationDone(position);


                }

            }
        });

        holder.tv_post_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objBookingListCallbacks.onPostCodeClicked(position);

            }
        });

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
    public int getItemCount() {
        return Service_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_service_name;
        public TextView tv_client_name;
        public TextView tv_post_code;
        public TextView tv_time;
        public ImageView iv_roller;
        public LinearLayout ll_background_rec;
        public LinearLayout ll_dates;

        public TextView tv_resc;
        public TextView tv_cancel;
        public TextView tv_accept;
        public BothSideCoordinatorLayout coordinatorLayout;
        public ImageView tv_start_close_booking;
        public ImageView iv_reschdule_icon;
        public ImageView time_icon;
        SwipeLayout sp;
        public TextView tv_date;
        public TextView tv_today;

        public int tag = 0;
        public boolean isOpen = false;

        ViewHolder(View view) {
            super(view);
            coordinatorLayout = (BothSideCoordinatorLayout) view;
            tv_service_name = (TextView) coordinatorLayout.findViewById(R.id.tv_service_name);
            tv_client_name = (TextView) coordinatorLayout.findViewById(R.id.tv_client_name);
            tv_post_code = (TextView) coordinatorLayout.findViewById(R.id.tv_postcode);
            tv_time = (TextView) coordinatorLayout.findViewById(R.id.tv_time);
            iv_roller = (ImageView) coordinatorLayout.findViewById(R.id.iv_roller);
            ll_background_rec = (LinearLayout) coordinatorLayout.findViewById(R.id.ll_background_rec);
            tv_today = (TextView) coordinatorLayout.findViewById(R.id.tv_today);
            tv_resc = (TextView) coordinatorLayout.findViewById(R.id.tv_resc);
            tv_cancel = (TextView) coordinatorLayout.findViewById(R.id.tv_cancel);
            tv_accept = (TextView) coordinatorLayout.findViewById(R.id.tv_accept);
            tv_start_close_booking = (ImageView) coordinatorLayout.findViewById(R.id.tv_start_close_booking);
            ll_dates = (LinearLayout) coordinatorLayout.findViewById(R.id.ll_dates);


            time_icon = (ImageView) coordinatorLayout.findViewById(R.id.time_icon);
            iv_reschdule_icon = (ImageView) coordinatorLayout.findViewById(R.id.iv_reschdule_icon);
            sp = (SwipeLayout) coordinatorLayout.findViewById(R.id.foregroundView);
            tv_date = (TextView) coordinatorLayout.findViewById(R.id.tv_date);

        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (holder.sp != null && openPosition != -1 && holder.isOpen) {
            if (openPosition != holder.tag) {
                holder.sp.translateTo(0);
                holder.isOpen = false;
            }
        }
    }
}

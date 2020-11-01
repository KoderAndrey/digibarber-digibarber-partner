package com.digibarber.app.CustomAdapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.digibarber.app.Beans.available_time;
import com.digibarber.app.R;

import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 4/10/17.
 */

public class LiveAvailavilityRecyclerAdapter extends RecyclerView.Adapter<LiveAvailavilityRecyclerAdapter.MyHolderV> {

    private Context context;
    private ArrayList<available_time> availbleTime;

    Typeface typefaceUITextBold;

    Typeface typefaceAvenirLight;

    public LiveAvailavilityRecyclerAdapter(Context context, ArrayList<available_time> availbleTime) {
        this.context = context;
        this.availbleTime = availbleTime;
        typefaceUITextBold = Typeface.createFromAsset(context.getAssets(), "fonts/SF-UI-Text-Bold.otf");
        typefaceAvenirLight = Typeface.createFromAsset(context.getAssets(), "fonts/avenir_light.ttf");
    }

    @Override
    public MyHolderV onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_avility_items, parent, false);
        MyHolderV mhv = new MyHolderV(view);

        return mhv;
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
    public void onBindViewHolder(final MyHolderV holder, final int position) {


         /*   if (position % 5 == 0) {
                // String Time = availbleTime.get(position + 1).time;
                String Time = availbleTime.get(position).time;
                if (Time != null && !Time.equals("")) {
                    String strData[] = Time.split(":");
                    holder.textView.setText(strData[0] + ":00");
                } else {
                    //holder.textView.setText(availbleTime.get(position + 1).time);
                    holder.textView.setText(availbleTime.get(position).time);
                }
                holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                holder.textView.setBackgroundResource(0);
                holder.textView.setTypeface(typefaceAvenirLight);
                holder.textView.setTextColor(Color.parseColor("#FF31353A"));
                holder.textView.setAlpha(1);
            } else {*/
        holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        holder.textView.setVisibility(View.VISIBLE);
        holder.textView.setTypeface(typefaceUITextBold);
        if (availbleTime.get(position).status.equalsIgnoreCase("NotAvailble")) {
            holder.textView.setBackgroundResource(R.mipmap.rectangle_83);
            holder.textView.setBackgroundResource(R.mipmap.rectangle_83);

            holder.textView.setTextColor(Color.parseColor("#FF31363B"));
            holder.textView.setAlpha((float) 0.65);
            holder.textView.setText(availbleTime.get(position).time);
        }else if (availbleTime.get(position).status.equalsIgnoreCase("NotAvailble_CannotBooked")) {
            holder.textView.setBackgroundResource(R.mipmap.rectangle_83);

            holder.textView.setTextColor(Color.parseColor("#FF31363B"));
            holder.textView.setAlpha((float) 0.65);
            holder.textView.setText(availbleTime.get(position).time);
        } else if (availbleTime.get(position).status.equalsIgnoreCase("TimeNotAvailble")) {
         //   holder.textView.setBackgroundResource(0);
            holder.textView.setTextColor(Color.parseColor("#FFFFFF"));
            holder.textView.setAlpha((float) 0.25);
            holder.textView.setShadowLayer(2f, -5, 5, context.getResources().getColor(R.color.shadow_color));

            holder.textView.setText(availbleTime.get(position).time);
            holder.textView.setVisibility(View.VISIBLE);
            holder.iv_slot_disabled.setVisibility(View.VISIBLE);
        }else if (availbleTime.get(position).status.equalsIgnoreCase("NotAvailble_InterValTime")) {
         //   holder.textView.setBackgroundResource(0);
            holder.textView.setTextColor(Color.parseColor("#FFFFFF"));
            holder.textView.setAlpha((float) 0.25);
            holder.textView.setShadowLayer(2f, -5, 5, context.getResources().getColor(R.color.shadow_color));

            holder.textView.setText(availbleTime.get(position).time);
            holder.textView.setVisibility(View.VISIBLE);
            holder.iv_slot_disabled.setVisibility(View.VISIBLE);
        } else {
            holder.textView.setBackgroundResource(R.mipmap.rectangle_83copy);
            holder.textView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.textView.setAlpha(1);
            holder.textView.setText(availbleTime.get(position).time);


        }
           /* }*/


        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (availbleTime.get(position).status.equalsIgnoreCase("NotAvailble_CannotBooked")) {
                    ShowPopupDialougUnavailble();

                } else if (availbleTime.get(position).status.equalsIgnoreCase("NotAvailble_InterValTime")) {
                    ShowPopupDialougUnavailble();

                } else if (availbleTime.get(position).status.equalsIgnoreCase("NotAvailble")) {
                    availbleTime.set(position, new available_time(availbleTime.get(position).time, "Availble"));
                    holder.textView.setBackgroundResource(R.mipmap.rectangle_83copy);
                    holder.textView.setTextColor(context.getResources().getColor(R.color.colorWhite));
                    holder.textView.setAlpha(1);
                } else if (availbleTime.get(position).status.equalsIgnoreCase("TimeNotAvailble")) {
                    ShowPopupDialougUnavailble();
                } else {
                    availbleTime.set(position, new available_time(availbleTime.get(position).time, "NotAvailble"));
                    holder.textView.setBackgroundResource(R.mipmap.rectangle_83);
                    holder.textView.setTextColor(Color.parseColor("#FF31363B"));
                    holder.textView.setAlpha((float) 0.75);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return availbleTime.size();
    }


    public void ShowPopupDialougUnavailble() {
        final Dialog dialog_first = new Dialog(context);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_time_not_avaiable);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
        TextView alertTitle = (TextView) dialog_first.findViewById(R.id.alertTitle);
        alertTitle.setText("You're Busy");
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();
            }
        });
        dialog_first.show();
    }

    public class MyHolderV extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView iv_slot_disabled;

        public MyHolderV(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.time_view);
            iv_slot_disabled = (ImageView) itemView.findViewById(R.id.iv_slot_disabled);
        }
    }

}
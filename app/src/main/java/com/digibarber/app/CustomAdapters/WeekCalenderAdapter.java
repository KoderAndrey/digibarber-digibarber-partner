package com.digibarber.app.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digibarber.app.Beans.WeekCalender;
import com.digibarber.app.R;

import java.util.ArrayList;


/**
 * Created by DIGIBARBER LTD on 11/10/16.
 */
public class WeekCalenderAdapter extends BaseAdapter {

    ArrayList<WeekCalender> ales_oppp;
    Activity activity;
    private LayoutInflater inflater = null;
    String date;
    int SelectedPos = -1;

    public WeekCalenderAdapter(Activity activity, ArrayList<WeekCalender> ales_oppp, String date) {
        this.activity = activity;
        this.ales_oppp = ales_oppp;
        this.date = date;
    }


    public void Selected(int pos) {
        this.SelectedPos = pos;
    }

    @Override
    public int getCount() {
        return ales_oppp.size();
    }

    @Override
    public Object getItem(int position) {
        return ales_oppp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            inflater = (LayoutInflater) this.activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_week_calender, null);
            holder = new ViewHolder();
            holder.tv_day_name = (TextView) convertView.findViewById(R.id.tv_day_name);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (SelectedPos == position) {
            holder.tv_date.setBackgroundResource(R.drawable.selected);
            holder.tv_date.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.tv_date.setBackgroundResource(0);
            holder.tv_date.setTextColor(Color.parseColor("#000000"));
        }
        holder.tv_date.setText(ales_oppp.get(position).date);
        holder.tv_day_name.setText(ales_oppp.get(position).dayName.substring(0, 3));
        return convertView;
    }

    public class ViewHolder {
        TextView tv_day_name;
        TextView tv_date;
    }


}

package com.digibarber.app.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digibarber.app.R;

import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 23/9/17.
 */

public class ServiceTimeGalleryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;
    private LayoutInflater inflater;
    int selected_Positiuon = 0;
    public ServiceTimeGalleryAdapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void selectedPosition(int position) {
        selected_Positiuon = position;
    }
    public int getSelectedPosition( ) {
        return selected_Positiuon ;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        final ViewHolder holder;

        if (convertView == null) {
            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.adapter_service_time_gallery, viewGroup, false);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv_gallery);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (i == selected_Positiuon) {
            holder.tv.setBackgroundResource(R.mipmap.service_price_selected);
            holder.tv.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.tv.setAlpha((float) 1);
        } else {
            holder.tv.setBackgroundResource(R.mipmap.service_price_unselected);
            holder.tv.setTextColor(context.getResources().getColor(R.color.service_time_color));
            holder.tv.setAlpha((float) 0.8);
        }

        holder.tv.setText(list.get(i));

        return convertView;
    }

    public class ViewHolder {
        TextView tv;

    }
}
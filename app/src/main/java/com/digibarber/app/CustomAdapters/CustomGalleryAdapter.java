package com.digibarber.app.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.digibarber.app.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DIGIBARBER LTD on 10/6/17.
 */
public class CustomGalleryAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> gallery_image_listing;
    Context context;

    public CustomGalleryAdapter(Context con, ArrayList<HashMap<String, String>> listing) {
        context = con;
        gallery_image_listing = listing;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        HashMap<String, String> hm = gallery_image_listing.get(position);
        holder = new ViewHolder();
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.gallery_image_layout, parent, false);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        if (hm.get("gallery_image").equalsIgnoreCase("") || hm.get("gallery_image").equalsIgnoreCase("null")) {
            if ((position+1)%3==0)

                Picasso.get()
                        .load(R.mipmap.placeholder_image)
                        .resize(512, 512)
                        .error(R.mipmap.placeholder_image)
                        .placeholder(R.mipmap.placeholder_image)
                        .noFade()
                        .into(holder.image);
        } else {


            Picasso.get()
                    .load(hm.get("gallery_image"))
                    .resize(512, 512)
                    .error(R.mipmap.placeholder_image)
                    .noFade()
                    .placeholder(R.mipmap.placeholder_image)

                    .into(holder.image);
        }
        return row;
    }
    @Override
    public int getCount() {
        return gallery_image_listing.size();
    }
    @Override
    public Object getItem(int position) {
        return gallery_image_listing.get(position);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    static class ViewHolder {
        ImageView image;
    }
}

package com.digibarber.app.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.digibarber.app.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DIGIBARBER LTD on 9/10/17.
 */

public class CustomReviewAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> review_listing = new ArrayList<HashMap<String, String>>();
    Context context;
    String tag;

    public CustomReviewAdapter(Context con, ArrayList<HashMap<String, String>> listing) {
        context = con;
        review_listing = listing;
        // tag = tag_count;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        HashMap<String, String> hm = review_listing.get(position);


        //System.out.println("** Review Adapter ** "+ review_listing.size());

        holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();


            row = inflater.inflate(R.layout.adapter_view_profile_review, parent, false);

            holder.client_image = (CircleImageView) row.findViewById(R.id.client_image);
            holder.client_name = (TextView) row.findViewById(R.id.client_name);
            holder.reviews_text = (TextView) row.findViewById(R.id.reviews_text);
            holder.text_total_ratings = (TextView) row.findViewById(R.id.text_total_ratings);
            holder.ratingBar = (RatingBar) row.findViewById(R.id.rating_stars);

            holder.date = (TextView) row.findViewById(R.id.date);


            if(hm.get("profile_image").equalsIgnoreCase("null") || hm.get("profile_image").equalsIgnoreCase(""))
            {

            }
            else
            {
                Picasso.with(context).load(hm.get("profile_image")).placeholder(R.mipmap.placeholder_image)
                        .fit().into(holder.client_image);
            }

            holder.client_name.setText(hm.get("name"));
            holder.reviews_text.setText(hm.get("review"));
            holder.text_total_ratings.setText(hm.get("rating"));
            holder.date.setText(hm.get("date_of_review"));
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }
        if (!hm.get("rating").equalsIgnoreCase("")) {
            double ratings = Double.parseDouble(hm.get("rating"));
            holder.ratingBar.setRating((float) ratings);
            LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.parseColor("#FF9500"), PorterDuff.Mode.SRC_ATOP);
        }
        return row;
    }


    @Override
    public int getCount() {
        return review_listing.size();
    }

    @Override
    public Object getItem(int position) {
        return review_listing.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {

        TextView client_name, date, text_total_ratings, reviews_text;
        CircleImageView client_image;
        RatingBar ratingBar;
    }
}
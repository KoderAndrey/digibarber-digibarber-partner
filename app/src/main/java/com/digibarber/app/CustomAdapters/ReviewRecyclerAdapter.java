package com.digibarber.app.CustomAdapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.digibarber.app.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DIGIBARBER LTD on 3/17/2017.
 */

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> review_listing = new ArrayList<HashMap<String, String>>();
    Context context;

    SimpleDateFormat formateFrom;
    SimpleDateFormat formateTarget;

    public ReviewRecyclerAdapter(Context con, ArrayList<HashMap<String, String>> listing) {
        context = con;
        review_listing = listing;
//        formateFrom = new SimpleDateFormat("dd/mm/yyyy");
//        formateTarget = new SimpleDateFormat("dd MMM yyyy");
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_listing_layout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> hm = review_listing.get(position);
        if (hm.get("Activity")!=null) {
            if (hm.get("Activity").equalsIgnoreCase("view"))
                holder.ll_main.setBackgroundColor(context.getResources().getColor(R.color.review_item_color));
            else
                holder.ll_main.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }else
            holder.ll_main.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));

        holder.client_name.setText(hm.get("name"));
        holder.reviews_text.setText(hm.get("review"));
        holder.date.setText(parseDate(hm.get("date_of_review")));

          if (hm.get("profile_image") != null && !hm.get("profile_image").equalsIgnoreCase(""))
            Picasso.with(context).load(hm.get("profile_image")).placeholder(R.mipmap.placeholder_image)
                .fit().into(holder.client_image);
        holder.tv_service.setText(hm.get("service"));
        if (!hm.get("rating").equalsIgnoreCase("")) {
            DecimalFormat twoDForm = new DecimalFormat("#0.0");
            double ratings = Double.parseDouble(hm.get("rating"));

            ratings = Math.floor(ratings * 100) / 100;
            holder.text_total_ratings.setText(ratings + "");
            holder.ratingBar.setRating((float) ratings);
            LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.parseColor("#FF9500"), PorterDuff.Mode.SRC_ATOP);

        }
    }
    String parseDate(String input) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM  yyyy");
            System.out.println(outputFormat.format(inputFormat.parse(input)));
            System.out.println("Date_format ->" + outputFormat.format(inputFormat.parse(input)));
            return outputFormat.format(inputFormat.parse(input));
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return "";
    }
    @Override
    public int getItemCount() {
        return review_listing.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        TextView client_name, date, text_total_ratings, reviews_text;
        CircleImageView client_image;
        RatingBar ratingBar;
        TextView tv_service;
LinearLayout ll_main;
        ViewHolder(View view) {
            super(view);
            mView = view;
            client_image = (CircleImageView) mView.findViewById(R.id.client_image);
            client_name = (TextView) mView.findViewById(R.id.client_name);
            reviews_text = (TextView) mView.findViewById(R.id.reviews_text);
            text_total_ratings = (TextView) mView.findViewById(R.id.text_total_ratings);
            date = (TextView) mView.findViewById(R.id.date);
            ratingBar = (RatingBar) mView.findViewById(R.id.rating_stars);
            tv_service = (TextView) mView.findViewById(R.id.tv_service);
            ll_main = (LinearLayout) mView.findViewById(R.id.ll_main);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}

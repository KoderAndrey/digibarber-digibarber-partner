package com.digibarber.app.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.digibarber.app.Beans.CustomGalleryImages;
import com.digibarber.app.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 3/17/2017.
 */

public class CustomGalleryImagesAdapter extends RecyclerView.Adapter<CustomGalleryImagesAdapter.ViewHolder> {


    public interface CustomGalleryListner {
        void onItemClick(String path);
    }

    CustomGalleryListner customGalleryListner;

    //private final List<DummyItem> mValues;
    ArrayList<CustomGalleryImages> images;
    Context context;
    int pos = -7;
    DisplayMetrics displaymetrics;
    int devicewidth;
    int deviceheight;
    ArrayList<Boolean> alSelectImagesTick;

    public CustomGalleryImagesAdapter(ArrayList<CustomGalleryImages> image, ArrayList<Boolean> alSelectImagesTick, Context con, CustomGalleryListner customGalleryListner) {
        images = image;
        context = con;
        this.alSelectImagesTick = alSelectImagesTick;
        displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        devicewidth = displaymetrics.widthPixels / 3;
        deviceheight = displaymetrics.heightPixels / 4;
        this.customGalleryListner = customGalleryListner;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void selcetedImage(int pos) {
        this.pos = pos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_gallery_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customGalleryListner.onItemClick(images.get(position).images);
            }
        });
//        if (alSelectImagesTick.get(position)) {
//            holder.iv_tick.setVisibility(View.VISIBLE);
//        } else {
//            holder.iv_tick.setVisibility(View.GONE);
//        }
        try {
            File imgFile = new File(images.get(position).images);
            final Uri imageUri = Uri.fromFile(imgFile);
            Log.i("test_test", "file " + imgFile + ", uri " + imageUri + ", path " + imgFile.getPath());
            Glide
                    .with(context)
                    .load(imgFile.getPath())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.i("test_test", "e " + e );
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.iv_image);
            //Picasso.with(context).load(imageUri).fit().centerCrop().into(holder.iv_image);
        } catch (NullPointerException | OutOfMemoryError e) {
            Log.i("test_test", "error " + e);
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView iv_tick;
        public final ImageView iv_image;

        ViewHolder(View view) {
            super(view);
            mView = view;
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            iv_tick = (ImageView) view.findViewById(R.id.iv_tick);
            iv_image.getLayoutParams().width = devicewidth;
            iv_image.getLayoutParams().height = deviceheight;
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}

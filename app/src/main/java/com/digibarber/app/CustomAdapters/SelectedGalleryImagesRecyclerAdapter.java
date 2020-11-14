package com.digibarber.app.CustomAdapters;

import android.content.Context;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.digibarber.app.Beans.SelectedImages;
import com.digibarber.app.Interfaces.RemoveGalleryImages;
import com.digibarber.app.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 3/17/2017.
 */

public class SelectedGalleryImagesRecyclerAdapter extends RecyclerView.Adapter<SelectedGalleryImagesRecyclerAdapter.ViewHolder> {

    ArrayList<SelectedImages> alImages;
    Context context;
    RemoveGalleryImages removeGalleryImages;

    public SelectedGalleryImagesRecyclerAdapter(ArrayList<SelectedImages> alImages, Context con, RemoveGalleryImages removeGalleryImages) {
        context = con;
        this.alImages = alImages;
        this.removeGalleryImages = removeGalleryImages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_selected_gallery_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        File imgFile = new File(alImages.get(position).image);
        Uri imageUri = Uri.fromFile(imgFile);
        Picasso.get().load(imageUri).memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(holder.iv_image);
        holder.iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeGalleryImages.RemoveGalleryImages(position);
            }
        });
        holder.iv_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeGalleryImages.ReplaceGalleryImages(position);
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
        public final ImageView iv_image;
        public final ImageView iv_cross;
        public final ImageView iv_replace;

        ViewHolder(View view) {
            super(view);
            mView = view;
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            iv_cross = (ImageView) view.findViewById(R.id.iv_cross);
            iv_replace = (ImageView) view.findViewById(R.id.iv_replace);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }


}

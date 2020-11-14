package com.digibarber.app.CustomMediaPicker

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.digibarber.app.R

class PhotosAdapter(var context: CMediaPicker) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = context.layoutInflater.inflate(R.layout.photoview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val album = context.allAlbums.get(context.currentIndex)
        val uri = album.images.get(position)
        if (position == 0 && album.selected.isEmpty()) {
            context.runOnUiThread {
                context.librayName.text = album.name.capitalize()
                Glide.with(context).load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(context.topImage)
            }
        }
        if (album.selected.contains(uri)) {
            holder.checkV.visibility = View.VISIBLE
        } else {
            holder.checkV.visibility = View.INVISIBLE
        }
        Glide.with(context).load(uri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.imageV)
        holder.itemView.setOnClickListener {
            kotlin.run {
                var selectedList = album.selected
                if (album.selected.contains(uri)) {
                    selectedList.remove(uri)
                    context.runOnUiThread {
                        holder.checkV.visibility = View.INVISIBLE
                    }
                    context.selectedImages -= 1
                } else if (context.selectedImages < context.maxImages) {
                    selectedList.add(uri)
                    context.selectedImages += 1
                    context.runOnUiThread {
                        holder.checkV.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(context, "Maximum Limit ${context.maxImages}", Toast.LENGTH_SHORT).show()
                }
                context.selectedPhotos = selectedList
                context.allAlbums.get(context.currentIndex).selected = selectedList
                if (selectedList.size > 0) {
                    Glide.with(context).load(selectedList.last())
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(context.topImage)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return context.allAlbums.get(context.currentIndex).images.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageV: ImageView
        var checkV: ImageView

        init {
            imageV = itemView.findViewById(R.id.imageV)
            checkV = itemView.findViewById(R.id.checkV)
        }
    }
}
package com.digibarber.app.CustomMediaPicker

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.digibarber.app.R
import com.squareup.picasso.Picasso

class AlbumAdapter (var context: CMediaPicker) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = context.layoutInflater.inflate(R.layout.cpicker_album_view, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val album = context.allAlbums.get(position)
        val uri = album.images.get(0)
        holder.countTxt.text  = album.images.size.toString()
        holder.nameTxt.text   = album.name.capitalize()
        context.runOnUiThread {
            Glide.with(context).load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.imageV)
        }
        holder.itemView.setOnClickListener {
            context.runOnUiThread {
                context.currentIndex = position
                context.librayName.text = album.name.capitalize()
                context.albumView.visibility = View.INVISIBLE
            }
            context.photosAdapter.notifyDataSetChanged()
        }
    }
    override fun getItemCount(): Int {
        return context.allAlbums.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageV    : ImageView
        var nameTxt   : TextView
        var countTxt  : TextView

        init {
            imageV   = itemView.findViewById(R.id.imageV)
            nameTxt  = itemView.findViewById(R.id.albumName)
            countTxt = itemView.findViewById(R.id.albumcount)

        }
    }
}
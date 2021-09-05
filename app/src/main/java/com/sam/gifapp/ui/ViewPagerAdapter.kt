package com.sam.gifapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sam.gifapp.R
import com.sam.gifapp.model.Gif

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.GifViewHolder>() {
    private var gifs: List<Gif> = listOf()
    private var currPosition = -1

    fun isLastPage(): Boolean = currPosition == gifs.size - 1

    fun isFirstPage(): Boolean = currPosition == -1 || currPosition == 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        return GifViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_gif, parent, false))
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.bind(gifs[position])
        currPosition = position
    }

    override fun getItemCount(): Int {
        return gifs.size
    }

    fun setGifs(gifs: List<Gif>) {
        this.gifs = gifs
        notifyDataSetChanged()
    }

    inner class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivGifs = itemView.findViewById<ImageView>(R.id.ivGif)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)

        fun bind(gif: Gif) {
            Glide.with(itemView.context)
                .asGif()
                .load(gif.url)
                .into(ivGifs)
            tvDescription.text = gif.description
        }
    }

}
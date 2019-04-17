package com.mitya.photosapp.albums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mitya.photosapp.R
import kotlinx.android.synthetic.main.item_list.view.*

class AlbumsAdapter(private val onClick: (Int) -> (Unit)) : RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {

    private val items = ArrayList<Album>()

    fun addAlbums(albums: List<Album>) {
        items.clear()
        this.notifyDataSetChanged()
        items.addAll(albums)
        this.notifyItemRangeInserted(0, items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], onClick)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(album: Album, onClick: (Int) -> (Unit)) {
            with(itemView) {
                title.text = album.title
                itemView.setOnClickListener { onClick(album.id) }
            }
        }
    }
}
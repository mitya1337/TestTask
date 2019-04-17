package com.mitya.photosapp.photos

import android.graphics.Bitmap
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mitya.photosapp.Cache
import com.mitya.photosapp.R
import com.mitya.photosapp.downloadImage
import kotlinx.android.synthetic.main.item_photo.view.*
import java.lang.ref.WeakReference

class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    private val items = ArrayList<Photo>()

    fun addPhotos(photos: List<Photo>) {
        items.clear()
        this.notifyDataSetChanged()
        items.addAll(photos)
        this.notifyItemRangeInserted(0, items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photo: Photo) {
            with(itemView) {
                title.text = photo.title
                if (Cache.getBitmapFromCache(photo.id) == null) DownloadImageTask(
                    itemView,
                    photo
                ).execute(photo.url)
                else photoView.setImageBitmap(Cache.getBitmapFromCache(photo.id))
            }
        }

        private class DownloadImageTask(itemView: View, photo: Photo) : AsyncTask<String, Int, Bitmap>() {
            private val viewReference: WeakReference<View> = WeakReference(itemView)
            private val photoReference: WeakReference<Photo> = WeakReference(photo)

            override fun onPreExecute() {
                viewReference.get()?.let {
                    it.loadingProgressBar.visibility = View.VISIBLE
                    it.photoView.visibility = View.GONE
                }
                super.onPreExecute()
            }

            override fun doInBackground(vararg params: String): Bitmap {
                return downloadImage(params[0])
            }

            override fun onPostExecute(result: Bitmap) {
                viewReference.get()?.let {
                    it.loadingProgressBar.visibility = View.GONE
                    it.photoView.visibility = View.VISIBLE
                    it.photoView.setImageBitmap(result)
                    Cache.saveBitmapToCache(photoReference.get()!!.id, result)
                }
                super.onPostExecute(result)
            }
        }
    }
}
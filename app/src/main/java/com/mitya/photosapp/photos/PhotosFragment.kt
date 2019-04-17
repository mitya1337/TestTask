package com.mitya.photosapp.photos

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitya.photosapp.ListFragment
import com.mitya.photosapp.makeHttpRequest
import com.mitya.photosapp.parsePhotoData
import kotlinx.android.synthetic.main.fragment_list.*
import java.lang.ref.WeakReference

class PhotosFragment : ListFragment() {

    private val adapter by lazy { PhotosAdapter() }
    private var albumId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        albumId = arguments?.getInt(ALBUM_ID_KEY) ?: 0
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler.adapter = adapter
        Task(this).execute("https://jsonplaceholder.typicode.com/photos")
        super.onViewCreated(view, savedInstanceState)
    }

    private class Task(context: PhotosFragment) : AsyncTask<String, Int, String>() {

        private val activityReference: WeakReference<PhotosFragment> = WeakReference(context)
        val fragment = activityReference.get()

        override fun doInBackground(vararg params: String): String {
            return makeHttpRequest(
                params[0],
                hashMapOf(Pair("albumId", fragment?.albumId.toString()))
            )
        }

        override fun onPostExecute(result: String) {
            fragment?.let { it.adapter.addPhotos(parsePhotoData(result)) }
        }
    }

    companion object {
        private const val ALBUM_ID_KEY = "albumID"
        fun newInstance(id: Int): PhotosFragment {
            val args = Bundle()
            args.putInt(ALBUM_ID_KEY, id)
            val fragment = PhotosFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
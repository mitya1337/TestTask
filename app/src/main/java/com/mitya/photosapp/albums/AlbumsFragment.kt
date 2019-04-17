package com.mitya.photosapp.albums

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitya.photosapp.*
import com.mitya.photosapp.photos.PhotosFragment
import kotlinx.android.synthetic.main.fragment_list.*
import java.lang.ref.WeakReference

class AlbumsFragment : ListFragment() {
    private val adapter by lazy { AlbumsAdapter { showPhotos(it) } }
    private var userId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        userId = arguments?.getInt(USER_ID_KEY) ?: 0
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler.adapter = adapter
        Task(this).execute("https://jsonplaceholder.typicode.com/albums")
        super.onViewCreated(view, savedInstanceState)
    }


    private class Task(context: AlbumsFragment) : AsyncTask<String, Int, String>() {

        private val activityReference: WeakReference<AlbumsFragment> = WeakReference(context)
        val fragment = activityReference.get()

        override fun doInBackground(vararg params: String): String {
            return makeHttpRequest(
                params[0],
                hashMapOf(Pair("userId", fragment?.userId.toString()))
            )
        }

        override fun onPostExecute(result: String) {
            fragment?.let { it.adapter.addAlbums(parseAlbumData(result)) }
        }
    }

    private fun showPhotos(id: Int) {
        (activity as MainActivity).replaceCurrentFragment(
            PhotosFragment.newInstance(
                id
            ), addToBackStack = true)
    }

    companion object {
        private const val USER_ID_KEY = "userID"
        fun newInstance(userId: Int): AlbumsFragment {
            val args = Bundle()
            args.putInt(USER_ID_KEY, userId)
            val fragment = AlbumsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
package com.mitya.photosapp.users

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import com.mitya.photosapp.*
import com.mitya.photosapp.albums.AlbumsFragment
import kotlinx.android.synthetic.main.fragment_list.*
import java.lang.ref.WeakReference

class UsersFragment : ListFragment() {

    private val adapter by lazy { UsersAdapter { showAlbums(it) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler.adapter = adapter
        Task(this).execute("https://jsonplaceholder.typicode.com/users")
        super.onViewCreated(view, savedInstanceState)
    }

    private class Task(context: UsersFragment) : AsyncTask<String, Int, String>() {

        private val activityReference: WeakReference<UsersFragment> = WeakReference(context)
        val fragment = activityReference.get()

        override fun doInBackground(vararg params: String): String {
            return makeHttpRequest(params[0])
        }

        override fun onPostExecute(result: String) {
            fragment?.let { it.adapter.addUsers(parseUsersData(result)) }
        }
    }

    private fun showAlbums(userId: Int) {
        (activity as MainActivity).replaceCurrentFragment(
            AlbumsFragment.newInstance(
                userId
            ), addToBackStack = true)
    }

    companion object {
        fun newInstance() = UsersFragment()
    }
}
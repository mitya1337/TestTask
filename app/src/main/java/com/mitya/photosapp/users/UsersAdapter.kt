package com.mitya.photosapp.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mitya.photosapp.R
import kotlinx.android.synthetic.main.item_list.view.*

class UsersAdapter(private val onClick: (Int) -> (Unit)) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private val items = ArrayList<User>()

    fun addUsers(users: List<User>) {
        items.clear()
        this.notifyDataSetChanged()
        items.addAll(users)
        this.notifyItemRangeInserted(0, items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], onClick)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User, onClick: (Int) -> Unit) {
            with(itemView) {
                title.text = user.name
                itemView.setOnClickListener { onClick(user.id) }
            }
        }
    }
}
package ru.uomkri.skbtest.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.uomkri.skbtest.R
import ru.uomkri.skbtest.repo.net.Repo

class FavListAdapter(private val data: List<Repo>) : RecyclerView.Adapter<ViewHolder>() {

    private val mutableData = data.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mutableData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mutableData[position]
        holder.name.text = item.name
    }

    fun clear() {
        mutableData.clear()
        notifyDataSetChanged()
    }

    fun refreshList(list: List<Repo>) {
        mutableData.clear()
        mutableData.addAll(list)
        notifyDataSetChanged()
    }

}
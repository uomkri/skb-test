package ru.uomkri.skbtest.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.uomkri.skbtest.databinding.ListItemBinding
import ru.uomkri.skbtest.repo.net.Repo

class ListAdapter() : PagedListAdapter<Repo, ViewHolder>(
    DiffUtilCallback()
) {

    var data = listOf<Repo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = ListItemBinding.inflate(layoutInflater).root

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.name.text = item?.name
    }

}

class DiffUtilCallback : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.fullName == newItem.fullName
    }

}
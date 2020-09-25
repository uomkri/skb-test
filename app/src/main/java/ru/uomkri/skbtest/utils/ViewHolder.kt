package ru.uomkri.skbtest.utils

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.uomkri.skbtest.R

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.repoName)
}
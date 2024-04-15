package com.example.gozlebrowser.presentation.homePage
import androidx.recyclerview.widget.DiffUtil
import com.example.gozlebrowser.domain.model.Recent

object RecentDiffCallback: DiffUtil.ItemCallback<Recent>() {
    override fun areItemsTheSame(oldItem: Recent, newItem: Recent): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Recent, newItem: Recent): Boolean {
        return oldItem == newItem
    }
}
package com.example.gozlebrowser.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import com.example.gozlebrowser.databinding.ItemRecentlyBinding
import com.example.gozlebrowser.domain.model.Recent
import com.squareup.picasso.Picasso

class RecentAdapter(private val context: Context) :
    ListAdapter<Recent, RecentViewHolder>(RecentDiffCallback) {

//    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val binding =
            ItemRecentlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val recentItem = getItem(position)
        with(holder.binding) {
            with(recentItem) {
                tvTitle.text = recentItem.title

//                Picasso.get().load(imageUrl).into(image)
                root.setOnClickListener {
                    //go to another screen
                    Toast.makeText(context, "Item clicked", Toast.LENGTH_SHORT).show()
//                    onCoinClickListener?.onCoinClick(this)
                }
            }
        }
    }

    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: Recent)
    }
}
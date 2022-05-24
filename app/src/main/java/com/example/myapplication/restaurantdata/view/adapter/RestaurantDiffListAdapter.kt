package com.example.myapplication.restaurantdata.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myapplication.R
import com.example.myapplication.databinding.ThreeColumnItemBinding
import com.example.myapplication.restaurantdata.model.RestaurantDTO

class RestaurantDiffListAdapter(private val listItemClickListener: OnListItemClickListener) :
    ListAdapter<RestaurantDTO, RecyclerView.ViewHolder>(RestaurantDTO.DIFF_CALLBACK) {

    /** Interface definition for a listener that will be notified when a list item is clicked. */
    interface OnListItemClickListener {
        fun onListItemClicked(restaurantDTO: RestaurantDTO)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RestaurantViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.three_column_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RestaurantViewHolder -> holder.bind(getItem(position))
        }
    }

    private inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ThreeColumnItemBinding.bind(itemView)

        init {
            itemView.isEnabled = true
        }

        fun bind(restaurantDTO: RestaurantDTO) {
            binding.restaurantName.text = restaurantDTO.name
            binding.description.text = restaurantDTO.description.replace(",", ", ", true)
            restaurantDTO.imgUrl?.let { imageUrl ->
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.mipmap.image_placeholder_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.restaurantCoverImage)

                itemView.setOnClickListener {
                    listItemClickListener.onListItemClicked(restaurantDTO)
                }
            }
        }
    }
}
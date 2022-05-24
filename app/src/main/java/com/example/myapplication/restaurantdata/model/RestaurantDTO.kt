package com.example.myapplication.restaurantdata.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("cover_img_url") val imgUrl: String? = null,
    @SerializedName("status") val status: String,
    @SerializedName("delivery_fee") val deliveryFee: Int
) : Parcelable {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RestaurantDTO>() {
            override fun areItemsTheSame(oldItem: RestaurantDTO, newItem: RestaurantDTO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RestaurantDTO, newItem: RestaurantDTO): Boolean {
                return oldItem == newItem
            }
        }
    }
}

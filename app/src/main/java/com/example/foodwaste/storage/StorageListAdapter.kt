package com.example.foodwaste.storage

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwaste.R
import com.example.foodwaste.StorageUtils
import com.example.foodwaste.model.FoodItem

class StorageListAdapter(private var mList: List<FoodItem>, private val activity: Activity) :
    RecyclerView.Adapter<StorageListAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.storage_list_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]
        holder.titleView.text = item.name
        holder.dateView.text = item.expirationDate
        holder.co2View.text = "${item.co2}kg"
        holder.thumbnailView.setBackgroundResource(StorageUtils.getPictureResourceId(item.name))

        if (item.isShared) {
            // Pills
            holder.pillView.isVisible = true
            holder.pillView.text = "Shared"
            holder.pillView.background =
                ContextCompat.getDrawable(activity, R.drawable.pill_bg_shared)

            // Share View
            holder.shareView.isVisible = true
            holder.shareIcon.isVisible = true
            holder.shareView.text = "You are sharing this item with your flatmates"
        } else {
            holder.shareView.isVisible = false
            holder.shareIcon.isVisible = false
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    fun update(list: List<FoodItem>) {
        mList = list
        notifyDataSetChanged()
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        //        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val titleView: TextView = itemView.findViewById(R.id.storage_list_item_title)
        val dateView: TextView = itemView.findViewById(R.id.storage_list_item_date)
        val dateIcon: ImageView = itemView.findViewById(R.id.storage_list_item_calendar_icon)
        val co2View: TextView = itemView.findViewById(R.id.storage_list_item_co2)
        val pillView: TextView = itemView.findViewById(R.id.storage_list_item_pill)
        val shareView: TextView = itemView.findViewById(R.id.storage_list_item_shared_text)
        val shareIcon: ImageView = itemView.findViewById(R.id.storage_list_item_shared_icon)
        val thumbnailView: ImageView = itemView.findViewById(R.id.storage_list_item_thumbnail)
    }
}
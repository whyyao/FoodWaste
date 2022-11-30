package com.example.foodwaste.storage

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwaste.R
import com.example.foodwaste.StorageUtils
import com.example.foodwaste.model.FoodItem

class ExpiringListAdapter(private var mList: List<FoodItem>, private val activity: Activity, private val isChecked: (FoodItem, Boolean) -> Unit) :
    RecyclerView.Adapter<ExpiringListAdapter.ViewHolder>() {

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

        // sets the image to the imageview from our itemHolder class
//        holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.titleView.text = item.name
        holder.titleView.setTextColor(
            ContextCompat.getColor(activity, R.color.white)
        )
        holder.dateView.setTextColor(
            ContextCompat.getColor(activity, R.color.white_subtitle)
        )
        holder.co2View.setTextColor(
            ContextCompat.getColor(activity, R.color.white_subtitle)
        )
        holder.dateView.text = item.expirationDate
        holder.co2View.text = "${item.co2}kg"
        holder.pillView.isVisible = false
        holder.checkBoxView.isVisible = true
        holder.checkBoxView.setOnCheckedChangeListener { buttonView, isChecked ->
            isChecked(item, isChecked)
        }
        holder.thumbnailView.setBackgroundResource(StorageUtils.getPictureResourceId(item.name))
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
        val co2View: TextView = itemView.findViewById(R.id.storage_list_item_co2)
        val pillView: TextView = itemView.findViewById(R.id.storage_list_item_pill)
        val checkBoxView: CheckBox = itemView.findViewById(R.id.storage_list_item_check_box)
        val thumbnailView: ImageView = itemView.findViewById(R.id.storage_list_item_thumbnail)

    }
}
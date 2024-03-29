package com.example.foodwaste.storage

import android.annotation.SuppressLint
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
import com.example.foodwaste.StringUtils
import com.example.foodwaste.model.FoodItem
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class StorageListAdapter(private var mList: List<FoodItem>, private val activity: Activity) :
    RecyclerView.Adapter<StorageListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.storage_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]

        // Basic Information
        holder.titleView.text = item.name
        holder.co2View.text = "${item.co2}kg"
        holder.thumbnailView.setBackgroundResource(StorageUtils.getPictureResourceId(item.name))
        holder.dateView.text = StringUtils.getPrettyDate(item.expirationDate)

        // Expiration Date Pills
        holder.pillView.isVisible = true
        holder.pillView.background =
            ContextCompat.getDrawable(activity, R.drawable.pill_bg)

        val apiDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val apiDate: Date = apiDateFormat.parse(item.expirationDate)
        val apiLocalTime: LocalDateTime =
            LocalDateTime.ofInstant(apiDate.toInstant(), ZoneId.systemDefault())
        val minus4days = apiLocalTime.minusDays(4).toLocalDate()
        val minus3days = apiLocalTime.minusDays(3).toLocalDate()
        val minus2days = apiLocalTime.minusDays(2).toLocalDate()

        val now = LocalDate.now()
        if (now.isAfter(apiLocalTime.toLocalDate())) {
            holder.pillView.text = "EXPIRED"
        } else if (now.isAfter(minus2days)) {
            holder.pillView.text = "1 Day"
        } else if (now.isAfter(minus3days)) {
            holder.pillView.text = "2 Days"
        } else if (now.isAfter(minus4days)) {
            holder.pillView.text = "3 Days"
        } else {
            holder.pillView.isVisible = false
        }

        // Mark shared items
        if (item.isShared) {
            // Pills
            holder.pillView.isVisible = true
            holder.pillView.text = "Shared"
            holder.pillView.background =
                ContextCompat.getDrawable(activity, R.drawable.pill_bg_shared)

            // Share View
            holder.shareView.isVisible = true
            holder.shareIcon.isVisible = true
            holder.shareView.text = "Item is shared in your flat."
        } else {
            // Otherwise hide share information
            holder.shareView.isVisible = false
            holder.shareIcon.isVisible = false
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: List<FoodItem>) {
        mList = list
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
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
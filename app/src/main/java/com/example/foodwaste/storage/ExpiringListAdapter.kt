package com.example.foodwaste.storage

import android.annotation.SuppressLint
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
import com.example.foodwaste.StringUtils
import com.example.foodwaste.model.FoodItem

class ExpiringListAdapter(
    private var mList: List<FoodItem>,
    private val activity: Activity,
    private val isChecked: (FoodItem, Boolean) -> Unit
) :
    RecyclerView.Adapter<ExpiringListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.storage_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        // Basic Information
        holder.titleView.text = item.name
        holder.titleView.setTextColor(
            ContextCompat.getColor(activity, R.color.white)
        )
        holder.dateView.setTextColor(
            ContextCompat.getColor(activity, R.color.white_subtitle)
        )
        holder.dateView.text = StringUtils.getPrettyDate(item.expirationDate)
        holder.co2View.setTextColor(
            ContextCompat.getColor(activity, R.color.white_subtitle)
        )
        holder.co2View.text = "${item.co2}kg"
        holder.thumbnailView.setBackgroundResource(StorageUtils.getPictureResourceId(item.name))

        // Hide Pills
        holder.pillView.isVisible = false

        // Show checkbox
        holder.checkBoxView.isVisible = true
        holder.checkBoxView.setOnCheckedChangeListener { buttonView, isChecked ->
            isChecked(item, isChecked)
        }
        holder.checkBoxView.isChecked = item.isChecked
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
        val co2View: TextView = itemView.findViewById(R.id.storage_list_item_co2)
        val pillView: TextView = itemView.findViewById(R.id.storage_list_item_pill)
        val checkBoxView: CheckBox = itemView.findViewById(R.id.storage_list_item_check_box)
        val thumbnailView: ImageView = itemView.findViewById(R.id.storage_list_item_thumbnail)

    }
}
package com.example.foodwaste.shopping

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwaste.R
import com.example.foodwaste.StorageUtils
import com.example.foodwaste.model.FoodItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.w3c.dom.Text
import java.lang.reflect.Type


class ShoppingListAdapter(
    private var mList: List<FoodItem>,
    private val activity: Activity,
    private val cancel: (foodItem: FoodItem) -> Unit
) :
    RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.storage_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        val sharedPref = activity.getPreferences(
            Context.MODE_PRIVATE
        )
        val listType: Type = object : TypeToken<List<FoodItem?>?>() {}.type
        Gson().fromJson<List<FoodItem>>(sharedPref.getString("stock", ""), listType)?.let {
            if (it.any { it.name == item.name }) {
                holder.backgroundLayout.background =
                    ColorDrawable(activity.resources.getColor(R.color.repeated_item_color))

                // Pills
                holder.pillView.isVisible = true
                holder.pillView.text = "Repeated"
                holder.pillView.background =
                    ContextCompat.getDrawable(activity, R.drawable.pill_bg_repeated)

                // Date text
                holder.shareIcon.isVisible = true
                holder.shareView.isVisible = true
                holder.shareIcon.setImageDrawable(
                    ContextCompat.getDrawable(activity, R.drawable.ic_fridge)
                )
                holder.shareView.text =
                    "You already have some in your fridge, lets try something else!"
            } else {
                holder.pillView.isVisible = false
                holder.shareIcon.isVisible = false
                holder.shareView.isVisible = false
            }
        }
        holder.titleView.text = item.name
        holder.co2View.isVisible = false
        holder.co2IconView.isVisible = false
        holder.cancelIcon.isVisible = true
        holder.cancelIcon.setOnClickListener {
            cancel.invoke(item)
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
        val dateIcon: ImageView = itemView.findViewById(R.id.storage_list_item_calendar_icon)
        val co2View: TextView = itemView.findViewById(R.id.storage_list_item_co2)
        val co2IconView: ImageView = itemView.findViewById(R.id.storage_list_item_co2_icon)
        val pillView: TextView = itemView.findViewById(R.id.storage_list_item_pill)
        val backgroundLayout: ConstraintLayout = itemView.findViewById(R.id.storage_list_item_bg)
        val cancelIcon: ImageView = itemView.findViewById(R.id.storage_list_item_cancel)
        val thumbnailView: ImageView = itemView.findViewById(R.id.storage_list_item_thumbnail)
        val shareView: TextView = itemView.findViewById(R.id.storage_list_item_shared_text)
        val shareIcon: ImageView = itemView.findViewById(R.id.storage_list_item_shared_icon)
    }
}
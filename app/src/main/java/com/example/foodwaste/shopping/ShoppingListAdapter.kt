package com.example.foodwaste.shopping

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwaste.R
import com.example.foodwaste.model.FoodItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class ShoppingListAdapter(private var mList: List<FoodItem>, private val activity: Activity) :
    RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

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
        val sharedPref = activity.getPreferences(
            Context.MODE_PRIVATE
        )
        val listType: Type = object : TypeToken<List<FoodItem?>?>() {}.type
        Gson().fromJson<List<FoodItem>>(sharedPref.getString("stock", ""), listType)?.let {
            it.forEach { foodItem ->
                if (foodItem.name == item.name) {
                    holder.backgroundLayout.background =
                        ColorDrawable(activity.resources.getColor(R.color.repeated_item_color))
                }
            }
        }
        holder.titleView.text = item.name
        holder.dateView.text = item.expirationDate
        holder.co2View.text = "${item.co2}kg"
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
        val backgroundLayout: ConstraintLayout = itemView.findViewById(R.id.storage_list_item_bg)
    }
}
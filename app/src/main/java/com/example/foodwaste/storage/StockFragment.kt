package com.example.foodwaste.storage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodwaste.databinding.FragmentStockBinding
import com.example.foodwaste.model.FoodItem
import com.google.gson.Gson


class StockFragment : Fragment() {
    private var _binding: FragmentStockBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // this creates a vertical layout Manager
        binding.storageRecyclerView.adapter = StorageListAdapter(
            MutableList(20) { FoodItem("apple") }
        )

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val json = sharedPref.getString("saved", "")
        val foodItem = Gson().fromJson(json, FoodItem::class.java)

//        binding.text.text = foodItem?.name ?: ""
    }
}
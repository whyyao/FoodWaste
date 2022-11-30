package com.example.foodwaste.storage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.foodwaste.R
import com.example.foodwaste.databinding.FragmentStockBinding
import com.example.foodwaste.model.FoodItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class StockFragment : Fragment() {
    private var _binding: FragmentStockBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val stockListAdapter: StorageListAdapter get() = binding.storageRecyclerViewStockList.adapter as StorageListAdapter
    private val expiringListAdapter: ExpiringListAdapter get() = binding.storageRecyclerViewExpiredList.adapter as ExpiringListAdapter

    private var expiringFoodList: List<FoodItem> = emptyList()
        set(value) {
            field = value
            expiringListAdapter.update(value)
        }
    private var stockFoodList: List<FoodItem> = emptyList()
        set(value) {
            field = value
            stockListAdapter.update(value)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockBinding.inflate(inflater, container, false)
        requireActivity().window.statusBarColor =
            resources.getColor(R.color.accent, requireActivity().theme)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // this creates a vertical layout Manager

        val sharedPref = requireActivity().getPreferences(
            Context.MODE_PRIVATE
        )
        val listType: Type = object : TypeToken<List<FoodItem?>?>() {}.type
        val gson = Gson()
        val stockList =
            gson.fromJson<List<FoodItem>>(sharedPref.getString("stock", ""), listType).orEmpty()
        val expiringList =
            gson.fromJson<List<FoodItem>>(sharedPref.getString("expiring", ""), listType).orEmpty()
        binding.storageRecyclerViewStockList.adapter = StorageListAdapter(
            stockList
        )
        binding.storageRecyclerViewExpiredList.adapter = ExpiringListAdapter(
            expiringList,
            requireActivity(),
            isChecked = this::isChecked
        )
        stockFoodList = stockList
        expiringFoodList = expiringList
        binding.fragmentStockUseButton.setOnClickListener {
            expiringFoodList = expiringFoodList.filter { !it.isChecked }
            val sharedPref = requireActivity().getPreferences(
                Context.MODE_PRIVATE
            )
            sharedPref?.edit()?.putString("expiring", Gson().toJson(expiringFoodList))?.apply()
        }
        binding.fragmentStockShareButton.setOnClickListener {
            val toAddList = expiringFoodList.filter { it.isChecked }
            expiringFoodList = expiringFoodList.filter { !it.isChecked }
            val sharedPref = requireActivity().getPreferences(
                Context.MODE_PRIVATE
            )
            sharedPref?.edit()?.putString("expiring", Gson().toJson(expiringFoodList))?.apply()

            val tempList = mutableListOf<FoodItem>()
            toAddList.forEach {
                tempList.add(it.apply {
                    it.isShared = true
                })
            }
            tempList.addAll(stockFoodList)
            stockFoodList = tempList
            sharedPref?.edit()?.putString("stock", Gson().toJson(stockFoodList))?.apply()
        }
    }

    private fun isChecked(foodItem: FoodItem, isChecked: Boolean) {
        val tempList = expiringFoodList.toMutableList()
        tempList.forEach { item ->
            if (item.name == foodItem.name) {
                item.isChecked = isChecked
            }
        }
        expiringFoodList = tempList
    }

    private fun checkViewState() {
        if (expiringFoodList.isEmpty()) {
            binding.fragmentStockExpiringText.isVisible = false
            binding.storageRecyclerViewExpiredList.isVisible = false
            binding.fragmentStockBackground.isVisible = false
            binding.fragmentStockUseButton.isVisible = false
            binding.fragmentStockShareButton.isVisible = false
        } else {
            binding.fragmentStockExpiringText.isVisible = true
            binding.storageRecyclerViewExpiredList.isVisible = true
            binding.fragmentStockBackground.isVisible = true
            binding.fragmentStockUseButton.isVisible = true
            binding.fragmentStockShareButton.isVisible = true
        }
    }
}

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

    private val stockListAdapter: StorageListAdapter get() = binding.storageRecyclerViewStockList.adapter as StorageListAdapter

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
        binding.storageRecyclerViewStockList.adapter = StorageListAdapter(
            testStockList
        )

        binding.storageRecyclerViewExpiredList.adapter = ExpiringListAdapter(
            testExpiringList
        )
    }
}

val testExpiringList = listOf(
    FoodItem(
        name = "apple",
        expirationDate = "2022-11-30",
        co2 = 2
    ),
    FoodItem(
        name = "onions",
        expirationDate = "2022-11-29",
        co2 = 3
    )
)

val testStockList = listOf(
    FoodItem(
        name = "apple",
        expirationDate = "2022-12-5",
        co2 = 2
    ),
    FoodItem(
        name = "ginger",
        expirationDate = "2022-12-10",
        co2 = 1
    )
)
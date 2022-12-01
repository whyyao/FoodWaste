package com.example.foodwaste.storage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.foodwaste.R
import com.example.foodwaste.StorageUtils
import com.example.foodwaste.databinding.FragmentStockBinding
import com.example.foodwaste.model.FoodItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class StockFragment : Fragment() {
    private var _binding: FragmentStockBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val stockListAdapter: StorageListAdapter get() = binding.storageRecyclerViewStockList.adapter as StorageListAdapter
    private val expiringListAdapter: ExpiringListAdapter get() = binding.storageRecyclerViewExpiredList.adapter as ExpiringListAdapter

    private var expiringFoodList: List<FoodItem> = emptyList()
        set(value) {
            field = value.sortedBy { it.expirationDate }
            expiringListAdapter.update(field)
        }
    private var stockFoodList: List<FoodItem> = emptyList()
        set(value) {
            field = value.sortedBy { it.expirationDate }
            stockListAdapter.update(field)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stockList = StorageUtils.getStockFoodList(requireActivity())
        val expiringList = StorageUtils.getExpiringFoodList(requireActivity())

        // Adapters
        binding.storageRecyclerViewStockList.adapter = StorageListAdapter(
            stockList,
            activity = requireActivity()
        )
        binding.storageRecyclerViewExpiredList.adapter = ExpiringListAdapter(
            expiringList,
            requireActivity(),
            isChecked = this::isChecked
        )

        // Assign Data
        stockFoodList = stockList
        expiringFoodList = expiringList


        binding.fragmentStockUseButton.setOnClickListener {
            if (expiringList.all { !it.isChecked }) {
                return@setOnClickListener
            }
            val usedList = expiringFoodList.filter { it.isChecked }
            val sumOfCo2 = usedList.sumOf { it.co2 }
            expiringFoodList = expiringFoodList.filter { !it.isChecked }
            StorageUtils.saveToExpiringFoodList(requireActivity(), expiringFoodList)
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setTitle("THANK YOU!")
                .setMessage("You’ve consumed your food and avoided $sumOfCo2 kg CO2 emission.")
                .setPositiveButton("Ok", null)
                .show()
            checkViewState()
        }
        binding.fragmentStockShareButton.setOnClickListener {
            val toAddList = expiringFoodList.filter { it.isChecked }
            expiringFoodList = expiringFoodList.filter { !it.isChecked }
            StorageUtils.saveToExpiringFoodList(requireActivity(), expiringFoodList)

            val tempList = mutableListOf<FoodItem>()
            toAddList.forEach {
                tempList.add(it.apply {
                    it.isShared = true
                })
            }
            tempList.addAll(stockFoodList)
            stockFoodList = tempList
            StorageUtils.saveToStockFoodList(requireActivity(), stockFoodList)
            checkViewState()
        }
        binding.fragmentStockDismissButton.setOnClickListener {
            val toAddList = expiringFoodList.filter { it.isChecked }
            expiringFoodList = expiringFoodList.filter { !it.isChecked }
            StorageUtils.saveToExpiringFoodList(requireActivity(), expiringFoodList)

            val tempList = mutableListOf<FoodItem>()
            tempList.addAll(toAddList)
            tempList.addAll(stockFoodList)
            stockFoodList = tempList
            StorageUtils.saveToStockFoodList(requireActivity(), stockFoodList)
            checkViewState()
        }
        checkViewState()
    }

    private fun isChecked(foodItem: FoodItem, isChecked: Boolean) {
        val tempList = expiringFoodList.toMutableList()
        tempList.forEach { item ->
            if (item.name.equals(foodItem.name, ignoreCase = true)) {
                item.isChecked = isChecked
            }
        }
        expiringFoodList = tempList
    }

    private fun checkViewState() {
        if (expiringFoodList.isEmpty()) {
            requireActivity().window.statusBarColor =
                resources.getColor(R.color.white, null)
            binding.fragmentStockExpiringText.isVisible = false
            binding.storageRecyclerViewExpiredList.isVisible = false
            binding.fragmentStockBackground.isVisible = false
            binding.fragmentStockUseButton.isVisible = false
            binding.fragmentStockShareButton.isVisible = false
            binding.fragmentStockDismissButton.isVisible = false
        } else {
            requireActivity().window.statusBarColor =
                resources.getColor(R.color.accent, null)
            binding.fragmentStockExpiringText.isVisible = true
            binding.storageRecyclerViewExpiredList.isVisible = true
            binding.fragmentStockBackground.isVisible = true
            binding.fragmentStockUseButton.isVisible = true
            binding.fragmentStockShareButton.isVisible = true
            binding.fragmentStockDismissButton.isVisible = true
        }
    }
}

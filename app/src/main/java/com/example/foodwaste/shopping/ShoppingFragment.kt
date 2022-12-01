package com.example.foodwaste.shopping

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.foodwaste.CaptureAct
import com.example.foodwaste.R
import com.example.foodwaste.StorageUtils
import com.example.foodwaste.databinding.FragmentShoppingBinding
import com.example.foodwaste.model.FoodItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ShoppingFragment : Fragment() {

    private var _binding: FragmentShoppingBinding? = null

    private val binding get() = _binding!!
    private var shoppingList: List<FoodItem> = emptyList()
        set(value) {
            field = value
            adapter.update(value)
            checkViewState()
        }

    private val adapter get() = binding.storageRecyclerViewShoppingTrackerList.adapter as ShoppingListAdapter

    var barLaucher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents != null) {
            itemScanned(result.contents)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        barLaucher = registerForActivityResult(
            ScanContract()
        ) { result: ScanIntentResult ->
            if (result.contents != null) {
                itemScanned(result.contents)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        requireActivity().window.statusBarColor =
            resources.getColor(android.R.color.background_light, null)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tempStorageList = StorageUtils.getShoppingList(requireActivity())
        binding.fragmentShoppingScanButton.setOnClickListener {
            scanCode()
        }
        binding.storageRecyclerViewShoppingTrackerList.adapter =
            ShoppingListAdapter(tempStorageList, requireActivity(), cancel = {
                val tempList = shoppingList.toMutableList()
                tempList.remove(it)
                shoppingList = tempList
            })
        shoppingList = tempStorageList
        binding.fragmentShoppingAddToStorageButton.setOnClickListener {
            saveToStorage()
        }

        checkViewState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun scanCode() {
        val scanOptions = ScanOptions()
        scanOptions.setPrompt("Volume up to flash on")
        scanOptions.setBeepEnabled(true)
        scanOptions.setOrientationLocked(true)
        scanOptions.captureActivity = CaptureAct::class.java
        barLaucher.launch(scanOptions)
    }

    private fun itemScanned(stream: String) {
        try {
            Gson().fromJson(stream, FoodItem::class.java)?.let {
                val tempList = shoppingList.toMutableList()
                tempList.add(it)
                shoppingList = tempList
            }
        } catch (e: java.lang.Exception) {
            MaterialAlertDialogBuilder(
                requireContext(), R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog
            ).setTitle("Oops!").setMessage("Unrecognized code").setPositiveButton("Ok", null).show()
        }
    }

    private fun saveToStorage() {
        val stockList = StorageUtils.getStockFoodList(requireActivity())
        val tempList: MutableList<FoodItem> = mutableListOf()
        tempList.addAll(stockList)
        tempList.addAll(shoppingList)
        StorageUtils.saveToStockFoodList(activity = requireActivity(), list = tempList)
        shoppingList = emptyList()
        StorageUtils.saveToShoppingList(activity = requireActivity(), list = shoppingList)
        checkViewState()
    }

    private fun checkViewState() {
        StorageUtils.saveToShoppingList(activity = requireActivity(), list = shoppingList)
        binding.fragmentShoppingAddToStorageButton.isVisible = shoppingList.isNotEmpty()
        binding.fragmentShoppingEmptyView.isVisible = shoppingList.isEmpty()
    }
}
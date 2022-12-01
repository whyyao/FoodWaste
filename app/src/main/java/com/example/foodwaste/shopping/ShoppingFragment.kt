package com.example.foodwaste.shopping

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodwaste.CaptureAct
import com.example.foodwaste.databinding.FragmentShoppingBinding
import com.example.foodwaste.model.FoodItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.lang.reflect.Type


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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        requireActivity().window.statusBarColor =
            resources.getColor(android.R.color.background_light, null)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentShoppingScanButton.setOnClickListener {
            scanCode()
        }
        binding.storageRecyclerViewShoppingTrackerList.adapter = ShoppingListAdapter(
            emptyList(),
            requireActivity(),
            cancel = {
                val tempList = shoppingList.toMutableList()
                tempList.remove(it)
                shoppingList = tempList
            }
        )
        binding.fragmentShoppingAddToStorageButton.setOnClickListener {
            saveToStorage()
        }
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
        Gson().fromJson(stream, FoodItem::class.java)?.let {
            val tempList = shoppingList.toMutableList()
            tempList.add(it)
            shoppingList = tempList
        }
    }

    private fun saveToStorage() {
        val sharedPref = requireActivity().getPreferences(
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        with(sharedPref?.edit() ?: return) {
            val listType: Type = object : TypeToken<List<FoodItem?>?>() {}.type
            val stockList =
                Gson().fromJson<List<FoodItem>>(sharedPref.getString("stock", ""), listType)
                    .orEmpty()
            val tempList: MutableList<FoodItem> = mutableListOf()
            tempList.addAll(stockList)
            tempList.addAll(shoppingList)
            putString("stock", gson.toJson(tempList))
            apply()
        }
        shoppingList = emptyList()
    }
}
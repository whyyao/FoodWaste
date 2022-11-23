package com.example.foodwaste

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.foodwaste.databinding.FragmentFirstBinding
import com.example.foodwaste.model.FoodItem
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            scanCode()
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

    var barLaucher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents != null) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Result")
            builder.setMessage(result.contents)
            builder.setPositiveButton("OK") { dialogInterface, _ ->
                run {
                    saveItem(result.contents)
                    dialogInterface.dismiss()
                }
            }
            builder.show()
        }
    }

    private fun saveItem(stream: String) {
        val foodItem = Gson().fromJson(stream, FoodItem::class.java)
        println("hey$foodItem")
    }
}
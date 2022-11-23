package com.example.foodwaste

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.foodwaste.databinding.FragmentShoppingBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ShoppingFragment : Fragment() {

    private var _binding: FragmentShoppingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        barLaucher = registerForActivityResult(
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
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

    private fun saveItem(stream: String) {
        val sharedPref = activity?.getPreferences(
            Context.MODE_PRIVATE
        )
        with(sharedPref?.edit() ?: return) {
            putString("saved", stream)
            apply()
        }
    }
}
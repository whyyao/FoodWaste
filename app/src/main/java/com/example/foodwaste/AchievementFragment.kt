package com.example.foodwaste

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.foodwaste.databinding.FragmentAchievementBinding

class AchievementFragment(private val mode: Int) : Fragment() {

    private var _binding: FragmentAchievementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementBinding.inflate(inflater, container, false)
        requireActivity().window.statusBarColor =
            resources.getColor(android.R.color.background_light, null)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor =
            resources.getColor(R.color.achievement_top_color, null)

        binding.fragmentAchievementImage.setBackgroundResource(
            if (mode == 1 || mode == 2) {
                R.drawable.achievement1
            } else {
                R.drawable.achievement2
            }
        )
    }

}
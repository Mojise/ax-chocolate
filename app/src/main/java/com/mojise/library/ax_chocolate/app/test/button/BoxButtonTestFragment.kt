package com.mojise.library.ax_chocolate.app.test.button

import android.os.Bundle
import android.view.View
import com.mojise.library.ax_chocolate.app.R
import com.mojise.library.ax_chocolate.app.base.BaseFragment
import com.mojise.library.ax_chocolate.app.databinding.FragmentChocolateButtonTestBinding

class BoxButtonTestFragment : BaseFragment<FragmentChocolateButtonTestBinding>(R.layout.fragment_chocolate_button_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boxButtonDefault.setOnClickListener {
            binding.boxButtonDefault.setLoadingWithTimeout(1000)
        }

        binding.boxButtonSimple.setOnClickListener {
            //binding.boxButtonSimple.isLoading = true
            binding.boxButtonSimple.setLoadingWithTimeout(1000)
//            binding.boxButtonSimple.isEnabled = false
//            binding.boxButtonSimple.postDelayed({
//                binding.boxButtonSimple.isEnabled = true
//            }, 1000)
        }
        binding.boxButtonSimple2.setOnClickListener {
            binding.boxButtonSimple2.setLoadingWithTimeout(1000)
        }
    }
}
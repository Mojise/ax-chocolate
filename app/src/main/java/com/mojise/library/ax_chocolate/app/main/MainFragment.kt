package com.mojise.library.ax_chocolate.app.main

import android.os.Bundle
import android.view.View
import com.mojise.library.ax_chocolate.app.R
import com.mojise.library.ax_chocolate.app.base.BaseFragment
import com.mojise.library.ax_chocolate.app.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNavigateToChocolateViewTest.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_chocolateViewTestFragment)
        }
        binding.btnNavigateToFloatingBottomSheetTest.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_floatingBottomSheetTestFragment)
        }
    }
}
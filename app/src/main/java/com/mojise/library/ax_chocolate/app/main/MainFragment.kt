package com.mojise.library.ax_chocolate.app.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.IdRes
import com.google.android.material.transition.MaterialSharedAxis
import com.mojise.library.ax_chocolate.app.R
import com.mojise.library.ax_chocolate.app.base.BaseFragment
import com.mojise.library.ax_chocolate.app.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            btnNavigateToChocolateViewTest.onClickedTo(R.id.action_mainFragment_to_chocolateViewTestFragment)
            btnNavigateToFloatingBottomSheetTest.onClickedTo(R.id.action_mainFragment_to_floatingBottomSheetTestFragment)
            btnNavigateToButtonTest.onClickedTo(R.id.action_mainFragment_to_boxButtonTestFragment)
        }
    }

    private fun Button.onClickedTo(@IdRes resId: Int) = setOnClickListener {
        navController.navigate(resId)
    }
}
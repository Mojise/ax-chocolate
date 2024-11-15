package com.mojise.library.ax_chocolate.app.test.bottomsheet

import android.os.Bundle
import android.view.View
import com.mojise.library.ax_chocolate.app.R
import com.mojise.library.ax_chocolate.app.base.BaseFragment
import com.mojise.library.ax_chocolate.app.databinding.FragmentBottomSheetTestBinding

class FloatingBottomSheetTestFragment : BaseFragment<FragmentBottomSheetTestBinding>(R.layout.fragment_bottom_sheet_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBottomSheetTest1.setOnClickListener {
            TestFloatingBottomSheet().show(childFragmentManager, "TestFloatingBottomSheet")
        }
    }
}
package com.mojise.library.ax_chocolate.app.test.bottomsheet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.transition.MaterialSharedAxis
import com.mojise.library.ax_chocolate.app.R
import com.mojise.library.ax_chocolate.app.base.BaseFragment
import com.mojise.library.ax_chocolate.app.databinding.FragmentBottomSheetTestBinding
import com.mojise.library.ax_chocolate.app.exts.setOnClickListener
import com.mojise.library.ax_chocolate.app.util.TAG

class FloatingBottomSheetTestFragment : BaseFragment<FragmentBottomSheetTestBinding>(R.layout.fragment_bottom_sheet_test) {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(TAG, "onAttach!! context=$context")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBottomSheetTestSimple.setOnClickListener {
            SimpleBottomSheet()
                .show(childFragmentManager, "SimpleFloatingBottomSheet")
        }
        binding.btnBottomSheetTest1.setOnClickListener {
            CustomBottomSheet()
                .show(childFragmentManager, "CustomFloatingBottomSheet")
        }
    }
}
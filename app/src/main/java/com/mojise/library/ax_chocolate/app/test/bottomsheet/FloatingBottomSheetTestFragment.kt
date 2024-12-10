package com.mojise.library.ax_chocolate.app.test.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.View
import com.mojise.library.ax_chocolate.app.R
import com.mojise.library.ax_chocolate.app.base.BaseFragment
import com.mojise.library.ax_chocolate.app.databinding.FragmentBottomSheetTestBinding
import com.mojise.library.ax_chocolate.app.util.TAG

class FloatingBottomSheetTestFragment : BaseFragment<FragmentBottomSheetTestBinding>(R.layout.fragment_bottom_sheet_test) {

    private lateinit var bottomSheetSimple: SimpleBottomSheet
    private lateinit var bottomSheetCustom: CustomBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomSheetSimple = SimpleBottomSheet()
        bottomSheetCustom = CustomBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "FloatingBottomSheetTestFragment :: onViewCreated :: bottomSheetSimple=$bottomSheetSimple")
        Log.e(TAG, "FloatingBottomSheetTestFragment :: onViewCreated :: childFragmentManager=$childFragmentManager")
        Log.e(TAG, "FloatingBottomSheetTestFragment :: onViewCreated :: childFragmentManager.aa=${childFragmentManager.findFragmentByTag("SimpleFloatingBottomSheet")}")

        if (childFragmentManager.findFragmentByTag("SimpleFloatingBottomSheet") == null) {
            bottomSheetSimple
                .show(childFragmentManager, "SimpleFloatingBottomSheet")
        }

        binding.btnBottomSheetTestSimple.setOnClickListener {
            bottomSheetSimple
                .show(childFragmentManager, "SimpleFloatingBottomSheet")
        }
        binding.btnBottomSheetTest1.setOnClickListener {
            bottomSheetCustom
                .show(childFragmentManager, "CustomFloatingBottomSheet")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //bottomSheetSimple.dismiss()
        super.onSaveInstanceState(outState)
    }
}
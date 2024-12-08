package com.mojise.library.ax_chocolate.app.test.bottomsheet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.mojise.library.ax_chocolate.app.R
import com.mojise.library.ax_chocolate.app.databinding.FragmentFloatingBottomSheetSimpleBinding
import com.mojise.library.ax_chocolate.app.util.TAG
import com.mojise.library.chocolate.bottomsheet.ChocolateBottomSheetDialogFragment
import com.mojise.library.chocolate.ext.dp

class SimpleBottomSheet : ChocolateBottomSheetDialogFragment<FragmentFloatingBottomSheetSimpleBinding>() {

    override val layoutResId: Int = R.layout.fragment_floating_bottom_sheet_simple

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTest1.setOnClickListener {
            binding.text = "Test1"
        }
        binding.btnTest2.setOnClickListener {
            binding.text = "Test2"
        }
        binding.btnTest3.setOnClickListener {
            binding.text = "Test3"
        }
    }
}
package com.mojise.library.ax_chocolate.app.test.view

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import com.mojise.library.ax_chocolate.app.R
import com.mojise.library.ax_chocolate.app.base.BaseFragment
import com.mojise.library.ax_chocolate.app.databinding.FragmentChocolateViewTestBinding

class ChocolateViewTestFragment : BaseFragment<FragmentChocolateViewTestBinding>(R.layout.fragment_chocolate_view_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonContainer.children
            .forEach { it.onClicked() }

        binding.btnAllEnabled.setOnClickListener {
            binding.buttonContainer.children
                .filterNot(View::isEnabled)
                .forEach {
                    it.isSelected = false
                    it.isEnabled = true
                }
        }
    }

    private fun View.onClicked() = setOnClickListener {
        when {
            isSelected.not() -> isSelected = true
            isEnabled -> isEnabled = false
        }
    }
}
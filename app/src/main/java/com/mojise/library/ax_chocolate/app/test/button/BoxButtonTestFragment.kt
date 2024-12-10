package com.mojise.library.ax_chocolate.app.test.button

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.mojise.library.ax_chocolate.app.R
import com.mojise.library.ax_chocolate.app.base.BaseFragment
import com.mojise.library.ax_chocolate.app.databinding.FragmentChocolateButtonTestBinding
import com.mojise.library.chocolate.button.box.ChocolateBoxButton

class BoxButtonTestFragment : BaseFragment<FragmentChocolateButtonTestBinding>(R.layout.fragment_chocolate_button_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boxButton1.onClickedLoading()

        binding.boxButtonDefault.onClickedLoading()
        binding.boxButtonTest2.setOnClickListener {
            binding.boxButtonTest2.isEnabled = false
        }
        binding.boxButtonOnlyIcon.onClickedLoading()
        binding.boxButtonSimple.onClickedLoading()
        binding.boxButtonSimple2.onClickedLoading()
        binding.boxButtonSpreadInside.onClickedLoading()
        binding.boxButtonGradient.onClickedLoading()
        binding.editText.addTextChangedListener {
            binding.btnSend.isEnabled = it.isNullOrBlank().not()
        }
        binding.btnSend.setOnClickListener {
            binding.editText.text?.clear()
        }
        binding.boxButtonEnabled.setOnClickListener {
            binding.buttonContainer.children
                .filterNot(View::isEnabled)
                .forEach { it.isEnabled = true }
        }
    }

    private fun ChocolateBoxButton.onClickedLoading() {
        setOnClickListener {
            setLoadingWithTimeout(3000)
        }
    }
}
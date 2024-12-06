package com.mojise.library.ax_chocolate.app.test.button

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.mojise.library.ax_chocolate.app.R
import com.mojise.library.ax_chocolate.app.base.BaseFragment
import com.mojise.library.ax_chocolate.app.databinding.FragmentChocolateButtonTestBinding

class BoxButtonTestFragment : BaseFragment<FragmentChocolateButtonTestBinding>(R.layout.fragment_chocolate_button_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boxButtonDefault.setOnClickListener {
            binding.boxButtonDefault.setLoadingWithTimeout(1000)
        }

        binding.boxButtonTest2.setOnClickListener {
            binding.boxButtonTest2.isEnabled = false
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
        binding.boxButtonGradient.setOnClickListener {
            binding.boxButtonGradient.isEnabled = false
        }
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
}
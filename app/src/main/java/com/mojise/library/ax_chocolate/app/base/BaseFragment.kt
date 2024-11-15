package com.mojise.library.ax_chocolate.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.transition.MaterialSharedAxis
import com.mojise.library.ax_chocolate.app.main.MainActivityKotlin

abstract class BaseFragment<T : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : Fragment() {

    protected lateinit var binding: T
        private set

    protected val mainActivity: MainActivityKotlin
        get() = requireActivity() as MainActivityKotlin

    protected val navController: NavController
        get() = mainActivity.navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).also { it.duration = 500L }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false).also { it.duration = 500L }

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).also { it.duration = 500L }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false).also { it.duration = 500L }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
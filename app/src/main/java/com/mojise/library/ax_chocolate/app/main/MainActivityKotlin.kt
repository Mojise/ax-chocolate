package com.mojise.library.ax_chocolate.app.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mojise.library.ax_chocolate.app.R
import com.mojise.library.ax_chocolate.app.base.BaseActivity
import com.mojise.library.ax_chocolate.app.databinding.ActivityMainBinding
import com.mojise.library.chocolate.modal.AxModalLoadingDialog2
import com.mojise.library.chocolate.modal.axModalLoadingDialog

class MainActivityKotlin : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val loadingDialog by axModalLoadingDialog()
    private val loadingDialog2 = AxModalLoadingDialog2(this)
    private val loadingDialog2Getter: AxModalLoadingDialog2
        get() = loadingDialog2

    val navHostFragment: NavHostFragment
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

    val navController: NavController
        get() = navHostFragment.navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()

        initView()
        initNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setFullScreen() {
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initView() {
        binding.title.text = javaClass.simpleName
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun initNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->

            binding.btnBack.isVisible = (destination.id != R.id.mainFragment)
            binding.title.text = destination.label

            when (destination.id) {
                R.id.mainFragment -> {

                }
                R.id.chocolateViewTestFragment -> {

                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivityKotlin"
    }
}
package com.example.myapplication.restaurantdata.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.myapplication.R
import com.example.myapplication.base.ui.addFragment
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.restaurantdata.view.fragment.MainFragment
import com.example.myapplication.restaurantdata.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private var binding: ActivityMainBinding?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        addObservers()
    }

    private fun addObservers() {
        addFragmentObserver()
        addProgressbarObserver()
    }

    private fun addFragmentObserver() {
        viewModel.replaceFragmentLiveData.observe(this) { fragmentTag ->
            when (fragmentTag) {
                MainFragment.TAG -> {
                    addFragment(MainFragment.newInstance(), R.id.content_frame)
                }
            }
        }
    }

    private fun addProgressbarObserver() {
        viewModel.showProgressLiveData.observe(this) { showProgressBar ->
            when {
                showProgressBar -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                else -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }
    }

}
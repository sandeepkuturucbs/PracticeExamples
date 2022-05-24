package com.example.myapplication.restaurantdata.view.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.base.network.Status
import com.example.myapplication.base.ui.displayToastMessage
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.restaurantdata.model.RestaurantDTO
import com.example.myapplication.restaurantdata.util.Constants
import com.example.myapplication.restaurantdata.view.adapter.RestaurantDiffListAdapter
import com.example.myapplication.restaurantdata.viewmodel.MainViewModel


class MainFragment : Fragment(R.layout.fragment_main), RestaurantDiffListAdapter.OnListItemClickListener {

    companion object {
        const val TAG: String = "MainFragment"
        fun newInstance() = MainFragment()
    }

    private val viewModel by activityViewModels<MainViewModel>()
    private var binding : FragmentMainBinding? = null

    private val dataDiffAdapter: RestaurantDiffListAdapter by lazy {
        RestaurantDiffListAdapter(this@MainFragment)
    }

    // region Override Methods
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        initViews()
        addObservers()
        loadData()
    }

    override fun onListItemClicked(restaurantDTO: RestaurantDTO) {
        requireActivity().displayToastMessage(restaurantDTO.name)
    }
    // endregion Override Methods

    // region Private Methods
    private fun initViews() {
        binding?.restaurantList?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = dataDiffAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                setDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.sea_blue_light))) })
        }

        binding?.swipeToRefresh?.setOnRefreshListener {
            Log.d(TAG, "Swiped up to Refresh")
            loadData()
        }
    }

    private fun addObservers() {
        viewModel.restaurantLData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    viewModel.setShowProgress(false)
                    dataDiffAdapter.submitList(it.data?.filterNotNull())
                }
                Status.LOADING -> {
                    viewModel.setShowProgress(true)
                }
                Status.ERROR -> {
                    viewModel.setShowProgress(false)
                }
            }
            binding?.swipeToRefresh?.isRefreshing = false
        }
    }

    private fun loadData() {
        val lat = Constants.DEFAULT_LATITUDE
        val lng = Constants.DEFAULT_LONGITUDE
        viewModel.getRestaurantsList(lat, lng)
    }
    // endregion Private Methods
}
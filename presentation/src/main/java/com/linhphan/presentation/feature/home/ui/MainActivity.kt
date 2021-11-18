package com.linhphan.presentation.feature.home.ui

import androidx.recyclerview.widget.DividerItemDecoration
import com.linhphan.common.Logger
import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.presentation.BR
import com.linhphan.presentation.R
import com.linhphan.presentation.base.BaseActivity
import com.linhphan.presentation.databinding.ActivityMainBinding
import com.linhphan.presentation.extensions.gone
import com.linhphan.presentation.extensions.hideKeyboard
import com.linhphan.presentation.extensions.visible
import com.linhphan.presentation.feature.home.adapter.ForecastAdapter
import com.linhphan.presentation.feature.home.viewmodel.MainViewModel
import com.linhphan.presentation.model.ForecastModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    companion object{
        private const val tag = "MainActivity"
    }

    private var foreCastAdapter = ForecastAdapter()

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun getBindingVariableId(): Int {
        return BR.viewModel
    }

    override fun setupViews() {
        setupForecastRecycleView()
    }

    override fun setupObservers() {
        viewModel.onGetWeatherClickObservable.observe(this, {
            onGetWeatherButtonClicked()
        })
        viewModel.forecastsObservable.observe(this, {
            onGetForecastResult(it)
        })
    }

    private fun setupForecastRecycleView(){
        binding.recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = foreCastAdapter
        }
    }

    private fun onGetWeatherButtonClicked(){
        //todo should invalidate the query here.
        val q = binding.edtCity.text.toString().trim()
        getForecasts(q)
        hideKeyboard()
    }

    private fun getForecasts(cityName: String){
        viewModel.fetchForecasts(this, cityName)
    }

    private fun onGetForecastResult(resultWrapper: ResultWrapper<List<ForecastModel>>) {
        when(resultWrapper){
            is ResultWrapper.GenericError -> {
                onGetForecastError(resultWrapper.code, resultWrapper.message)
                binding.progressBar.gone()
            }
            ResultWrapper.InProgress -> {
                binding.progressBar.visible()
            }
            is ResultWrapper.Success -> {
                onGetForecastSuccess(resultWrapper.data)
                binding.progressBar.gone()
            }
        }
    }

    private fun onGetForecastSuccess(data: List<ForecastModel>) {
        foreCastAdapter.setData(data)
    }

    private fun onGetForecastError(code: Int, message: String) {
        foreCastAdapter.clear()
        //todo handle empty data
        //todo handle error here
    }

}
package com.linhphan.presentation.feature.home.viewmodel

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.linhphan.common.ApiResponseCode
import com.linhphan.common.Logger
import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.domain.usecase.IForecastUseCase
import com.linhphan.presentation.base.BaseViewModel
import com.linhphan.presentation.extensions.temporaryLockView
import com.linhphan.presentation.mapper.toWeatherInfoModel
import com.linhphan.presentation.model.ForecastModel
import com.linhphan.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val forecastUseCase: IForecastUseCase,
) : BaseViewModel(), TextView.OnEditorActionListener {

    companion object{
        private const val tag = "MainViewModel"
    }

    private val _onGetWeatherClick = SingleLiveEvent<Nothing>()
    var onGetWeatherClickObservable = _onGetWeatherClick as LiveData<Nothing>
    private val _forecasts = MutableLiveData<ResultWrapper<List<ForecastModel>>>()
    val forecastsObservable = _forecasts as LiveData<ResultWrapper<List<ForecastModel>>>

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (event == null || event.action != KeyEvent.ACTION_DOWN)
            return false
        _onGetWeatherClick.call()
        return false
    }

    fun onButtonClicked(v: View) {
        v.temporaryLockView()
        _onGetWeatherClick.call()
    }

    fun fetchForecasts(context: Context, cityName: String) {
        viewModelScope.launch {
            forecastUseCase.getForecast(cityName)
                .map { resultWrapper ->
                    when(resultWrapper){
                        is ResultWrapper.GenericError -> {
                            ResultWrapper.GenericError(resultWrapper.code, resultWrapper.message)
                        }
                        ResultWrapper.InProgress -> ResultWrapper.InProgress
                        is ResultWrapper.Success -> {
                            ResultWrapper.Success(resultWrapper.data.map { it.toWeatherInfoModel(context) })
                        }
                    }
                }
                .catch {
                    emit(ResultWrapper.GenericError(ApiResponseCode.ERROR_UNKNOWN, it.message.toString()))
                }
                .collect {
                    _forecasts.postValue(it)
                }
        }
    }
}
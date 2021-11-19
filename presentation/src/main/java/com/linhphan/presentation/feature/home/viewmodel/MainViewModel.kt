package com.linhphan.presentation.feature.home.viewmodel

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.linhphan.common.ApiResponseCode
import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.domain.usecase.IForecastUseCase
import com.linhphan.presentation.R
import com.linhphan.presentation.base.BaseViewModel
import com.linhphan.presentation.extensions.distinctUntilChanged
import com.linhphan.presentation.extensions.temporaryLockView
import com.linhphan.presentation.extensions.toast
import com.linhphan.presentation.mapper.toWeatherInfoModel
import com.linhphan.presentation.model.ForecastModel
import com.linhphan.presentation.util.SecuredSharePreference
import com.linhphan.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Intent
import com.linhphan.common.Logger
import com.linhphan.presentation.feature.home.ui.MainActivity


private const val MIN_QUERY_LENGTH = 3
private const val MIN_TEXT_SCALE_FACTOR = 50 //~50%
@HiltViewModel
class MainViewModel @Inject constructor(
    private val forecastUseCase: IForecastUseCase,
    private val preference: SecuredSharePreference
) : BaseViewModel(), TextView.OnEditorActionListener {

    companion object{
        private const val tag = "MainViewModel"
    }

    private val _onGetWeatherClick = SingleLiveEvent<Nothing>()
    var onGetWeatherClickObservable = _onGetWeatherClick as LiveData<Nothing>
    private val _forecasts = MutableLiveData<ResultWrapper<List<ForecastModel>>>()
    val forecastsObservable = _forecasts as LiveData<ResultWrapper<List<ForecastModel>>>

    private val _textScaleProgress = MutableLiveData(0)
    val textScaleProgressObservable = _textScaleProgress.distinctUntilChanged()
    private val _textScale = MutableLiveData(0)
    val textScaleObservable = _textScale.distinctUntilChanged()
    private val _textSize = MutableLiveData(24f)
    val textSizeObservable = _textSize.distinctUntilChanged()
    private var defaultTextSize = 0f
    private val _applyTextScaleButtonState = MutableLiveData(false)
    val applyTextScaleButtonState = _applyTextScaleButtonState as LiveData<Boolean>
    private val _onTextSizeChange = SingleLiveEvent<Nothing>()
    val onTextSizeChangeObservable = _onTextSizeChange as LiveData<Nothing>

    /**
     * to handle the the get-weather button's state
     * it's value is automatically updated
     * once the city-name input's value changed on the event [onTextChanged]
     */
    private val _buttonState = MutableLiveData(false)
    val buttonState = _buttonState.distinctUntilChanged()

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (v == null || event == null || event.action != KeyEvent.ACTION_DOWN)
            return false
        if (validateQuery(v.text.toString()).not()){
            v.toast(R.string.lp_message_error_min_length_require)
            return false
        }
        _onGetWeatherClick.call()
        return false
    }

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){
        _buttonState.value = validateQuery(s)
    }

    fun onButtonClicked(v: View) {
        v.temporaryLockView()
        _onGetWeatherClick.call()
    }

    /**
     * fetching forecasts from domain layer
     */
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

    /**
     * verify that a query is valid or not
     * @return true if the query's length is at least 3 letters
     * otherwise return false
     */
    private fun validateQuery(s: CharSequence): Boolean {
        return s.trim().length >= MIN_QUERY_LENGTH
    }

    fun resumeLastQuery(context: Context, query: String?){
        if (query == null) return
        if(validateQuery(query)){
            fetchForecasts(context, query)
            context.toast("resume last query | $query")
        }
    }

    //#region adjusting text size
    fun getLastTextScaleFactor(): Int{
        return preference.getTextScaleFactor()
    }

    fun setDefaultTextSize(context: Context){
        val fontScale = context.resources.configuration.fontScale
        val txt = TextView(context)
        val scaledFactorPer = (fontScale * 100).toInt()
        defaultTextSize = txt.textSize / fontScale
        _textSize.value = defaultTextSize * fontScale
        _textScale.value = scaledFactorPer
        _textScaleProgress.value = scaledFactorPer - MIN_TEXT_SCALE_FACTOR
    }

    fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean){
        if (progress == _textScaleProgress.value) return
        val context = seekBar?.context ?: return
        val expectedScale = progress * 0.01 + 0.5
        _applyTextScaleButtonState.value = true
        _textScale.value = progress + MIN_TEXT_SCALE_FACTOR
        _textScaleProgress.value = progress
        _textSize.value = (defaultTextSize * expectedScale).toFloat()
    }

    fun onApplyNewTextScale(){
        val factor = _textScale.value ?: 100
        preference.saveTextScaleFactor(factor)
        _onTextSizeChange.call()
        Logger.d(tag, "onApplyNewTextScale | factor = $factor")
    }
    //#endregion adjusting text size

    //#region navigation
    fun restartApplication(context: Context, query: String?){
        val intent = MainActivity.createIntent(context, query).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }
    //#endregion navigation
}
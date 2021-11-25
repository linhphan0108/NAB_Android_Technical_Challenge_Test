package com.linhphan.presentation.feature.home.ui

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.presentation.R
import com.linhphan.presentation.feature.home.viewmodel.MainViewModel
import com.linhphan.presentation.model.ForecastModel
import com.linhphan.presentation.util.SingleLiveEvent
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    @BindValue
//    @JvmField
    lateinit var viewModel: MainViewModel

    private lateinit var mainScenario: ActivityScenario<MainActivity>

    private val onGetWeatherClickObservable = SingleLiveEvent<Nothing>()
    private val forecastsObservable = MutableLiveData<ResultWrapper<List<ForecastModel>>>()
    private val onTextSizeChangeObservable = SingleLiveEvent<Nothing>()
    private val buttonState = MutableLiveData(false)

    @Before
    fun setUp() {
        Mockito.`when`(viewModel.onGetWeatherClickObservable)
            .thenReturn(onGetWeatherClickObservable)
        Mockito.`when`(viewModel.forecastsObservable)
            .thenReturn(forecastsObservable)
        Mockito.`when`(viewModel.onTextSizeChangeObservable)
            .thenReturn(onTextSizeChangeObservable)
        Mockito.`when`(viewModel.buttonState)
            .thenReturn(buttonState)

        hiltRule.inject()
        mainScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun check_getWeatherButton_state(){
        //given

        //when

        //then
        onView(withId(R.id.btn_query)).check(matches(not(isEnabled())))
        buttonState.postValue(true)
//        onView(withId(R.id.btn_query)).check(matches(isEnabled()))
    }
}
package com.linhphan.data.repository

import com.linhphan.data.BuildConfig
import com.linhphan.data.mapper.toWeatherInfo
import com.linhphan.data.remote.Services
import com.linhphan.domain.entity.ForecastEntity
import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.domain.repository.IForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ForecastRepository @Inject constructor(
    private val services: Services,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): BaseRepository(ioDispatcher), IForecastRepository {
    override suspend fun getForecast(
        cityName: String,
        count: Int
    ): Flow<ResultWrapper<List<ForecastEntity>>> {
        return safeApiCall(
            apiCall = {
                return@safeApiCall services.getForecast(
                    query = cityName,
                    count = count,
                    appId = BuildConfig.API_KEY
                )
            },
            mapper = { listForecastResponse ->
                listForecastResponse.forecasts.map {
                    it.toWeatherInfo()
                }
            }
        )
    }

    override suspend fun deleteForecast() {
    }
}
package com.linhphan.domain.repository

import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.domain.entity.ForecastEntity
import kotlinx.coroutines.flow.Flow

interface IForecastRepository {
    suspend fun getForecast(city: String): Flow<ResultWrapper<List<ForecastEntity>>>
    suspend fun deleteForecast()
}
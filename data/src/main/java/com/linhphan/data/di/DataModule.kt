package com.linhphan.data.di

import com.linhphan.data.remote.Services
import com.linhphan.data.repository.ForecastRepository
import com.linhphan.domain.repository.IForecastRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun provideForecastData(service: Services, ioDispatcher: CoroutineDispatcher): IForecastRepository{
        return ForecastRepository(service, ioDispatcher)
    }

}
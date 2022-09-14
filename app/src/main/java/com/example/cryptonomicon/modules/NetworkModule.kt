package com.example.cryptonomicon.modules

import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.datasource.CoinGeckoDatasource
import com.example.cryptonomicon.repository.NetworkRepository
import com.example.cryptonomicon.usecase.CoroutineUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideCoinGekoApi(): CoinGeckoApi {
        return CoinGeckoApi.create()
    }

    @Provides
    fun provideNetworkRepository(api: CoinGeckoApi): NetworkRepository {
        return CoinGeckoDatasource(api)
    }

}
package com.example.cryptonomicon.modules

import android.content.Context
import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.dao.AppDatabase
import com.example.cryptonomicon.datasource.CoinGeckoDatasource
import com.example.cryptonomicon.datasource.RoomDatasource
import com.example.cryptonomicon.repository.CryptoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideCoinGekoApi(): CoinGeckoApi {
        return CoinGeckoApi.create()
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideCoinGeckoDatasource(api: CoinGeckoApi) : CoinGeckoDatasource {
        return CoinGeckoDatasource(api)
    }

    @Provides
    fun provideNetworkRepository(networkDatasource: CoinGeckoDatasource, dbDatasource: RoomDatasource): CryptoRepository {
        return CryptoRepository(networkDatasource, dbDatasource)
    }

}
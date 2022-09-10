package com.example.cryptonomicon.datasource

import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.models.GeckoResponse
import com.example.cryptonomicon.repository.NetworkRepository
import retrofit2.Response
import javax.inject.Inject

class CoinGeckoDatasource @Inject constructor(var api: CoinGeckoApi): NetworkRepository {

    override suspend fun ping() = api.ping()

}
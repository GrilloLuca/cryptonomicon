package com.example.cryptonomicon.datasource

import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.models.GeckoResponse
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.repository.NetworkRepository
import retrofit2.Response
import javax.inject.Inject

class CoinGeckoDatasource @Inject constructor(var api: CoinGeckoApi): NetworkRepository {

    override suspend fun ping() = api.ping()

    /**
    * Obtain all the coins market data (price, market cap, volume)
    * */
    override suspend fun getTokens(
        currency: String,
        order: String,
        perPage: Int
    ): Response<List<Token>> {
        return api.getTokens(currency, order, perPage)
    }

}
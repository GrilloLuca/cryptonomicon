package com.example.cryptonomicon.datasource

import android.util.Log
import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.repository.NetworkRepository
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import javax.inject.Inject

class CoinGeckoDatasource @Inject constructor(var api: CoinGeckoApi) : NetworkRepository {

    companion object {
        private const val TAG = "CoinGeckoDatasource"
    }

    /**
     * Obtain all the coins market data (price, market cap, volume)
     * */
    override suspend fun getTokens(currency: String, order: String, perPage: Int): List<Token>? {

        val res = api.getTokens(currency, order, perPage)
        return if (res.isSuccessful) {
            res.body()
        } else {
            logError(res.errorBody())
            listOf()
        }
    }

    override suspend fun getTokenDetails(tokenId: String): TokenDetails? {

        val res = api.getTokenDetails(tokenId)
        return if (res.isSuccessful) {
            res.body()
        } else {
            logError(res.errorBody())
            null
        }

    }

    override suspend fun getMarketChart(
        tokenId: String,
        currency: String,
        from: String,
        to: String
    ): MarketData? {

        val res = api.getMarketChart(tokenId, currency, from, to)
        return if (res.isSuccessful) {
            res.body()
        } else {
            logError(res.errorBody())
            null
        }
    }

    private fun logError(error: ResponseBody?) {
        error?.string()?.let {
            Log.d(TAG, it)
        }
    }


}
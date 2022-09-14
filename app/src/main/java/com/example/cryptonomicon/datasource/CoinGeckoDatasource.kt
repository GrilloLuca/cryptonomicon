package com.example.cryptonomicon.datasource

import android.util.Log
import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.PingResponse
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.repository.NetworkRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class CoinGeckoDatasource @Inject constructor(var api: CoinGeckoApi) : NetworkRepository {

    companion object {
        private const val TAG = "CoinGeckoDatasource"
    }

    override fun ping(): Flow<PingResponse?> = flow {
        val res = api.ping()
        if(res.isSuccessful) {
            emit(res.body())
        }
    }

    /**
     * Obtain all the coins market data (price, market cap, volume)
     * */
    override fun getTokens(
        currency: String,
        order: String,
        perPage: Int
    ): Flow<List<Token>?> = flow {

        val res = api.getTokens(currency, order, perPage)
        if (res.isSuccessful) {
            emit(res.body())
        } else {
            logError(res.errorBody())
            emit(listOf())
        }
    }

    override fun getTokenDetails(tokenId: String) : Flow<TokenDetails?> = flow {

        val res = api.getTokenDetails(tokenId)
        if(res.isSuccessful) {
            emit(res.body())
        } else {
            logError(res.errorBody())
            emit(null)
        }

    }

    override fun getMarketChart(
        tokenId: String,
        currency: String,
        from: String,
        to: String
    ): Flow<MarketData?>  = flow {

        val res = api.getMarketChart(tokenId, currency, from, to)
        if(res.isSuccessful) {
            emit(res.body())
        } else {
            logError(res.errorBody())
            emit(null)
        }

    }


    private fun logError(error: ResponseBody?) {
        error?.string()?.let {
            Log.d(TAG, it)
        }
    }


}
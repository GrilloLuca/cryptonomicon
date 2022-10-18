package com.example.cryptonomicon.datasource

import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.repository.ApiContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoinGeckoDatasource @Inject constructor(var api: CoinGeckoApi) : ApiContract {

    companion object {
        private const val TAG = "CoinGeckoDatasource"
    }

    /**
     * Obtain all the coins market data (price, market cap, volume)
     * */
    override suspend fun getTokens(
        currency: String,
        order: String,
        perPage: Int
    ): Flow<Resource<List<Token>>>  = flow {

        val res = api.getTokens(currency, order, perPage)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()))
        } else {
            emit(Resource.Error(res.errorBody()?.source().toString(), listOf()))
        }

    }

    override fun getTokenDetails(tokenId: String): Flow<Resource<TokenDetails>> = flow {

        val res = api.getTokenDetails(tokenId)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()))
        } else {
            emit(Resource.Error(res.errorBody()?.source().toString(), null))
        }

    }

    override suspend fun getMarketChart(
        tokenId: String,
        currency: String,
        from: String,
        to: String
    ): Resource<MarketData> {

        return try {
            val res = api.getMarketChart(tokenId, currency, from, to)
            return if (res.isSuccessful) {
                Resource.Success(res.body())
            } else {
                Resource.Error(res.errorBody()?.source().toString(), null)
            }
        } catch (throwable: Throwable) {
            Resource.Error(throwable.localizedMessage, null)
        }
    }

}
package com.example.cryptonomicon.datasource

import android.util.Log
import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.repository.ApiContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
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
    ): Resource<List<Token>> {

        return try {
            val res = api.getTokens(currency, order, perPage)
            return if (res.isSuccessful) {
                Resource.Success(res.body())
            } else {
                Resource.Error(res.errorBody()?.source().toString(), listOf())
            }
        } catch (throwable: Throwable) {
            errorHandler(throwable, listOf())
        }

    }

    override fun getTokenDetails(tokenId: String): Flow<Resource<TokenDetails>> = flow {

            val res = api.getTokenDetails(tokenId)
            if (res.isSuccessful) {
                emit(Resource.Success(res.body()))
            } else {
                emit(Resource.Error(res.errorBody()?.source().toString(), null))
            }

//            errorHandler(throwable, TokenDetails("0", Description("")))

    }

    override suspend fun saveTokenDetails(tokenId: String, details: TokenDetails) {

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
            errorHandler(throwable, MarketData(null))
        }
    }

    private fun <T> errorHandler(throwable: Throwable, output: T): Resource<T> {
        val errorMessage = when (throwable) {
            is IOException -> throwable.localizedMessage
            is HttpException -> throwable.response()?.errorBody()?.source().toString()
            else -> "generic error"
        }

        return Resource.Error(errorMessage, output)
    }

}
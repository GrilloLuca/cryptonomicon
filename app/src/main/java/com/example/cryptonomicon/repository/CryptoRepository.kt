package com.example.cryptonomicon.repository

import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.datasource.CoinGeckoDatasource
import com.example.cryptonomicon.datasource.RoomDatasource
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CryptoRepository @Inject constructor(
    var networkDatasource: CoinGeckoDatasource,
    var dbDatasource: RoomDatasource
    ) {

    fun getTokens(
        currency: String,
        order: String,
        perPage: Int
    ): Flow<Resource<List<Token>>> = flow {

        dbDatasource.getTokens(currency, order, perPage)
            .flowOn(Dispatchers.IO)
            .catch { e ->
                errorHandler(e, null)
            }
            .collect {
                emit(it)
            }

        networkDatasource.getTokens(currency, order, perPage)
            .flowOn(Dispatchers.IO)
            .catch { e ->
                errorHandler(e, listOf<Token>())
            }
            .collect {
                if(it is Resource.Success) {
                    it.data?.let { tokenList ->
                        dbDatasource.saveTokens(tokenList)
                    }
                }
                emit(it)
            }

    }

    fun getTokenDetails(tokenId: String): Flow<Resource<TokenDetails>> = flow {

        dbDatasource.getTokenDetails(tokenId)
            .flowOn(Dispatchers.IO)
            .catch { e ->
                errorHandler(e, listOf<Token>())
            }
            .collect {
                emit(it)
            }

        networkDatasource.getTokenDetails(tokenId)
            .flowOn(Dispatchers.IO)
            .catch { e ->
                errorHandler(e, listOf<Token>())
            }
            .collect {
                if(it is Resource.Success) {
                    it.data?.let { details ->
                        dbDatasource.saveTokenDetails(tokenId, details)
                    }
                }
            emit(it)
        }

    }

    suspend fun getMarketChart(
        tokenId: String,
        currency: String,
        from: String,
        to: String
    ): Resource<MarketData> {

        return networkDatasource.getMarketChart(tokenId, currency, from, to)
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
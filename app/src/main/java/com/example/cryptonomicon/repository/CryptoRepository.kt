package com.example.cryptonomicon.repository

import android.util.Log
import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.datasource.CoinGeckoDatasource
import com.example.cryptonomicon.datasource.RoomDatasource
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CryptoRepository @Inject constructor(
    var networkDatasource: CoinGeckoDatasource,
    var dbDatasource: RoomDatasource
    ) {

    suspend fun getTokens(
        currency: String,
        order: String,
        perPage: Int
    ): Resource<List<Token>> {

        return networkDatasource.getTokens(currency, order, perPage)

    }

    fun getTokenDetails(tokenId: String): Flow<Resource<TokenDetails>> = flow {

        dbDatasource.getTokenDetails(tokenId)
            .flowOn(Dispatchers.IO)
            .collect {
                emit(it)
            }

        networkDatasource.getTokenDetails(tokenId)
            .flowOn(Dispatchers.IO)
            .collect {
            if(it is Resource.Success) {
                it.data?.let { details ->
                    dbDatasource.saveTokenDetails(tokenId, details)
                }
            }
            emit(it)
        }

    }

    suspend fun saveTokenDetails(tokenId: String, details: TokenDetails) {
        TODO("Not yet implemented")
    }

    suspend fun getMarketChart(
        tokenId: String,
        currency: String,
        from: String,
        to: String
    ): Resource<MarketData> {

        return networkDatasource.getMarketChart(tokenId, currency, from, to)
    }


}
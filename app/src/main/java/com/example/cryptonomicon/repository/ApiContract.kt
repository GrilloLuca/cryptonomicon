package com.example.cryptonomicon.repository

import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import kotlinx.coroutines.flow.Flow

interface ApiContract {

    suspend fun getTokens(currency: String, order: String, perPage: Int): Resource<List<Token>>

    fun getTokenDetails(tokenId: String): Flow<Resource<TokenDetails>>
    suspend fun saveTokenDetails(tokenId: String, details: TokenDetails)

    suspend fun getMarketChart(
        tokenId: String,
        currency: String,
        from: String,
        to: String
    ): Resource<MarketData>

}
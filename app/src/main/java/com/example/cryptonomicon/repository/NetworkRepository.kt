package com.example.cryptonomicon.repository

import com.example.cryptonomicon.models.GeckoResponse
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import retrofit2.Response

interface NetworkRepository {

    suspend fun ping(): Response<GeckoResponse>
    suspend fun getTokens(currency: String, order: String, perPage: Int): Response<List<Token>>
    suspend fun getTokenDetails(tokenId: String): Response<TokenDetails>
    suspend fun getMarketChart(
        tokenId: String,
        currency: String,
        from: String,
        to: String
    ): Response<MarketData>

}
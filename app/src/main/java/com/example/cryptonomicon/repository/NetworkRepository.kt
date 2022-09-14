package com.example.cryptonomicon.repository

import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails

interface NetworkRepository {

    suspend fun getTokens(currency: String, order: String, perPage: Int): List<Token>?
    suspend fun getTokenDetails(tokenId: String): TokenDetails?
    suspend fun getMarketChart(
        tokenId: String,
        currency: String,
        from: String,
        to: String
    ): MarketData?

}
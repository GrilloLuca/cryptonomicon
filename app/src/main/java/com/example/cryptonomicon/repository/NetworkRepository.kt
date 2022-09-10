package com.example.cryptonomicon.repository

import com.example.cryptonomicon.models.GeckoResponse
import com.example.cryptonomicon.models.Token
import retrofit2.Response

interface NetworkRepository {

    suspend fun ping(): Response<GeckoResponse>
    suspend fun getTokens(currency: String, order: String, perPage: Int): Response<List<Token>>

}
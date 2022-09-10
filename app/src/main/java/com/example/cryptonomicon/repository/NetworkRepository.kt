package com.example.cryptonomicon.repository

import com.example.cryptonomicon.models.GeckoResponse
import retrofit2.Response

interface NetworkRepository {

    suspend fun ping(): Response<GeckoResponse>

}
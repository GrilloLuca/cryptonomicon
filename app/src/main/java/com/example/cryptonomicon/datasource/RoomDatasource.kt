package com.example.cryptonomicon.datasource

import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.dao.AppDatabase
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.repository.ApiContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RoomDatasource @Inject constructor(var db: AppDatabase) : ApiContract {
    override suspend fun getTokens(
        currency: String,
        order: String,
        perPage: Int
    ): Resource<List<Token>> {
        TODO("Not yet implemented")
    }

    override fun getTokenDetails(tokenId: String): Flow<Resource<TokenDetails>> = flow {


        val details = db.tokenDetailsDao().findById(tokenId)
        emit(Resource.Success(details))

    }

    override suspend fun saveTokenDetails(tokenId: String, details: TokenDetails) {
        withContext(Dispatchers.IO) {
            db.tokenDetailsDao().insertAll(details)
        }
    }

    override suspend fun getMarketChart(
        tokenId: String,
        currency: String,
        from: String,
        to: String
    ): Resource<MarketData> {
        TODO("Not yet implemented")
    }
}
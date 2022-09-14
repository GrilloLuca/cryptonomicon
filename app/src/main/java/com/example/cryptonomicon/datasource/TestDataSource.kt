package com.example.cryptonomicon.datasource

import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.PingResponse
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TestDataSource @Inject constructor(var api: CoinGeckoApi) : NetworkRepository {
    override fun ping(): Flow<PingResponse?> = flow {

    }

    override fun getTokens(currency: String, order: String, perPage: Int): Flow<List<Token>?> =
        flow {
            val fakeTokens = listOf(
                Token(
                    id = "1",
                    name = "GrilloToken",
                    image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579"
                ),
                Token(id = "2",
                    name = "Testcoin",
                    current_price = 0.02f,
                    image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579"
                ),
                Token(id = "3",
                    name = "Fuck coin",
                    symbol = "FCK",
                    current_price = 0.000005f,
                    image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579"),
            )
            emit(fakeTokens)
        }

    override fun getTokenDetails(tokenId: String): Flow<TokenDetails?> = flow {

    }

    override fun getMarketChart(
        tokenId: String,
        currency: String,
        from: String,
        to: String
    ): Flow<MarketData?> = flow {

    }
}
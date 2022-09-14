package com.example.cryptonomicon.datasource

import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.repository.NetworkRepository
import javax.inject.Inject

class TestDataSource @Inject constructor(var api: CoinGeckoApi) : NetworkRepository {

    override suspend fun getTokens(currency: String, order: String, perPage: Int) =
        Resource.Success(
            listOf(
                Token(
                    id = "1",
                    name = "GrilloToken",
                    image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579"
                ),
                Token(
                    id = "2",
                    name = "Testcoin",
                    current_price = 0.02f,
                    image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579"
                ),
                Token(
                    id = "3",
                    name = "Fuck coin",
                    symbol = "FCK",
                    current_price = 0.000005f,
                    image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579"
                ),
            )
        )

    override suspend fun getTokenDetails(tokenId: String): Resource<TokenDetails> =
        Resource.Error("getTokenDetails: ERROR FETCHING DATA", null)


    override suspend fun getMarketChart(
        tokenId: String,
        currency: String,
        from: String,
        to: String
    ): Resource<MarketData> =
        Resource.Error("Error loading market cap data", null)
}


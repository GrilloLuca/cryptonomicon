package com.example.cryptonomicon

import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.PingResponse
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp
import javax.inject.Inject

class CryptoUseCases @Inject constructor(var repo: NetworkRepository) {

    companion object {

        private const val TAG = "CryptoUseCase"

        private const val CURRENCY_EUR = "EUR"
        private const val CURRENCY_USD = "USD"

        private const val MARKET_CAP_DESC = "market_cap_desc"
        private const val MARKET_CAP_ASC = "market_cap_asc"

    }

    fun ping(): Flow<PingResponse?> = repo.ping()

    /**
     * retrieve top ten tokens sorted bt market cap
     */
    fun getTokens(): Flow<List<Token>?> {

        return repo.getTokens(
            CURRENCY_EUR,
            MARKET_CAP_DESC,
            10
        )

    }

    /**
     * retrieve the token details
     */
    fun getTokenDetails(tokenId: String) = repo.getTokenDetails(tokenId)

    /**
     * retrieve the market chart data, in a range of dates
     */
    fun getWeeklyMarketChart(tokenId: String): Flow<MarketData?> {

        val week = 7 * 60 * 60 * 24 * 1000
        val now = System.currentTimeMillis()
        val to = Timestamp(now).time / 1000
        val from = Timestamp(now - week).time / 1000

        return repo.getMarketChart(
            tokenId,
            CURRENCY_EUR,
            "$from",
            "$to"
        )

    }

}
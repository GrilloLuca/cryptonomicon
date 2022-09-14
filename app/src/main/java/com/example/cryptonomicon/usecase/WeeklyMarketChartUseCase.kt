package com.example.cryptonomicon.usecase

import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.repository.NetworkRepository
import java.sql.Timestamp
import javax.inject.Inject

class WeeklyMarketChartUseCase @Inject constructor(private var repo: NetworkRepository) :
    CoroutineUseCase<WeeklyMarketChartInput, MarketData?>() {
    override suspend fun execute(input: WeeklyMarketChartInput): MarketData? {

        val week = 7 * 60 * 60 * 24 * 1000
        val now = System.currentTimeMillis()
        val to = Timestamp(now).time / 1000
        val from = Timestamp(now - week).time / 1000

        return repo.getMarketChart(
            input.tokenId,
            input.currency,
            from.toString(),
            to.toString()
        )
    }
}

data class WeeklyMarketChartInput(
    var tokenId: String,
    var currency: String
)
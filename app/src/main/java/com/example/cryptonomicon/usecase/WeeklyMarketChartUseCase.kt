package com.example.cryptonomicon.usecase

import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.sql.Timestamp
import javax.inject.Inject

class WeeklyMarketChartUseCase @Inject constructor(private var repo: CryptoRepository) :
    CoroutineUseCase<WeeklyMarketChartInput, Resource<MarketData>>() {

    override fun execute(input: WeeklyMarketChartInput): Flow<Resource<MarketData>> = flow {

        val week = 7 * 60 * 60 * 24 * 1000
        val now = System.currentTimeMillis()
        val to = Timestamp(now).time / 1000
        val from = Timestamp(now - week).time / 1000

        val marketData =repo.getMarketChart(
            input.tokenId,
            input.currency,
            from.toString(),
            to.toString()
        )
        emit(marketData)
    }
}

data class WeeklyMarketChartInput(
    var tokenId: String,
    var currency: String
)
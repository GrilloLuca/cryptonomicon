package com.example.cryptonomicon.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var datasource: NetworkRepository) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"

        private const val CURRENCY_EUR = "EUR"
        private const val CURRENCY_USD = "USD"

        private const val MARKET_CAP_DESC = "market_cap_desc"
        private const val MARKET_CAP_ASC = "market_cap_asc"
    }

    var tokenList = MutableLiveData<List<Token>>()
    var tokenDetails = MutableLiveData<TokenDetails>()
    var marketChart = MutableLiveData<MarketData>()

    /**
     * Ping method of CoinGecko api
     * dispatchers should be injected in the viewModel as well
     */
    fun ping() = CoroutineScope(Dispatchers.IO).launch {
        datasource.ping()
    }

    /**
     * retrieve top ten tokens sorted bt market cap
     */
    fun getTokens() =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = datasource.getTokens(
                    CURRENCY_EUR,
                    MARKET_CAP_DESC,
                    10
                )
                if (res.isSuccessful) {
                    tokenList.postValue(res.body())
                } else {
                    res.errorBody()?.string()?.let { Log.d(TAG, it) }
                }
            } catch (e: Throwable) {
                tokenList.postValue(listOf())
            }
        }


    /**
     * retrieve the token details
     */
    fun getTokenDetails(tokenId: String) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = datasource.getTokenDetails(tokenId)
                if (res.isSuccessful) {
                    tokenDetails.postValue(res.body())
                } else {
                    res.errorBody()?.string()?.let { Log.d(TAG, it) }
                }
            } catch (e: Throwable) {
                tokenDetails.postValue(null)
            }
        }

    /**
     * retrieve the market chart data, in a range of dates
     */
    fun getWeeklyMarketChart(tokenId: String) =
        CoroutineScope(Dispatchers.IO).launch {

            val to = LocalDate.now()
            val from = to.minusDays(7)

            try {
                val res = datasource.getMarketChart(
                    tokenId,
                    CURRENCY_EUR,
                    from.toUnix(),
                    to.toUnix()
                )
                if (res.isSuccessful) {
                    marketChart.postValue(res.body())
                } else {
                    res.errorBody()?.string()?.let { Log.d(TAG, it) }
                }
            } catch (e: Throwable) {
                marketChart.postValue(null)
            }
        }


}

fun LocalDate.toUnix() = run {
    this.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond.toString()
}
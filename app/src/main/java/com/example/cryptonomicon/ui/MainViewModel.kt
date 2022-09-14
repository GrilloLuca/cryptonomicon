package com.example.cryptonomicon.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var tokenListUseCase: GetTokenListUseCase,
    private var tokenDetailsUseCase: TokenDetailsUseCase,
    private var weeklyMarketChartUseCase: WeeklyMarketChartUseCase

) : ViewModel() {

    var tokenList = MutableLiveData<List<Token>>()
    var tokenDetails = MutableLiveData<TokenDetails>()
    var marketChart = MutableLiveData<MarketData>()

    /**
     * execute useCases and update the livedata
     * parameters should be injected for better testability
     */
    fun getTokens() {
        viewModelScope.launch {
            val res = tokenListUseCase.execute(
                TokenListInput(CURRENCY_EUR, MARKET_CAP_DESC, 10)
            )
            tokenList.postValue(res)
        }
    }

    fun getTokenDetails(tokenId: String) {
        viewModelScope.launch {
            val res = tokenDetailsUseCase.execute(tokenId)
            tokenDetails.postValue(res)
        }
    }

    fun getWeeklyMarketChart(tokenId: String) {
        viewModelScope.launch {
            val res = weeklyMarketChartUseCase.execute(
                WeeklyMarketChartInput(
                    tokenId = tokenId,
                    currency = CURRENCY_EUR
                ))

            marketChart.postValue(res)
        }
    }

    companion object {

        private const val TAG = "CryptoUseCase"

        private const val CURRENCY_EUR = "eur"
        private const val CURRENCY_USD = "usd"

        private const val MARKET_CAP_DESC = "market_cap_desc"
        private const val MARKET_CAP_ASC = "market_cap_asc"

    }

}
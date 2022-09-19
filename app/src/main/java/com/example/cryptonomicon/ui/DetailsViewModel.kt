package com.example.cryptonomicon.ui

import androidx.lifecycle.*
import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    state: SavedStateHandle,
    private var tokenDetailsUseCase: TokenDetailsUseCase,
    private var weeklyMarketChartUseCase: WeeklyMarketChartUseCase

) : ViewModel() {

    var tokenDetails = MutableLiveData<TokenDetails>()
    var marketChart = MutableLiveData<MarketData>()

    var error = MutableLiveData<String>()

    init {
        // request tokens details and market data by ViewModel
        // and observe result in the composable
        state.get<String>("tokenId")?.let {
            getTokenDetails(it)
            getWeeklyMarketChart(it)
        }
    }

    private fun getTokenDetails(tokenId: String) {
        viewModelScope.launch {
            tokenDetailsUseCase.execute(tokenId).collect { res ->
                when (res) {
                    is Resource.Success -> tokenDetails.postValue(res.data)
                    is Resource.Error -> {
                        error.postValue(res.text)
                        res.data?.let {
                            tokenDetails.postValue(it)
                        }
                    }
                }
            }
        }
    }

    private fun getWeeklyMarketChart(tokenId: String) {
        viewModelScope.launch {
            weeklyMarketChartUseCase.execute(
                WeeklyMarketChartInput(
                    tokenId = tokenId,
                    currency = CURRENCY_EUR
                )
            ).collect { res ->
                when(res) {
                    is Resource.Success -> marketChart.postValue(res.data)
                    is Resource.Error -> {
                        error.postValue(res.text)
                        res.data?.let {
                            marketChart.postValue(res.data)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val CURRENCY_EUR = "eur"
        private const val CURRENCY_USD = "usd"
    }

}
package com.example.cryptonomicon.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptonomicon.CryptoUseCases
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var useCase: CryptoUseCases) : ViewModel() {

    var tokenList = MutableLiveData<List<Token>>()
    var tokenDetails = MutableLiveData<TokenDetails>()
    var marketChart = MutableLiveData<MarketData>()

    /**
     * Ping method of CoinGecko api
     * dispatchers should be injected in the viewModel as well
     */
    fun ping() = CoroutineScope(Dispatchers.IO).launch {
        useCase.ping()
    }

    fun getTokens() {
        viewModelScope.launch {
            useCase.getTokens()
                .catch {
                    tokenList.postValue(listOf())
                }.collect {
                    tokenList.postValue(it)
                }
        }
    }

    fun getTokenDetails(tokenId: String) {
        viewModelScope.launch {
            useCase.getTokenDetails(tokenId).catch {
                tokenDetails.postValue(null)
            }.collect {
                tokenDetails.postValue(it)
            }
        }
    }

    fun getWeeklyMarketChart(tokenId: String) {
        viewModelScope.launch {
            useCase.getWeeklyMarketChart(tokenId).catch {
                marketChart.postValue(null)
            }.collect {
                marketChart.postValue(it)
            }
        }
    }


}
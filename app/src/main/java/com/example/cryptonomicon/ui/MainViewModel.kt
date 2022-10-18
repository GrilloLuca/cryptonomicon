package com.example.cryptonomicon.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MainViewModel @Inject constructor(
    private var tokenListUseCase: GetTokenListUseCase,
    ) : ViewModel() {

    var tokenList = MutableLiveData<List<Token>>()

    init {
        getTokens()
    }
    /**
     * execute useCases and update the livedata
     * parameters should be injected for better testability
     */
    fun getTokens() {
        viewModelScope.launch {
            tokenListUseCase.execute(
                TokenListInput(CURRENCY_EUR, MARKET_CAP_DESC, 10)
            ).collect { res ->
                when (res) {
                    is Resource.Success -> tokenList.postValue(res.data)
                    is Resource.Error -> { }
                }
            }

        }
    }

    companion object {

        private const val CURRENCY_EUR = "eur"
        private const val CURRENCY_USD = "usd"

        private const val MARKET_CAP_DESC = "market_cap_desc"
        private const val MARKET_CAP_ASC = "market_cap_asc"

    }

}
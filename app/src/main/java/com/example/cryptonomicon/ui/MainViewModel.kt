package com.example.cryptonomicon.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var datasource: NetworkRepository) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"

        private const val MARKET_CAP_DESC = "market_cap_desc"
        private const val MARKET_CAP_ASC = "market_cap_asc"

    }

    var tokenList = MutableLiveData<List<Token>>()

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
    fun getTokens() = CoroutineScope(Dispatchers.IO).launch {

        try {
            val res = datasource.getTokens("EUR", MARKET_CAP_DESC, 10)
            if (res.isSuccessful) {
                tokenList.postValue(res.body())
            } else {
                res.errorBody()?.string()?.let { Log.d(TAG, it) }
            }
        } catch (e: Throwable) {
            tokenList.postValue(listOf())
        }

    }

}
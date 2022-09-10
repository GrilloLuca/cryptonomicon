package com.example.cryptonomicon.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptonomicon.models.GeckoResponse
import com.example.cryptonomicon.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var datasource: NetworkRepository): ViewModel() {

    companion object {
        private const val TAG ="MainViewModel"
    }

    var response = MutableLiveData<GeckoResponse>()

    fun ping() = kotlin.runCatching {

        // dispatchers should be injected in the viewModel as well
        CoroutineScope(Dispatchers.IO).launch {
            val res = datasource.ping()
            if(res.isSuccessful) {
                response.postValue(res.body())
            } else {
                res.errorBody()?.string()?.let { Log.d(TAG, it) }
            }
        }

    }
}
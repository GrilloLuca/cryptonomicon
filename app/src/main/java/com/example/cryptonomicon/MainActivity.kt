package com.example.cryptonomicon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.ui.MainViewModel
import com.example.cryptonomicon.ui.compose.Loader
import com.example.cryptonomicon.ui.compose.TokensContent
import com.example.cryptonomicon.ui.theme.CryptonomiconTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {

        private const val CURRENCY_EUR = "EUR"
        private const val CURRENCY_USD = "USD"

        private const val MARKET_CAP_DESC = "market_cap_desc"
        private const val MARKET_CAP_ASC = "market_cap_asc"

    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.tokenList.observe(this) { tokens ->
            updateContent(tokens)
        }
        viewModel.getTokens(CURRENCY_EUR, MARKET_CAP_DESC)

        updateContent(null)

    }

    private fun updateContent(tokens: List<Token>?) {

        setContent {
            CryptonomiconTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    tokens?.let {
                        TokensContent(it)
                    } ?: Loader()
                }
            }
        }
    }
}

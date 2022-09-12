package com.example.cryptonomicon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.ui.MainViewModel
import com.example.cryptonomicon.ui.theme.CryptonomiconTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TokenDetailsActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenId = intent.extras?.get(MainActivity.EXTRA_SELECTED_TOKEN) as? String
        tokenId?.let {
            viewModel.getTokenDetails(it)
            viewModel.getWeeklyMarketChart(it)
        }

        setContent {
            CryptonomiconTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        TokenDetailsContent()
                        MarketChartContent()
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun TokenDetailsContent(viewModel: MainViewModel = viewModel()) {

        val details = viewModel.tokenDetails.observeAsState()
        details.value?.let {
            DetailCard(it)
        } ?: Loader()

    }

    @Preview
    @Composable
    fun MarketChartContent(viewModel: MainViewModel = viewModel()) {

        val data = viewModel.marketChart.observeAsState()
        data.value?.let {
            MarketChart(it)
        } ?: Loader()

    }

    @Composable
    fun DetailCard(token: TokenDetails) {

        Column {
            DetailHeader(token)
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                elevation = 2.dp,
                shape = RoundedCornerShape(corner = CornerSize(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = HtmlCompat.fromHtml(
                            token.description.en,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        ).toString(),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }

    }

    @Composable
    fun DetailHeader(token: TokenDetails) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TokenImage(token.image.small)
            Text(token.links.homepage[0])
        }
    }

    @Composable
    fun MarketChart(data: MarketData) {

        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .height(300.dp),
            elevation = 2.dp,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            MarketPriceList(data.prices)
        }
    }

    @Composable
    fun MarketPriceList(prices: List<List<Double>>) {
        LazyColumn {
            items(prices) { price ->
                MarketPrice(price)
            }
        }
    }

    @Composable
    fun MarketPrice(price: List<Double>) {
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {

            val df = Date(price[0].toLong())
            val vv: String = SimpleDateFormat("MM dd, yyyy hh:mma", Locale.ITALIAN).format(df)

            Text(
                text = "date: $vv",
                style = MaterialTheme.typography.h6
            )
            Text(
                text = "price: ${price[1]}",
                style = MaterialTheme.typography.caption
            )
        }
    }

}

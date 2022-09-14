package com.example.cryptonomicon.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.ui.MainViewModel
import java.text.SimpleDateFormat
import java.util.*


/**
 * TokenDetailsScreen shows detailed asset info with website url, description and market price
 */
@Composable
fun TokenDetailsScreen(navController: NavController, viewModel: MainViewModel) {
    val details = viewModel.tokenDetails.observeAsState()
    val chart = viewModel.marketChart.observeAsState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            elevation = 2.dp,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Column {
                details.value?.let {
                    DetailHeader(it)
                    DetailCard(it)
                } ?: Loader()
            }
        }
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            elevation = 2.dp,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            chart.value?.let {
                MarketPriceList(it.prices)
            } ?: Loader()
        }
    }
}
@Composable
fun DetailHeader(token: TokenDetails) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TokenImage(token.image?.small ?: "")
        Text(token.links?.homepage?.firstOrNull() ?: "")
    }
}

@Composable
fun DetailCard(token: TokenDetails) {
    Text(
        text = HtmlCompat.fromHtml(
            token.description.en,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString(),
        style = MaterialTheme.typography.caption,
        modifier = Modifier
            .padding(16.dp)
    )
}

@Composable
fun MarketPriceItem(price: List<Double>) {
    Column(
        modifier = Modifier
            .padding(4.dp),
    ) {

        val df = Date(price[0].toLong())
        val vv: String = SimpleDateFormat("dd/MM/yyyy, h:mm a", Locale.ITALIAN).format(df)

        Text(
            text = "date: $vv",
            style = MaterialTheme.typography.body1
        )
        Text(
            text = "price: ${price[1]}",
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MarketPriceList(prices: List<List<Double>>) {
    LazyColumn(
        modifier = Modifier
            .height(400.dp)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        items(prices) { price ->
            MarketPriceItem(price)
        }
    }
}
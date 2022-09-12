package com.example.cryptonomicon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.example.cryptonomicon.models.MarketData
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.ui.MainViewModel
import com.example.cryptonomicon.ui.compose.preview.TokenProvider
import com.example.cryptonomicon.ui.theme.CryptonomiconTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CryptonomiconTheme {

                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    NavHost(navController = navController, startDestination = "main") {
                        composable(route = "main") {
                            val viewModel = hiltViewModel<MainViewModel>()
                            viewModel.getTokens()
                            TokensScreen(
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                        composable(
                            route = "details/{tokenId}",
                            arguments = listOf(navArgument("tokenId") {
                                type = NavType.StringType
                            })
                        ) {
                            val viewModel = hiltViewModel<MainViewModel>()
                            val tokenId = it.arguments?.getString("tokenId")!!
                            viewModel.getTokenDetails(tokenId)
                            viewModel.getWeeklyMarketChart(tokenId)
                            TokenDetailsScreen(
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }

    }
}

// region composable

@Composable
fun TokensScreen(navController: NavController, viewModel: MainViewModel) {
    val tokens = viewModel.tokenList.observeAsState()

    tokens.value?.let {
        if (it.isEmpty()) {
            EmptyList()
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            it.forEach { token ->
                TokenListItem(navController, token)
            }
        }
    } ?: Loader()

}

@Composable
fun TokenDetailsScreen(navController: NavController, viewModel: MainViewModel) {
    val details = viewModel.tokenDetails.observeAsState()
    val chart = viewModel.marketChart.observeAsState()

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())) {

        details.value?.let {
            DetailCard(it)
        } ?: Loader()

        chart.value?.let {
            MarketChart(it)
        } ?: Loader()

    }
}

@Preview
@Composable
fun Loader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun EmptyList() {
    val context = LocalContext.current
    Row {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(context.resources.getString(R.string.txt_error_connection))
        }
    }
}

@Preview
@Composable
fun TokenImage(
    @PreviewParameter(TokenProvider::class, limit = 1) tokenImage: String?
) {
    Image(
        painter = rememberAsyncImagePainter(tokenImage),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(8.dp)
            .size(32.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
    )
}

@Composable
fun TokenListItem(
    navController: NavController,
    token: Token
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row(modifier = Modifier
            .clickable {
                navController.navigate("details/${token.id}")
            }
        ) {
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
            ) {
                TokenImage(token.image)
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "${token.name} (${token.symbol})",
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = "${token.current_price}€",
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

@Composable
fun MarketPriceList(prices: List<List<Double>>) {
    LazyColumn {
        items(prices) { price ->
            MarketPrice(price)
        }
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

// endregion

package com.example.cryptonomicon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.ui.NavigationUI
import coil.compose.rememberAsyncImagePainter
import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.datasource.CoinGeckoDatasource
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.repository.NetworkRepository
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
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text(stringResource(id = R.string.app_name))
                        })
                    },
                    content = { padding ->
                        Column(Modifier.padding(padding)) {
                            NavigationScreen(navController)
                        }
                    })
            }
        }
    }
}

/**
 * Navigation screen handle Navigation trough token list and token details
 * with NavController and HiltViewmodel
 */
@Composable
fun NavigationScreen(navController: NavHostController) {

    NavHost(navController = navController, startDestination = "main") {
        composable(route = "main") { _ ->
            val viewModel = hiltViewModel<MainViewModel>().also {
                // request tokens by ViewModel and observe result in the composable
                it.getTokens()
            }
            TokensScreen(navController, viewModel)
        }
        composable(
            route = "details/{tokenId}",
            arguments = listOf(navArgument("tokenId") {
                type = NavType.StringType
            })
        ) { details ->
            details.arguments?.getString("tokenId")?.let { tokenId ->
                val viewModel = hiltViewModel<MainViewModel>().also {
                    // request tokens details and market data by ViewModel
                    // and observe result in the composable
                    it.getTokenDetails(tokenId)
                    it.getWeeklyMarketChart(tokenId)
                }
                TokenDetailsScreen(navController, viewModel)
            }
        }
    }
}

// region composable
/**
 * TokenScreen shows the list of assets in a simple column
 */
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
    Text(
        text = stringResource(R.string.txt_error_connection),
        modifier = Modifier
            .fillMaxWidth()
    )
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

// endregion

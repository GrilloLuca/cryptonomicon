package com.example.cryptonomicon

import android.graphics.Color
import android.os.Bundle
import android.widget.ScrollView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.ui.MainViewModel
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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = tokens?.let {
                        Modifier.fillMaxSize()
                    } ?: Modifier.fillMaxWidth(),
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

@Composable
fun Greeting(name: String) {
    Text(text = name)
}

@Composable
fun Loader() {
    LinearProgressIndicator()
}


@Composable
fun TokensContent(tokens: List<Token>) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        tokens.forEach { token ->
            TokenListItem(token)
        }
    }
}

@Composable
fun TokenListItem(token: Token) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row {
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
            ) {
                TokenImage(token)
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "${token.name} (${token.symbol})",
                    style = typography.h6
                )
                Text(text = "${token.current_price}$", style = typography.caption)
            }
        }
    }
}

@Composable
private fun TokenImage(token: Token) {
    Image(
        painter = rememberAsyncImagePainter(token.image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(8.dp)
            .size(32.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptonomiconTheme {
        Greeting("Android")
    }
}
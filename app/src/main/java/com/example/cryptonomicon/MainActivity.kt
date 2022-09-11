package com.example.cryptonomicon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.cryptonomicon.MainActivity.Companion.EXTRA_SELECTED_TOKEN
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.ui.MainViewModel
import com.example.cryptonomicon.ui.compose.preview.ErrorProvider
import com.example.cryptonomicon.ui.compose.preview.TokenListProvider
import com.example.cryptonomicon.ui.compose.preview.TokenProvider
import com.example.cryptonomicon.ui.theme.CryptonomiconTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {

        const val EXTRA_SELECTED_TOKEN = "extra_selected_token"

        private const val CURRENCY_EUR = "EUR"
        private const val CURRENCY_USD = "USD"

        private const val MARKET_CAP_DESC = "market_cap_desc"
        private const val MARKET_CAP_ASC = "market_cap_asc"

    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getTokens(CURRENCY_EUR, MARKET_CAP_DESC)

        setContent {
            CryptonomiconTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TokensContent()
                }
            }
        }

    }
}

// region composable

@Preview
@Composable
fun TokensContent(viewModel: MainViewModel = viewModel()) {
    val tokens = viewModel.tokenList.observeAsState()

    tokens.value?.let {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            it.forEach { token ->
                TokenListItem(token)
            }
        }
    } ?: Loader()

}

@Preview
@Composable
fun Loader() {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
fun Error(
    @PreviewParameter(ErrorProvider::class, limit = 1) name: String
) {
    Row {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = name)
        }
    }
}

@Preview
@Composable
private fun TokenImage(
    @PreviewParameter(TokenProvider::class, limit = 1) token: Token
) {
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

@Preview
@Composable
fun TokenListItem(
    @PreviewParameter(TokenProvider::class, limit = 1) token: Token
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row(modifier = Modifier
            .clickable { openDetails(context, token.id) }
        ) {
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
                    style = MaterialTheme.typography.h6
                )
                Text(text = "${token.current_price}€", style = MaterialTheme.typography.caption)
            }
        }
    }

}

// endregion

fun openDetails(context: Context, tokenId: String) {
    val intent = Intent(context, TokenDetailsActivity::class.java)
    intent.putExtra(EXTRA_SELECTED_TOKEN, tokenId)
    context.startActivity(intent)
}


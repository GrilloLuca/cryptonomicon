package com.example.cryptonomicon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.ui.MainViewModel
import com.example.cryptonomicon.ui.compose.preview.TokenProvider
import com.example.cryptonomicon.ui.theme.CryptonomiconTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TokenDetailsActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenId = intent.extras?.get(MainActivity.EXTRA_SELECTED_TOKEN) as? String
        tokenId?.let {
            viewModel.getTokenDetails(it)
        }

        setContent {
            CryptonomiconTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TokenDetailsContent()
                }
            }
        }
    }

    @Preview
    @Composable
    fun TokenDetailsContent(viewModel: MainViewModel = viewModel()) {
        val details = viewModel.tokenDetails.observeAsState()

        details.value?.let {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                DetailCard(it)
            }
        } ?: Loader()

    }

    @Composable
    fun DetailCard(token: TokenDetails) {

        Row {
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
            ) {
                TokenImage(token.image.small)
            }
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
            ) {
                Text(token.links.homepage[0])
            }
        }
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
                    style = MaterialTheme.typography.h6
                )
            }

        }
    }

}

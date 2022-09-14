package com.example.cryptonomicon.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cryptonomicon.R
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.ui.MainViewModel


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
                Row {
                    Text(
                        text = token.name,
                        style = MaterialTheme.typography.h6
                    )
                    token.symbol?.let {
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "($it)",
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
                token.current_price?.let {
                    Text(
                        text = "$it €",
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TokenListItemPreview() {
    return TokenListItem(
        rememberNavController(),
        Token(
            id= "bitcoin",
            name = "Bitcoin",
            symbol = "BTC",
            image = R.drawable.ic_bitcoin,
            current_price = 18000.0f
        )
    )
}

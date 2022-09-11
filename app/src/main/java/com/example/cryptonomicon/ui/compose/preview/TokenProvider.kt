package com.example.cryptonomicon.ui.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.cryptonomicon.models.Token

class TokenProvider : PreviewParameterProvider<Token> {
    override val values: Sequence<Token>
        get() = sequence {
            Token(
                id = "bitcoin",
                symbol = "btc",
                name = "Bitcoin",
                image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
                current_price = 21262f
            )
        }
}

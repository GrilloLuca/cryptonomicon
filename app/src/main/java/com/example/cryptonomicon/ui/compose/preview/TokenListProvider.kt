package com.example.cryptonomicon.ui.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.cryptonomicon.models.Token

class TokenListProvider : PreviewParameterProvider<List<Token>> {
    override val values: Sequence<List<Token>>
        get() = sequence {
            listOf(
                Token(
                    id = "bitcoin",
                    symbol = "btc",
                    name = "Bitcoin",
                    image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
                    current_price = 21262f,
                    market_cap = 407550166703,
                    market_cap_rank = 1,
                    fully_diluted_valuation = 446982400684,
                    total_volume = 32424804832,
                    high_24h = 21428f,
                    low_24h = 20866f,
                    price_change_24h = 15.79,
                    price_change_percentage_24h = 0.0743,
                    market_cap_change_24h = 793350416.0,
                    market_cap_change_percentage_24h = 0.19504,
                    circulating_supply = 19147406f,
                    total_supply = 21000000f,
                    max_supply = 21000000f,
                    ath = 59717f,
                    ath_change_percentage = -64.32536,
                    ath_date = "2021-11-10T14:24:11.849Z",
                    atl = 51.3,
                    atl_change_percentage = 41429.09226,
                    atl_date = "2013-07-05T00:00:00.000Z",
                    roi = null,
                    last_updated = "2022-09-11T06:33:04.548Z"
                )
            )
        }

}

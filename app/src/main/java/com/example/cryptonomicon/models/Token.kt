package com.example.cryptonomicon.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Token(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val symbol: String? = null,
    @ColumnInfo val current_price: Float? = null,
    @ColumnInfo val image: String? = null,

//    val ath: Float,
//    val ath_change_percentage: Double,
//    val ath_date: String,
//    val atl: Double,
//    val atl_change_percentage: Double,
//    val atl_date: String,
//    val circulating_supply: Float,
//    val fully_diluted_valuation: Long?,
//    val last_updated: String,
//    val low_24h: Float?,
//    val high_24h: Float?,
//    val market_cap: Long,
//    val market_cap_change_24h: Double?,
//    val market_cap_change_percentage_24h: Double?,
//    val price_change_percentage_24h: Double?,
//    val price_change_24h: Double?,
//    val market_cap_rank: Int?,
//    val roi: Any?,
//    val max_supply: Float?,
//    val total_supply: Float?,
//    val total_volume: Long
)
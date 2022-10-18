package com.example.cryptonomicon.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TokenDetails(
    @PrimaryKey val id: String,
    @ColumnInfo val description: Description,
    @ColumnInfo val links: Links? = null,
    @ColumnInfo var image: ImageDetail? = null
)
package com.example.cryptonomicon.models

import com.google.gson.annotations.SerializedName

data class GeckoResponse(

    @SerializedName("gecko_says")
    var geckoSays: String
)

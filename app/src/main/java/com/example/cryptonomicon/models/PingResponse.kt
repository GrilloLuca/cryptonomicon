package com.example.cryptonomicon.models

import com.google.gson.annotations.SerializedName

data class PingResponse(

    @SerializedName("gecko_says")
    var geckoSays: String
)

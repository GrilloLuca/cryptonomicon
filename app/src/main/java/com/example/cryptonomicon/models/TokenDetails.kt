package com.example.cryptonomicon.models

data class TokenDetails(
    val description: Description,
    val links: Links? = null,
    var image: ImageDetail? = null
)
package com.example.cryptonomicon.ui.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ErrorProvider: PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf("Error")
}

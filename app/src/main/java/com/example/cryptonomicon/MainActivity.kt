package com.example.cryptonomicon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cryptonomicon.ui.MainViewModel
import com.example.cryptonomicon.ui.theme.CryptonomiconTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.tokenList.observe(this) { tokens ->

            updateContent(
                tokens.joinToString(", ") {
                    it.name
                })

        }
        viewModel.getTokens()

        updateContent(null)

    }

    private fun updateContent(text: String?) {

        setContent {
            CryptonomiconTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = text?.let {
                        Modifier.fillMaxSize()
                    } ?: Modifier.fillMaxWidth() ,
                    color = MaterialTheme.colors.background
                ) {
                    text?.let {
                        Greeting(text)
                    } ?: Loader()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = name)
}

@Composable
fun Loader() {
    LinearProgressIndicator()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptonomiconTheme {
        Greeting("Android")
    }
}
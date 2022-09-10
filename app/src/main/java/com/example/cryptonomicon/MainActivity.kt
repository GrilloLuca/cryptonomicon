package com.example.cryptonomicon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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

        viewModel.response.observe(this) {
            updateContent(it.geckoSays)
        }
        viewModel.ping()

        updateContent("")

    }

    private fun updateContent(text: String) {

        setContent {
            CryptonomiconTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(text)
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "$name")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptonomiconTheme {
        Greeting("Android")
    }
}
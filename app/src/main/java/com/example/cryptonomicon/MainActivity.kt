package com.example.cryptonomicon

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cryptonomicon.ui.MainViewModel
import com.example.cryptonomicon.ui.compose.TokenDetailsScreen
import com.example.cryptonomicon.ui.compose.TokensScreen
import com.example.cryptonomicon.ui.theme.CryptonomiconTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CryptonomiconTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text(stringResource(id = R.string.app_name))
                        })
                    },
                    content = { padding ->
                        Column(Modifier.padding(padding)) {
                            NavigationScreen(navController)
                        }
                    })
            }
        }
    }
}

/**
 * Navigation screen handle Navigation trough token list and token details
 * with NavController and HiltViewModel
 */
@Composable
fun NavigationScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<MainViewModel>()
    val error = viewModel.error.observeAsState()
    error.value?.let {
        if(BuildConfig.DEBUG)
            Toast.makeText(LocalContext.current, it, Toast.LENGTH_LONG).show()
    }

    NavHost(navController = navController, startDestination = "main") {
        composable(route = "main") { _ ->
            // request tokens by ViewModel and observe result in the composable
            viewModel.getTokens()
            TokensScreen(navController, viewModel)
        }
        composable(
            route = "details/{tokenId}",
            arguments = listOf(navArgument("tokenId") {
                type = NavType.StringType
            })
        ) { details ->
            details.arguments?.getString("tokenId")?.let { tokenId ->
                // request tokens details and market data by ViewModel
                // and observe result in the composable
                viewModel.getTokenDetails(tokenId)
                viewModel.getWeeklyMarketChart(tokenId)

                TokenDetailsScreen(navController, viewModel)
            }
        }
    }
}


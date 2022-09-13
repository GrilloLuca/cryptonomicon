package com.example.cryptonomicon

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.datasource.CoinGeckoDatasource
import com.example.cryptonomicon.models.PingResponse
import com.example.cryptonomicon.repository.NetworkRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Response

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @MockK
    lateinit var repo: NetworkRepository

    private lateinit var useCase: CryptoUseCases

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        this.useCase = CryptoUseCases(repo)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.cryptonomicon", appContext.packageName)
    }

    @Test
    fun getTokensUseCaseTest() {

        every {
            repo.ping()
        } returns flow {
            emit(PingResponse("OK"))
        }

        runBlocking {
            useCase.ping().collect {
                assertEquals("OK", it?.geckoSays)
            }
        }

    }

}
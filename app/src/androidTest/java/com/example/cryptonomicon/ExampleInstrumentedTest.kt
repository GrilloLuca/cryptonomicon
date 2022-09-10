package com.example.cryptonomicon

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cryptonomicon.api.CoinGeckoApi
import com.example.cryptonomicon.datasource.CoinGeckoDatasource
import com.example.cryptonomicon.models.GeckoResponse
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
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

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.cryptonomicon", appContext.packageName)
    }

    @Test
    fun GeckoApiPing() {

        val api = CoinGeckoApi.create()
        val datasource = CoinGeckoDatasource(api)

        val res = runBlocking {
            datasource.ping()
        }

        val expected = GeckoResponse("(V3) To the Moon!")

        assertEquals(expected, res.body())
        assertEquals(res.code(), 200)

    }
}
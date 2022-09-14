package com.example.cryptonomicon

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cryptonomicon.models.*
import com.example.cryptonomicon.repository.NetworkRepository
import com.example.cryptonomicon.usecase.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

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
class MainInstrumentedTest {

    @MockK
    lateinit var repo: NetworkRepository

    private lateinit var tokenDetailsUseCase: TokenDetailsUseCase
    private lateinit var tokenListUseCase: GetTokenListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        this.tokenDetailsUseCase = TokenDetailsUseCase(repo)
        this.tokenListUseCase = GetTokenListUseCase(repo)
    }

    @Test
    fun pingUseCaseTest() {
        every {
            repo.ping()
        } returns flow {
            emit(PingResponse("OK"))
        }

//        runBlocking {
//            useCase.ping().collect {
//                assertEquals("OK", it?.geckoSays)
//            }
//        }
    }

    @Test
    fun getTokenListTest() {

        val input = TokenListInput("eur", "desc", 10)

        every {
            runBlocking {
                repo.getTokens(
                    input.currency,
                    input.order,
                    input.page
                )
            }
        } returns listOf()

        runBlocking {
            val result = tokenListUseCase.execute(input)
        }

    }

    @Test
    fun getTokenDetailsTest() {

        val tokenId = "bitcoin"
        every {
            runBlocking {
                repo.getTokenDetails(tokenId)
            }

        } returns TokenDetails(
            Description("OK"), null, null
        )


        runBlocking {

            val details = tokenDetailsUseCase.execute(tokenId)
            assertEquals("OK", details?.description?.en)

        }

    }

}
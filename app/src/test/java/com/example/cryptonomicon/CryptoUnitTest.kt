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
class CryptoUnitTest {

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
    fun tokenListUseCaseSuccessTest() {

        val input = TokenListInput("eur", "desc", 10)

        every {
            runBlocking {
                repo.getTokens(
                    input.currency,
                    input.order,
                    input.page
                )
            }
        } returns Resource.Success(
            listOf(
                Token("bitcoin", "bitcoin")
            ))

        runBlocking {
            val result = tokenListUseCase.execute(input)
            assertEquals("bitcoin", result.data?.first()?.id)
        }

    }

    @Test
    fun tokenDetailsUseCaseSuccessTest() {

        val tokenId = "bitcoin"
        every {
            runBlocking {
                repo.getTokenDetails(tokenId)
            }

        } returns Resource.Success(TokenDetails(
            Description("OK"), null, null
        ))


        runBlocking {

            val details = tokenDetailsUseCase.execute(tokenId)
            assertEquals("OK", details.data?.description?.en)

        }

    }

    @Test
    fun tokenListUseCaseErrorTest() {

        val input = TokenListInput("eur", "desc", 10)

        every {
            runBlocking {
                repo.getTokens(
                    input.currency,
                    input.order,
                    input.page
                )
            }
        } returns Resource.Error("ERROR", null)

        runBlocking {
            val result = tokenListUseCase.execute(input)
            assertEquals("ERROR", result.text)
        }

    }


}
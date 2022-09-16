package com.example.cryptonomicon.usecase

import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTokenListUseCase @Inject constructor(private var repo: CryptoRepository):
    CoroutineUseCase<TokenListInput, Resource<List<Token>>>() {

    override fun execute(input: TokenListInput): Flow<Resource<List<Token>>> = flow {
        val tokens = repo.getTokens(
            input.currency,
            input.order,
            input.page
        )

        emit(tokens)
    }

}

data class TokenListInput(
    val currency: String,
    val order: String,
    val page: Int)
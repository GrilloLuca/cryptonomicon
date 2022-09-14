package com.example.cryptonomicon.usecase

import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.repository.NetworkRepository
import javax.inject.Inject

class GetTokenListUseCase @Inject constructor(private var repo: NetworkRepository):
    CoroutineUseCase<TokenListInput, List<Token>?>() {

    override suspend fun execute(input: TokenListInput): List<Token>? {
        return repo.getTokens(
            input.currency,
            input.order,
            input.page
        )
    }

}

data class TokenListInput(
    val currency: String,
    val order: String,
    val page: Int)
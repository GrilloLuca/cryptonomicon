package com.example.cryptonomicon.usecase

import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.models.Token
import com.example.cryptonomicon.repository.NetworkRepository
import javax.inject.Inject

class GetTokenListUseCase @Inject constructor(private var repo: NetworkRepository):
    CoroutineUseCase<TokenListInput, Resource<List<Token>>>() {

    override suspend fun execute(input: TokenListInput): Resource<List<Token>> {
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
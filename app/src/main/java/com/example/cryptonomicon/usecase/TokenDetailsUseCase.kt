package com.example.cryptonomicon.usecase

import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.repository.NetworkRepository
import javax.inject.Inject

class TokenDetailsUseCase @Inject constructor(private var repo: NetworkRepository):
    CoroutineUseCase<String, TokenDetails?>() {

    override suspend fun execute(input: String) = repo.getTokenDetails(input)
}
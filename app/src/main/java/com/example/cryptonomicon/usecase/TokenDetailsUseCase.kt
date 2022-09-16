package com.example.cryptonomicon.usecase

import com.example.cryptonomicon.Resource
import com.example.cryptonomicon.models.TokenDetails
import com.example.cryptonomicon.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TokenDetailsUseCase @Inject constructor(private var repo: CryptoRepository):
    CoroutineUseCase<String, Resource<TokenDetails>>() {

    override fun execute(input: String) = repo.getTokenDetails(input)
}
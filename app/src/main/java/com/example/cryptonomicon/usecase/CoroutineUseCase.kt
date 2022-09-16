package com.example.cryptonomicon.usecase

import kotlinx.coroutines.flow.Flow

abstract class CoroutineUseCase<Input, Output> {
    abstract fun execute(input: Input): Flow<Output>
}

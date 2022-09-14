package com.example.cryptonomicon.usecase

abstract class CoroutineUseCase<Input, Output> {
    abstract suspend fun execute(input: Input): Output
}

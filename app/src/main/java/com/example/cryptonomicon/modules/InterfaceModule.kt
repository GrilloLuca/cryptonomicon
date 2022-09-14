package com.example.cryptonomicon.modules

import com.example.cryptonomicon.usecase.CoroutineUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
abstract class InterfaceModule {

    @Binds
    abstract fun bindCoroutineUseCase(validator: CoroutineUseCase<Any, Any>) : CoroutineUseCase<Any, Any>

}
package com.example.cryptonomicon.api

import com.example.cryptonomicon.BuildConfig
import com.example.cryptonomicon.models.GeckoResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CoinGeckoApi {

    @GET("ping")
    suspend fun ping(): Response<GeckoResponse>

    companion object {

        fun create(): CoinGeckoApi {

            val httpClient = OkHttpClient
                .Builder()
                .apply {
                    this.addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })

                    this.addInterceptor { chain ->
                        val original: Request = chain.request()
                        val requestBuilder: Request.Builder = original.newBuilder()
                            .method(original.method, original.body)

                        chain.proceed(requestBuilder.build())
                    }
                }

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoinGeckoApi::class.java)

        }

    }

}
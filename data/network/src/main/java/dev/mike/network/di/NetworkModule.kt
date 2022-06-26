package dev.mike.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mike.network.ApiService
import dev.mike.network.BuildConfig
import dev.mike.network.utils.BASE_URL
import dev.mike.network.utils.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @ExperimentalSerializationApi
    private val converter = json.asConverterFactory("application/json".toMediaType())

    private fun okHttpClient1(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(10, TimeUnit.SECONDS)
        okHttpClient.readTimeout(30, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(30, TimeUnit.SECONDS)

        when {
            BuildConfig.DEBUG -> {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                okHttpClient.addInterceptor(logging)
            }
        }

        return okHttpClient
    }
    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun providesAPI(): ApiService {

        val httpClient = okHttpClient1()
        return Retrofit.Builder()
            .addConverterFactory(converter)
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .build()
            .create(ApiService::class.java)
    }
}

package com.idle.togeduck.di

import com.idle.togeduck.model.dto.event.DefaultEventRepository
import com.idle.togeduck.model.dto.event.EventRepository
import com.idle.togeduck.model.dto.event.EventService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = ""

    @Singleton
    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
//            .addInterceptor() // TODO. 인터셉터 추가 필요
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val nullOnEmptyConverterFactory = object : Converter.Factory() {
            fun converterFactory() = this

            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit,
            ) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter =
                    retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

                override fun convert(value: ResponseBody) =
                    if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }

        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideEventService(retrofit: Retrofit) : EventService {
        return retrofit.create(EventService::class.java)
    }

    @Singleton
    @Provides
    fun provideEventRepository(eventService: EventService) : EventRepository {
        return DefaultEventRepository(eventService)
    }
}


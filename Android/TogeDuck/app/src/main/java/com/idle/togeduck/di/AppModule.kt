package com.idle.togeduck.di

import com.idle.togeduck.event.model.DefaultEventRepository
import com.idle.togeduck.event.model.EventRepository
import com.idle.togeduck.event.model.EventService
import com.idle.togeduck.my_record.model.DefaultHistoryRepository
import com.idle.togeduck.my_record.model.HistoryRepository
import com.idle.togeduck.my_record.model.HistoryService
import com.idle.togeduck.quest.exchange.model.DefaultExchangeRepository
import com.idle.togeduck.quest.exchange.model.ExchangeRepository
import com.idle.togeduck.quest.exchange.model.ExchangeService
import com.idle.togeduck.quest.recruit.model.DefaultRecruitRepository
import com.idle.togeduck.quest.recruit.model.RecruitRepository
import com.idle.togeduck.quest.recruit.model.RecruitService
import com.idle.togeduck.quest.share.model.DefaultShareRepository
import com.idle.togeduck.quest.share.model.ShareRepository
import com.idle.togeduck.quest.share.model.ShareService
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
import retrofit2.create
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

    @Singleton
    @Provides
    fun provideShareService(retrofit: Retrofit) :ShareService {
        return retrofit.create(ShareService::class.java)
    }

    @Singleton
    @Provides
    fun provideShareRepository(shareService: ShareService) :ShareRepository {
        return DefaultShareRepository(shareService)
    }

    @Singleton
    @Provides
    fun provideExchangeService(retrofit: Retrofit) : ExchangeService {
        return retrofit.create(ExchangeService::class.java)
    }

    @Singleton
    @Provides
    fun provideExchangeRepository(exchangeService: ExchangeService) : ExchangeRepository {
        return DefaultExchangeRepository(exchangeService)
    }

    @Singleton
    @Provides
    fun provideRecruitService(retrofit: Retrofit) : RecruitService {
        return retrofit.create(RecruitService::class.java)
    }

    @Singleton
    @Provides
    fun provideRecruitRepository(recruitService: RecruitService) : RecruitRepository {
        return DefaultRecruitRepository(recruitService)
    }

    @Singleton
    @Provides
    fun provideHistoryService(retrofit: Retrofit) : HistoryService {
        return retrofit.create(HistoryService::class.java)
    }

    @Singleton
    @Provides
    fun provideHistoryRepository(historyService: HistoryService) : HistoryRepository {
        return DefaultHistoryRepository(historyService)
    }
}


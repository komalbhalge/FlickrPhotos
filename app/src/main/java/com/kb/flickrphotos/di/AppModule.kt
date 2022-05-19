package com.kb.flickrphotos.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kb.flickrphotos.BuildConfig
import com.kb.flickrphotos.controllers.Controller
import com.kb.flickrphotos.data.repositories.APIRepository
import com.kb.flickrphotos.data.service.FlickrAPIService
import com.kb.flickrphotos.data.service.FlickrApiHelper
import com.kb.flickrphotos.data.service.FlickrApiHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideController(@ApplicationContext appContext: Context): Controller {
        return Controller(appContext)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            )
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String,
        httpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideFlickrApiHelper(flickrApiHelper: FlickrApiHelperImpl): FlickrApiHelper =
        flickrApiHelper

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): FlickrAPIService =
        retrofit.create(FlickrAPIService::class.java)

    @Provides
    @Singleton
    fun provideAPIRepository(flickrApiHelper: FlickrApiHelper): APIRepository =
        APIRepository(flickrApiHelper)
}

package com.example.notes_app.di

import com.example.notes_app.api.AuthInterceptor
import com.example.notes_app.api.NotesApi
import com.example.notes_app.api.UserApi
import com.example.notes_app.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)

    }

    @Singleton
    @Provides
    fun providesOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }


    // so here basically Hilt will call provideRetrofitBuilder() and pass that Retrofit.Builder
    // here automatically because of different annotation we have used
    @Singleton
    @Provides
    fun providesUserApi(retrofitBuilder: Retrofit.Builder): UserApi {
        return retrofitBuilder.build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun providesNotesApi(retrofitBuilder: Builder, okHttpClient: OkHttpClient): NotesApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(NotesApi::class.java)
    }

}
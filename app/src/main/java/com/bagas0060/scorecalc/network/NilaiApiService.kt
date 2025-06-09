package com.bagas0060.scorecalc.network

import com.bagas0060.scorecalc.model.IpSemesterImage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://score-api.bagasaldianata.my.id/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NilaiApiService {
    @GET("scores")
    suspend fun getNilai(): List<IpSemesterImage>
}

object NilaiApi {
    val service: NilaiApiService by lazy {
        retrofit.create(NilaiApiService::class.java)
    }

    fun getIpSemesterImageUrl(gambar: String): String{
        return gambar
    }
}

enum class ApiStatus { LOADING, SUCCESS}
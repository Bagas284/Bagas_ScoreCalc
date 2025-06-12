package com.bagas0060.scorecalc.network

import com.bagas0060.scorecalc.model.IpSemesterImage
import com.bagas0060.scorecalc.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

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
    suspend fun getNilai(
        @Header("Authorization") email: String
    ): List<IpSemesterImage>

    @Multipart
    @POST("scores")
    suspend fun tambahNilai(
        @Header("Authorization") email: String,
        @Part("semester") semester: RequestBody,
        @Part("mataKuliah") mataKuliah: RequestBody,
        @Part gambar: MultipartBody.Part
    ): OpStatus

    @DELETE("scores")
    suspend fun hapusNilai(
        @Header("Authorization") email: String,
        @Query("id") id: String
    ): OpStatus
}

object NilaiApi {
    val service: NilaiApiService by lazy {
        retrofit.create(NilaiApiService::class.java)
    }

    fun getIpSemesterImageUrl(gambar: String): String{
        return "${BASE_URL}image?id=$gambar"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED}
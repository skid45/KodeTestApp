package com.skid.kodetestapp.data.remote

import com.google.gson.GsonBuilder
import com.skid.kodetestapp.data.model.ResponseNetworkEntity
import com.skid.kodetestapp.data.utils.LocalDateDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import java.time.LocalDate

interface UserService {

    @GET("users")
    @Headers("Prefer: code=200, example=success")
//    @Headers("Prefer: code=200, dynamic=true")
//    @Headers("Prefer: code=500, example=error-500")
    suspend fun getUsers(): ResponseNetworkEntity
}

fun UserService(): UserService {
    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    return retrofit.create(UserService::class.java)
}

private const val BASE_URL = "https://stoplight.io/mocks/kode-education/trainee-test/25143926/"
package com.ppp.pegasussociety.ApiInterface

import com.ppp.pegasussociety.Authentication.Signup.CountryCode
import com.ppp.pegasussociety.SignUpResponse.SignUpResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AllApi {

    @Multipart
    @POST("User/signup")
    suspend fun signUp(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("countryCode") countryCode: RequestBody,
        @Part("mobileNo") mobileNo: RequestBody
    ): Response<SignUpResponse>

    @GET("countryinfo/all")
    suspend fun getCountry(): Response<List<CountryCode>>

}
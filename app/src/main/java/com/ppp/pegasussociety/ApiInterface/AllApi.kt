package com.ppp.pegasussociety.ApiInterface

import com.ppp.pegasussociety.CountryData.CountryCode
import com.ppp.pegasussociety.Login.VerifyResponse
import com.ppp.pegasussociety.Model.ResponseData
import com.ppp.pegasussociety.SignUpResponse.SignUpResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AllApi {

    @Multipart
    @POST("user/login-verify")
    suspend fun login(
        @Part("email") email: RequestBody,
        @Part("otp") otp: RequestBody
    ): Response<ResponseData>



/*    @Multipart
    @POST("api/user/login")
    suspend fun sendOtp(
        @Part("email") email: RequestBody
    ): Response<OtpResponse>
    */
    @Multipart
    @POST("User/signup")
    suspend fun signUp(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("countryCode") countryCode: RequestBody,
        @Part("phoneNumber") mobileNo: RequestBody
    ): Response<SignUpResponse>

    @GET("countryinfo/all")
    suspend fun getCountry(): Response<List<CountryCode>>

    @GET("VerifyPhoneEmail/check")
    suspend fun verifyInput(
        @Query("input") input: String
    ): Response<VerifyResponse>

}
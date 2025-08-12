package com.ppp.pegasussociety.ApiInterface

import com.ppp.pegasussociety.CountryData.CountryCode
import com.ppp.pegasussociety.Login.VerifyResponse
import com.ppp.pegasussociety.Model.PostDto
import com.ppp.pegasussociety.Model.ResponseData
import com.ppp.pegasussociety.Model.ScreenTimeApiResponse
import com.ppp.pegasussociety.Model.ScreenTimeEntryRequest
import com.ppp.pegasussociety.SignUpResponse.SignUpResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AllApi {

    @GET("Posts/by-interest/{category}")
    suspend fun getPostsByInterest(@Path("category") category: String): List<PostDto>


    @GET("Posts/{postId}")
    suspend fun getPostById(
        @Path("postId") postId: Int
    ): Response<PostDto>

    @GET("Posts/all")
    suspend fun getAllPosts(): List<PostDto>

    @GET("Posts/latest")
    suspend fun getLatestPosts(): List<PostDto>

    @GET("Posts/popular")
    suspend fun getPopularPosts(): List<PostDto>

    @POST("ScreenTimeApi/log-screen-time")
    suspend fun logScreenTime(
        @Body entry: ScreenTimeEntryRequest
    ): Response<Unit> // or a response model if server returns one

    @GET("ScreenTimeApi/screen-time/{childrenId}")
    suspend fun getScreenTimeLogs(
        @Path("childrenId") childId: String
    ): Response<ScreenTimeApiResponse>

/*
    @POST("ScreenTimeApi/log-screen-time")
    suspend fun postScreenTimeEntry(@Body entry: ScreenTimeEntry)

    @GET("/screen-time")
    suspend fun getAllScreenTimeEntries(): List<ScreenTimeEntry>
*/

    //  @POST("v1/screentime/add")
   // suspend fun addEntry(@Body entry: ScreenTimeEntry): Response<Unit> // Assuming the API returns an empty successful response.

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
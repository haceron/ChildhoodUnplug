package com.ppp.pegasussociety.Repository

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.ppp.pegasussociety.ApiInterface.AllApi
import com.ppp.pegasussociety.CountryData.CountryCode
import com.ppp.pegasussociety.Login.VerifyResponse
import com.ppp.pegasussociety.Model.AddChildRequest
import com.ppp.pegasussociety.Model.AddChildResponse
import com.ppp.pegasussociety.Model.ChildrenResponse
import com.ppp.pegasussociety.Model.ScreenTimeEntryRequest
import com.ppp.pegasussociety.Screens.ActivityBannerItem
import com.ppp.pegasussociety.SharedPrefManager
import com.ppp.pegasussociety.SignUpResponse.SignUpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val allApi: AllApi) {

    suspend fun addChild(
        childrenName: String,
        parentId: String,
        interest: List<String>,
        focusArea: List<String>,
        gender: String,
        DOB: String
    ): AddChildResponse {
        // Convert lists to comma-separated strings
        val interestStr = interest.joinToString(",")
        val focusAreaStr = focusArea.joinToString(",")

        // Convert to RequestBody
        val childrenNamePart = childrenName.toRequestBody("text/plain".toMediaTypeOrNull())
        val interestPart = interestStr.toRequestBody("text/plain".toMediaTypeOrNull())
        val focusAreaPart = focusAreaStr.toRequestBody("text/plain".toMediaTypeOrNull())
        val genderPart = gender.toRequestBody("text/plain".toMediaTypeOrNull())
        val dobPart = DOB.toRequestBody("text/plain".toMediaTypeOrNull())

        // Call API
        return allApi.addChild(
            parentId = parentId,
            childrenName = childrenNamePart,
            interest = interestPart,
            focusArea = focusAreaPart,
            gender = genderPart,
            DOB = dobPart
        )
    }

    suspend fun getChild(parentId: String):ChildrenResponse{
              return  allApi.getChild(parentId)
    }


    suspend fun getActivitiesByCategory(category: String): List<ActivityBannerItem> {
        return try {
            val response = allApi.getPostsByInterest(category) // category is already URL encoded
            response.map {
                ActivityBannerItem(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.imageUrl,
                    bgColor = Color(0xFFB4DB6F),
                    content = it.content,
                    attachmentUrl = it.attachmentUrl
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


    suspend fun getActivityLatest(): List<ActivityBannerItem> {
        return try {
            val response = allApi.getLatestPosts()
            response.map {
                ActivityBannerItem(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.imageUrl,
                    bgColor = Color(0xFFB4DB6F), // Could be improved using category
                    content = it.content,
                    attachmentUrl = it.attachmentUrl
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getActivityPopular(): List<ActivityBannerItem>{
        return try {
            val response = allApi.getPopularPosts()
            response.map {
                ActivityBannerItem(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.imageUrl,
                    bgColor = Color(0xFFB4DB6F), // Could be improved using category
                    content = it.content,
                    attachmentUrl = it.attachmentUrl
                )
            }

        }
        catch (e: Exception){
            emptyList()
        }
    }

    suspend fun getActivityBanners(): List<ActivityBannerItem> {
        return try {
            val response = allApi.getAllPosts()
            response.map {
                ActivityBannerItem(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.imageUrl,
                    bgColor = Color(0xFFB4DB6F), // Could be improved using category
                    content = it.content,
                    attachmentUrl = it.attachmentUrl
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getActivityById(articleId: Int): ActivityBannerItem? {
        return try {
            val response = allApi.getPostById(articleId) // Ensure this returns a single item, not a list
            response.body()?.let {
                ActivityBannerItem(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.imageUrl,
                    bgColor = Color(0xFFB4DB6F),
                    content = it.content,
                    attachmentUrl = it.attachmentUrl
                )
            }
        } catch (e: Exception) {
            null
        }
    }


    suspend fun postEntry(entry: ScreenTimeEntryRequest): Result<Unit> {
        return try {
            val response = allApi.logScreenTime(entry)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private val _signUpResponse = MutableStateFlow<SignUpResponse>(SignUpResponse())
    val signUpResponse : StateFlow<SignUpResponse>
        get() = _signUpResponse

    private val _signUpResponseCode = MutableStateFlow<Int>(0)
    val signUpResponseCode : StateFlow<Int>
        get() = _signUpResponseCode




    suspend fun signUp(
        name: String,
        email: String,
        countryCode: String,
        mobileNo: String,
        context: Context)  {
        //Log.d("Signup", "Repository")
        //Log.d(
        /*       "signupcheck",
               "parent_name: $parent_name, email: $email,  mobileno: $mobileno, city: $city, kids: $kids"
           )*/

        try {
            val response = allApi.signUp(
                name = name.toRequestBody("Multipart/form-data".toMediaTypeOrNull()),
                email = email.toRequestBody("Multipart/form-data".toMediaTypeOrNull()),
                countryCode = countryCode.toRequestBody("Multipart/form-data".toMediaTypeOrNull()),
                mobileNo = mobileNo.toRequestBody("Multipart/form-data".toMediaTypeOrNull())
            )
            if (response.isSuccessful && response.body() != null) {
                val sp = SharedPrefManager(context)
                sp.saveLoginStatus(true)
                sp.saveID(response.body()?.user?.id!!)
                sp.saveFullName(response.body()?.user?.name!!)
                sp.saveEmail(response.body()?.user?.email!!)
                sp.savePhone(response.body()?.user?.phoneNumber!!)
                sp.saveCountryCode(response.body()?.user?.countryCode!!)
                //Log.d(
                /*         "CreateMember",
                         "Sent : ${response.code()} ${response.message()} ${response.body()?.message}"
                     )*/
                _signUpResponse.emit(response.body()!!)
                _signUpResponseCode.emit(response.code())
                //   Toast.makeText(context, "Signup Successful", Toast.LENGTH_SHORT).show()
            } else if (response.code() == 409) {
                _signUpResponse.emit(response.body()!!)
            } else {
                //Log.d(
                /*           "CreateMember",
                           "${response.code()} ${response.message()} ${response.body()?.message}"
                       )*/
                _signUpResponseCode.emit(response.code())
                // Toast.makeText(context, "User already exists !!", Toast.LENGTH_SHORT).show()

            }
        } catch (e: Exception) {
            //Log.d("SignUp With", e.message.toString())
            // Toast.makeText(context, "Try again, Something went wrong !!", Toast.LENGTH_SHORT).show()

            _signUpResponseCode.emit(400)
        }
    }

    // Fetch country code list
    suspend fun getCountryList(): Response<List<CountryCode>> {
        return withContext(Dispatchers.IO) {
            allApi.getCountry()
        }
    }

    private val _loginResponse = MutableStateFlow("wait")
    val loginResponse: StateFlow<String>
        get() = _loginResponse

    /*        private val _loginResponseCode = MutableStateFlow(0)
            val loginResponseCode: StateFlow<Int>
            get() = _loginResponseCode*/
    private val _loginResponseCode = MutableStateFlow(0)
    val loginResponseCode: StateFlow<Int>
        get() = _loginResponseCode

    suspend fun login(email: String, otp: String, context: Context): Int {
        return try {
            val sp = SharedPrefManager(context)
            val response = allApi.login(
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), otp)
            )

            if (response.isSuccessful && response.body() != null) {
                sp.saveLoginStatus(true)
                _loginResponseCode.emit(response.code())  // Emit response code
                _loginResponse.emit(response.body()?.message ?: "Login Successful")

                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                //Log.d("Login", "Success: ${response.body()?.message} - Code: ${response.code()}")
                response.code() // ✅ Return response code
            } else {
                _loginResponseCode.emit(response.code())
                _loginResponse.emit("Error in Login: ${response.message()}")

                Toast.makeText(context, "Login Failure!!", Toast.LENGTH_SHORT).show()
                //Log.d("Login", "Failure: ${response.errorBody()?.string()} - Code: ${response.code()}, ")
                response.code() // ✅ Return failure code
            }
        } catch (e: Exception) {
            _loginResponse.emit("Something Went Wrong: ${e.message}")
            //Log.e("Login Error", e.message ?: "Unknown error")
            500 // Return an error code (e.g., 500 for server error)
        }
    }

    suspend fun verifyInput(input: String): VerifyResponse {
        val response = allApi.verifyInput(input)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("API failed: ${response.code()}")
        }
    }

}


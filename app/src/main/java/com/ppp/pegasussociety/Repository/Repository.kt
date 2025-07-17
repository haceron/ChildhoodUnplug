package com.ppp.pegasussociety.Repository

import android.content.Context
import com.ppp.pegasussociety.ApiInterface.AllApi
import com.ppp.pegasussociety.Authentication.Signup.CountryCode
import com.ppp.pegasussociety.SharedPrefManager
import com.ppp.pegasussociety.SignUpResponse.SignUpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val allApi: AllApi) {

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

}


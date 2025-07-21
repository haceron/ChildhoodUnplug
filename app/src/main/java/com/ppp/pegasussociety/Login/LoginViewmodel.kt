package com.ppp.pegasussociety.Login


import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppp.pegasussociety.CountryData.CountryCode
import com.ppp.pegasussociety.NetworkMonitor
import com.ppp.pegasussociety.SharedPrefManager
import com.msg91.sendotp.OTPWidget
import com.ppp.pegasussociety.Repository.Repository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPrefManager: SharedPrefManager,
    networkMonitor: NetworkMonitor


) : ViewModel() {
   // val context = LocalContext.current
    private val _loginResponseCode = MutableStateFlow(0)
    val loginResponseCode: StateFlow<Int> get() = _loginResponseCode
    private val _loginResponse = MutableStateFlow("")
    val loginResponse: StateFlow<String> get() = _loginResponse
    private val _otpStatus = MutableStateFlow("")
    val otpStatus: StateFlow<String> get() = _otpStatus
    var email by mutableStateOf("")
    var otp by mutableStateOf("")
    var isLogin = (false)
    var mobile by mutableStateOf("")
    var _isLoading = mutableStateOf(false)
        private set

    val isOnline = networkMonitor.isConnected
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    //  private val sharedPrefManager = SharedPrefManager(context)
    val responsivecode: StateFlow<Int>
        get() = repository.loginResponseCode

    private val _countryList = mutableStateOf<List<CountryCode>>(  listOf(
        CountryCode(
            name = "India",
            dial_code = "+91",
            code = "IN",
            flag = "🇮🇳"
        )
    ))
    val countryList: State<List<CountryCode>> = _countryList

    private val _selectedCountryCode = mutableStateOf<CountryCode?>( CountryCode(
        name = "India",
        dial_code = "+91",
        code = "IN",
        flag = "🇮🇳"
    )
    )
    val selectedCountryCode: State<CountryCode?> = _selectedCountryCode

    fun onCountrySelected(countryCode: CountryCode) {
        _selectedCountryCode.value = countryCode
    }

    // State for API message
    private val _verifyMessage = MutableStateFlow<String?>(null)
    val verifyMessage: StateFlow<String?> = _verifyMessage

/*    suspend fun verifyPhoneOrEmailNow(input: String): Result<String> {
        return repository.verifyPhoneOrEmail(input)
    }*/

/*    private val _isVerifying = MutableStateFlow(false)
    val isVerifying: StateFlow<Boolean> = _isVerifying

    suspend fun verifyPhoneOrEmailNow(input: String): Result<VerifyModel> {
        _isVerifying.value = true
        val result = repository.verifyPhoneOrEmail(input)
        _isVerifying.value = false
        return result
    }*/


/*    suspend fun verifyPhoneOrEmailNow(input: String): Result<String> {
        return try {
            _isVerifying.value = true
            val result = repository.verifyPhoneOrEmail(input)
            result
        } finally {
            _isVerifying.value = false
        }
    }*/

    private val _parentName = MutableStateFlow("")
    val parentName: StateFlow<String> = _parentName

    fun loadParentName() {
        _parentName.value = sharedPrefManager.getFullName() ?: ""
    }

    private val _isVerifying = MutableStateFlow(false)
    val isVerifying: StateFlow<Boolean> = _isVerifying

    fun verifyPhoneOrEmailNow(input: String): Flow<Result<VerifyResponse>> = flow {
        _isVerifying.value = true
        try {
            val response = repository.verifyInput(input)
            sharedPrefManager.apply {
                saveID(response.data.id)
                saveFullName(response.data.name)
                savePhone(response.data.phoneNumber)
                saveEmail(response.data.email)
                saveCountryCode(response.data.countryCode)
                saveLoginStatus(true)
            }

           // emit(Result.success(response))
            sharedPrefManager.saveAll(response.data)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        } finally {
            _isVerifying.value = false
        }
    }


    private val _userIdentifier = mutableStateOf<String?>(null)
    val userIdentifier: State<String?> = _userIdentifier

    fun saveUserIdentifier(input: String) {
        _userIdentifier.value = input
    }

    fun fetchCountries() {
        viewModelScope.launch {
            try {
                val response = repository.getCountryList()
                if (response.isSuccessful && response.body() != null) {
                    val countries = response.body()!!
                    _countryList.value = countries

                    // Set default selected country to India 🇮🇳
                    val defaultCountry = countries.find { it.code.equals("IN", ignoreCase = true) }
                    _selectedCountryCode.value = defaultCountry ?: countries.firstOrNull()

                    //Log.d("CountryDefault", "Selected: ${_selectedCountryCode.value}")
                } else {
                    //Log.e("CountryError", "Response error: ${response.code()}")
                }
            } catch (e: Exception) {
                //Log.e("CountryError", "Exception: ${e.message}")
            }
        }
    }

    fun onEmailTextChanged(newText: String) {
        email = newText
    }

    fun onOtpTextChanged(newText: String) {
        otp = newText
    }

    fun onMobileTextChanged(newText: String){
        mobile = newText
    }
    var reqId by mutableStateOf("")
        private set


    private val _otpResult = mutableStateOf<String?>(null)
    val otpResult: State<String?> = _otpResult

    fun resetOtpStatus() {
        if (_otpStatus.value != "sent" && !_otpStatus.value.startsWith("success")) {
            _otpStatus.value = "sent"
        }
    }


    fun handleSendOTP(identifier: String) {
        val widgetId = "3564426c4c62343737343432"
        val tokenAuth = "431949TSrZlg16U680f7642P1"

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    OTPWidget.sendOTP(widgetId, tokenAuth, identifier)
                }
                val json = JSONObject(result)
                val type = json.getString("type")
                val message = json.getString("message")

                if (type == "success") {
                    reqId = message
                    _otpStatus.value = "sent"
                } else {
                    _otpStatus.value = "error: $message"
                }
            } catch (e: Exception) {
                _otpStatus.value = "error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun handleVerifyOtp(otp: String) {
        val widgetId = "3564426c4c62343737343432"
        val tokenAuth = "431949TSrZlg16U680f7642P1"

        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    OTPWidget.verifyOTP(widgetId, tokenAuth, reqId, otp)
                }
                //Log.d("req", "$reqId, $otp")

                val json = JSONObject(result)
                val type = json.getString("type")
                val message = json.getString("message")

                _otpStatus.value = if (type == "success") "success" else "error: $message"

            } catch (e: Exception) {
                _otpStatus.value = "error: ${e.message}"
            }
        }
    }


    fun login(email: String, otp: String, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = repository.login(email, otp, context)
            _loginResponse.emit(response.toString())

            _isLoading.value = false
        }
    }




    /* fun fetchCountries() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = repository.getCountryList()
            if (response.isSuccessful) {
                _countryList.value = response.body() ?: emptyList()
            } else {
                _countryList.value = emptyList() // Handle error properly in production
            }
            _isLoading.value = false
        }
    }
*/
    /*    fun sendOtp(email: String, context: Context) {
            viewModelScope.launch {
                _isLoading.value = true
                val response = repository.sendOtp(email, context)
              //  SharedPrefManager(context).saveEmail(email)
                 delay(2000)
                _otpStatus.value = response.toString() // Should return "otp sent"
                _isLoading.value = false
            //    //Log.d("Otpload", "${_otpStatus.value}")

            }
        }*/

    // Call to verify phone/email
    /*    fun verifyPhoneOrEmail(input: String) {
            viewModelScope.launch {
                val result = repository.verifyPhoneOrEmail(input)
                result.onSuccess {
                    _verifyMessage.value = it
                }.onFailure {
                    _verifyMessage.value = "Error: ${it.message}"
                }
            }
        }*/


    /*  fun handleVerifyOtp(otp: String) {
          val widgetId = "3564426c4c62343737343432"
          val tokenAuth = "431949TSrZlg16U680f7642P1"

          viewModelScope.launch {
              try {
                  val result = withContext(Dispatchers.IO) {
                      OTPWidget.verifyOTP(widgetId, tokenAuth, reqId, otp)
                  }

                  val json = JSONObject(result)
                  val type = json.getString("type")
                  val message = json.getString("message")

                  if (type != "error") {
                      _otpStatus.value = "success"
                  } else {
                      _otpStatus.value = "error: $message"
                  }

              } catch (e: Exception) {
                  _otpStatus.value = "error: ${e.message}"
              }
          }
      }*/

    /* private fun handleSendOTP() {
         val widgetId = "3564426c4c62343737343432";
         val tokenAuth = "431949AETWDJ9e16808af80P1"//"{authToken}";

         val identifier = "918859401740"; // or 'example@xyz.com'

         viewModelScope.launch {
             try {

                 val result = withContext(Dispatchers.IO) {
                     OTPWidget.sendOTP(widgetId, tokenAuth, identifier)
                 }
                 println("Result: $result")

             } catch (e: Exception) {
                 println("Error in SendOTP")
             }
         }
     }*/
}



/*
package com.ppp.pegasussociety.Login

*/
/*
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ppp.pegasussociety.SharedPrefManager
import com.ppp.pegasussociety.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){

    val otpResponse = repository.otpResponse
    val otpResponseCode = repository.otpResponseCode
    var emailSP by mutableStateOf("")




    suspend fun sendOtp(context: Context){

        repository.sendOtp(
            emailSP,
            context
        )
    }

    fun initSharedPrefs(sharedPrefManager: SharedPrefManager){
        if(sharedPrefManager.getEmail()!!.isNotEmpty()){
            emailSP = sharedPrefManager.getEmail()!!
        }

    }

}*//*


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppp.pegasussociety.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import android.content.Context
import com.msg91.sendotp.OTPWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

@HiltViewModel
class OTPViewModel @Inject constructor(
    private val repository: OTPRepository
) : ViewModel() {

    val otpResult = mutableStateOf<String?>(null)
    val isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var isVerifying by mutableStateOf(false)
    var otpSuccess by mutableStateOf<Boolean?>(null)
    var reqId by mutableStateOf("919927258100")
    val widgetId = "3564426c4c62343737343432"

    fun verifyOtp(widgetId: String, token: String, reqId: String, otp: String) {
        viewModelScope.launch {
            isVerifying = true
            try {
                val result = repository.verifyOTP(widgetId, token, reqId, otp)
                // You can inspect `result` or check if verification was successful
                otpSuccess = result.contains("success", ignoreCase = true)
            } catch (e: Exception) {
                errorMessage.value = e.message
                otpSuccess = false
            } finally {
                isVerifying = false
            }
        }
    }

    fun sendOTP(widgetId: String, token: String, identifier: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                otpResult.value = repository.sendOTP(widgetId, token, identifier)
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
            isLoading.value = false
        }
    }

    fun retryOTP(widgetId: String, token: String, reqId: String, channel: Number) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                otpResult.value = repository.retryOTP(widgetId, token, reqId, channel)
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
            isLoading.value = false
        }
    }

*/
/*    fun verifyOTP(widgetId: String, token: String, reqId: String, otp: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                otpResult.value = repository.verifyOTP(widgetId, token, reqId, otp)
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
            isLoading.value = false
        }
    }*//*

}


*/
/*
@HiltViewModel
class OtpViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _loginResponseCode = MutableStateFlow(0)
    val loginResponseCode: StateFlow<Int> get() = _loginResponseCode

    private val _otpStatus = MutableStateFlow("")
    val otpStatus: StateFlow<String> get() = _otpStatus

    var email by mutableStateOf("")
    var otp by mutableStateOf("")

    fun onEmailTextChanged(newText: String) {
        email = newText
    }

    fun onOtpTextChanged(newText: String) {
        otp = newText
    }
*//*

*/
/*
    fun sendOtp(email: String, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = repository.sendOtp(email, context)
            _otpStatus.value = response.toString() // Should return "otp sent"
            _isLoading.value = false
        }
    }*//*
*/
/*



  *//*

*/
/*  private fun handleSendOTP() {
        val widgetId = "3564426c4c62343737343432";
        val tokenAuth = "{authToken}";

        val identifier = "9927258100"; // or 'example@xyz.com'

        coroutineScope.launch {
            try {

                val result = withContext(Dispatchers.IO) {
                    OTPWidget.sendOTP(widgetId, tokenAuth, identifier)
                }
                println("Result: $result")

            } catch (e: Exception) {
                println("Error in SendOTP")
            }
        }
    }
*//*
*/
/*



   *//*

*/
/* fun login(email: String, otp: String, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            val responseCode = repository.login(email, otp, context)
            _loginResponseCode.emit(responseCode)
            _isLoading.value = false
        }
    }*//*
*/
/*

}*/
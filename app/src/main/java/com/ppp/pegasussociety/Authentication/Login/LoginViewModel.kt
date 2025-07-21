/*
package com.ppp.pegasussociety.Authentication.Login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msg91.sendotp.OTPWidget
import com.ppp.pegasussociety.Authentication.Signup.CountryCode
import com.ppp.pegasussociety.NetworkMonitor
import com.ppp.pegasussociety.Repository.Repository
import com.ppp.pegasussociety.SharedPrefManager

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

    private val _countryList = mutableStateOf<List<CountryCode>>(
        listOf(
            CountryCode(
                name = "India",
                dial_code = "+91",
                code = "IN",
                flag = "🇮🇳"
            )
        )
    )
    val countryList: State<List<CountryCode>> = _countryList

    private val _selectedCountryCode = mutableStateOf<CountryCode?>(
        CountryCode(
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

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    fun loadParentName() {
        _name.value = sharedPrefManager.getFullName() ?: ""
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
                savePhone(response.data.phoneNo)
                saveEmail(response.data.email)
             //   saveCity(response.data.city)
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

    fun onMobileTextChanged(newText: String) {
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

*/
/*
    fun login(email: String, otp: String, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = repository.login(email, otp, context)
            _loginResponse.emit(response.toString())

            _isLoading.value = false
        }
    }*//*

}
*/

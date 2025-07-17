package com.ppp.pegasussociety.Authentication.Signup

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppp.pegasussociety.SharedPrefManager
import com.ppp.pegasussociety.Repository.Repository
import com.ppp.pegasussociety.SignUpResponse.SignUpResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SignUpViewmodel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var NameSP by mutableStateOf("")
    var mobileNoSP by mutableStateOf("")
    var emailSP by mutableStateOf("")
    var citySP by mutableStateOf("")
    var otpTxt by mutableStateOf("")
    var kidsSP by mutableStateOf("")
    var childNameSP by mutableStateOf("")
    var childAgeSP by mutableStateOf("")

    val signUpResponseCode: StateFlow<Int> = repository.signUpResponseCode

    private val _signUpResponse = MutableStateFlow(SignUpResponse())
    val signupResponse: StateFlow<SignUpResponse> = _signUpResponse

    private val _isSigningUp = MutableStateFlow(false)
    val isSigningUp: StateFlow<Boolean> = _isSigningUp

    private val _countryList = mutableStateOf(
        listOf(
            CountryCode(name = "India", dial_code = "+91", code = "IN", flag = "🇮🇳")
        )
    )
    val countryList: State<List<CountryCode>> = _countryList

    private val _selectedCountryCode = mutableStateOf(
        CountryCode(name = "India", dial_code = "+91", code = "IN", flag = "🇮🇳")
    )
    val selectedCountryCode: State<CountryCode> = _selectedCountryCode

    fun onCountrySelected(countryCode: CountryCode) {
        _selectedCountryCode.value = countryCode
    }

    fun signupUser(context: Context) {
        viewModelScope.launch {
            _isSigningUp.value = true
            try {
                //Log.d("signupchecking", "$parentNameSP email: $emailSP $phoneSP city: $citySP $kidsSP")
                repository.signUp(
                    name = NameSP,
                    email = emailSP,
                    countryCode = _selectedCountryCode.value.dial_code,
                    mobileNo = mobileNoSP,
                    context = context
                )
            } finally {
                _isSigningUp.value = false
            }
        }
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
                    _selectedCountryCode.value = (defaultCountry ?: countries.firstOrNull())!!

                    //Log.d("CountryDefault", "Selected: ${_selectedCountryCode.value}")
                } else {
                    //Log.e("CountryError", "Response error: ${response.code()}")
                }
            } catch (e: Exception) {
                //Log.e("CountryError", "Exception: ${e.message}")
            }
        }
    }

    fun initSharedPrefs(sharedPrefManager: SharedPrefManager) {
        sharedPrefManager.getFullName()?.takeIf { it.isNotEmpty() }?.let {
            NameSP = it
        }
        sharedPrefManager.getEmail()?.takeIf { it.isNotEmpty() }?.let {
            emailSP = it
        }
        sharedPrefManager.getPhone()?.takeIf { it.isNotEmpty() }?.let {
            mobileNoSP = it
        }
/*        sharedPrefManager.getCity()?.takeIf { it.isNotEmpty() }?.let {
            citySP = it
        }*/
      /*  sharedPrefManager.getKidsNum()?.takeIf { it.isNotEmpty() }?.let {
            kidsSP = it
        }*/

        // Restore Country Code
        sharedPrefManager.getCountryCode()?.let { savedCode ->
            val matchedCountry = _countryList.value.firstOrNull { it.dial_code == savedCode }
            matchedCountry?.let {
                _selectedCountryCode.value = it
            }
        }
    }
}


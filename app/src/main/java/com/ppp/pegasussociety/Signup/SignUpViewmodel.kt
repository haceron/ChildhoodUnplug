package com.ppp.pegasussociety.Signup

/*
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppp.pegasussociety.Model.SignupResponse
import com.ppp.pegasussociety.SharedPrefManager
import com.ppp.pegasussociety.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewmodel @Inject constructor(val repository: Repository)
    :ViewModel() {

    var parentNameSP by mutableStateOf("")
    //  var fullNameSP by mutableStateOf("")
    var countryCodeSP by mutableStateOf("")
    var phoneSP by mutableStateOf("")
    var emailSP by mutableStateOf("")
    var citySP by mutableStateOf("")
    var otpTxt by mutableStateOf("")
    var kidsSP by mutableStateOf("")
    var childNameSP by mutableStateOf("")
    var childAgeSP by mutableStateOf("")

    val signUpResponseCode: StateFlow<Int> = repository.signUpResponseCode
    val signUpResponse: StateFlow<SignupResponse> = repository.signUpResponse
    private val  _selectedCountry = MutableStateFlow("")
    val selectedCountry: StateFlow<String> = _selectedCountry

    fun signupUser(context: Context) {
        viewModelScope.launch {
            //Log.d("signupchecking", "$parentNameSP email: $emailSP ,phone: $phoneSP city: $citySP $kidsSP")
            repository.signUp(
                parentNameSP,
                citySP,
                emailSP,
                countryCodeSP,
                phoneSP,
                kidsSP,
                context
            )
        }
    }

    fun initSharedPrefs(sharedPrefManager: SharedPrefManager){
        if(sharedPrefManager.getFullName()!!.isNotEmpty()){
            parentNameSP = sharedPrefManager.getFullName()!!
        }
        if(sharedPrefManager.getEmail()!!.isNotEmpty()){
            emailSP = sharedPrefManager.getEmail()!!
        }
        if(sharedPrefManager.getPhone()!!.isNotEmpty()){
            phoneSP = sharedPrefManager.getPhone()!!
        }
       */
/* if(_selectedCountry.value.isEmpty()){
            _selectedCountry.value = sharedPrefManager.getCountryCode()!!
        }*//*

        if(sharedPrefManager.getCountryCode()!!.isNotEmpty()){
            countryCodeSP = sharedPrefManager.getCountryCode()!!
        }
        if(sharedPrefManager.getCity()!!.isNotEmpty()){
            citySP = sharedPrefManager.getCity()!!
        }
        if(sharedPrefManager.getKidsNum()!!.isNotEmpty()){
            kidsSP = sharedPrefManager.getKidsNum()!!
        }
    }

  */
/*  fun onSelectedCountryTextChanged(newText: String){
        _selectedCountry.value = newText

    }*//*


}*/


import SignupResponse
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppp.pegasussociety.CountryData.CountryCode
import com.ppp.pegasussociety.Repository.Repository
import com.ppp.pegasussociety.SharedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewmodel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var parentNameSP by mutableStateOf("")
    var phoneSP by mutableStateOf("")
    var emailSP by mutableStateOf("")
    var citySP by mutableStateOf("")
    var otpTxt by mutableStateOf("")
    var kidsSP by mutableStateOf("")
    var childNameSP by mutableStateOf("")
    var childAgeSP by mutableStateOf("")

    val signUpResponseCode: StateFlow<Int> = repository.signUpResponseCode

    private val _signUpResponse = MutableStateFlow(SignupResponse())
    val signupResponse: StateFlow<SignupResponse> = _signUpResponse

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
                    name = parentNameSP,
                  //  city = citySP,
                    email = emailSP,
                    mobileNo = phoneSP,
                    countryCode = _selectedCountryCode.value.dial_code,
                //    kids = kidsSP,
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
            parentNameSP = it
        }
        sharedPrefManager.getEmail()?.takeIf { it.isNotEmpty() }?.let {
            emailSP = it
        }
        sharedPrefManager.getPhone()?.takeIf { it.isNotEmpty() }?.let {
            phoneSP = it
        }
        // Restore Country Code
        sharedPrefManager.getCountryCode()?.let { savedCode ->
            val matchedCountry = _countryList.value.firstOrNull { it.dial_code == savedCode }
            matchedCountry?.let {
                _selectedCountryCode.value = it
            }
        }
    }
}

  /*  fun onSelectedCountryTextChanged(newText: String){
        _selectedCountry.value = newText

    }*/
    /*    private val _selectedCountry = MutableStateFlow("")
        val selectedCountry: StateFlow<String> = _selectedCountry*/



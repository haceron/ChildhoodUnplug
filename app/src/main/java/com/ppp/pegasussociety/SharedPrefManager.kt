package com.ppp.pegasussociety

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.ppp.pegasussociety.Login.VerifyData
import javax.inject.Inject


class SharedPrefManager @Inject constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun saveAll(data: VerifyData) {
        editor.putString("id", data.id)
        editor.putString("parent_name", data.name)
        editor.putString("mobileno", data.phoneNumber)
        editor.putString("email", data.email)
  /*      editor.putInt("saveKidsNum", data.kids)
        editor.putString("city", data.city)
        editor.putString("role", data.role)*/
        editor.putString("createdAt", data.createdAt)
        editor.putString("updatedAt", data.updatedAt)
        editor.putString("countryCode", data.countryCode)
        editor.apply()
    }

    fun saveLoginStatus(isLogin: Boolean) {
        editor.putBoolean("loginStatus", isLogin)
        editor.apply()
    }

    fun getLoginStatus(): Boolean {
        return sharedPreferences.getBoolean("loginStatus", false)
    }

    fun saveID(parentId: String) {
        editor.putString("parentID", parentId)
        editor.apply()
    }

    fun getID(): String? {
        return sharedPreferences.getString("parentID", "")
    }

    fun saveFullName(fullName: String) {
        editor.putString("fullName", fullName)
        editor.apply()
    }

    fun getFullName(): String? {
        return sharedPreferences.getString("fullName", "")
    }

    fun savePhone(phone: String) {
        editor.putString("phone", phone)
        editor.apply()
    }

    fun getPhone(): String? {
        return sharedPreferences.getString("phone", "")
    }

    fun saveEmail(email: String) {
        editor.putString("email", email)
        editor.apply()
    }

    fun getEmail(): String? {
        return sharedPreferences.getString("email", "")
    }

    fun saveCountryCode(code: String) {
        editor.putString("countryCode", code)
        editor.apply()
    }

    fun getCountryCode(): String? {
        return sharedPreferences.getString("countryCode", "")
    }


}
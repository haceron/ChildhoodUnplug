package com.ppp.pegasussociety.Login

import com.msg91.sendotp.OTPWidget

class OTPRepository {
    suspend fun sendOTP(widgetId: String, token: String, identifier: String): String {
        return OTPWidget.sendOTP(widgetId, token, identifier)
    }

    suspend fun retryOTP(widgetId: String, token: String, reqId: String, channel: Number): String {
        return OTPWidget.retryOTP(widgetId, token, reqId, channel)
    }

    suspend fun verifyOTP(widgetId: String, token: String, reqId: String, otp: String): String {
        return OTPWidget.verifyOTP(widgetId, token, reqId, otp)
    }

    suspend fun getWidgetProcess(widgetId: String, token: String): String {
        return OTPWidget.getWidgetProcess(widgetId, token)
    }
}

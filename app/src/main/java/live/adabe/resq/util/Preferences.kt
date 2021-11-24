package live.adabe.resq.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class Preferences(application: Application) {
    private var sharedPreferences: SharedPreferences

    companion object {
        private const val SHARED_PREF_AUTH_NAME = "AuthPref"
        private const val IS_SMS_PERMISSION_GRANTED = "sms"
        private const val IS_LOCATION_PERMISSION_GRANTED = "location"
        private const val EMERGENCY_CONTACT_1 = "first-contact"
        private const val EMERGENCY_CONTACT_2 = "second-contact"
    }

    init {
        sharedPreferences =
            application.getSharedPreferences(SHARED_PREF_AUTH_NAME, Context.MODE_PRIVATE)
    }

    fun getIsSmsPermissionGranted(): Boolean =
        sharedPreferences.getBoolean(IS_SMS_PERMISSION_GRANTED, false)

    fun getIsLocationPermissionGranted(): Boolean =
        sharedPreferences.getBoolean(IS_LOCATION_PERMISSION_GRANTED, false)

    fun getContactOne(): String? = sharedPreferences.getString(EMERGENCY_CONTACT_1, "")

    fun getContactTwo(): String? = sharedPreferences.getString(EMERGENCY_CONTACT_2, "")

    fun setIsSmsPermissionGranted(booleanGranted: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(IS_SMS_PERMISSION_GRANTED, booleanGranted)
            apply()
        }
    }

    fun setIsLocationPermissionGranted(booleanGranted: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(IS_LOCATION_PERMISSION_GRANTED, booleanGranted)
            apply()
        }
    }

    fun setContactOne(number: String){
        with(sharedPreferences.edit()){
            putString(EMERGENCY_CONTACT_1, number)
            apply()
        }
    }

    fun setContactTwo(number: String){
        with(sharedPreferences.edit()){
            putString(EMERGENCY_CONTACT_2, number)
            apply()
        }
    }
}
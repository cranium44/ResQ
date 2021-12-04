package live.adabe.resq.util

import android.app.Application
import android.location.Location
import android.telephony.SmsManager
import android.widget.Toast
import live.adabe.resq.R
import timber.log.Timber
import javax.inject.Inject


class MessageUtil @Inject constructor(
    private var application: Application,
    private var preferences: Preferences
) {

    fun sendText(location: Location) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            preferences.getContactOne()?.let { contact ->
                smsManager.sendTextMessage(
                    "911",
                    null,
                    application.getString(
                        R.string.emergency_text,
                        location.latitude.toInt(),
                        location.longitude.toInt()
                    ), null, null
                )
                Toast.makeText(
                    application, "Your Message Sent",
                    Toast.LENGTH_LONG
                ).show()
            } ?: kotlin.run {
                preferences.getContactTwo()?.let { contact2 ->
                    smsManager.sendTextMessage(
                        contact2, null, application.getString(
                            R.string.emergency_text,
                            location.latitude,
                            location.longitude
                        ), null, null
                    )
                    Toast.makeText(
                        application, "Your Message Sent",
                        Toast.LENGTH_LONG
                    ).show()
                } ?: run {
                    Toast.makeText(
                        application, "No emergency contact found",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(
                application, ex.message.toString(),
                Toast.LENGTH_LONG
            ).show()
            Timber.d(ex.message)
        }
    }
}
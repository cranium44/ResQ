package live.adabe.resq.util

import android.app.Application
import android.telephony.SmsManager
import android.widget.Toast
import live.adabe.resq.R
import live.adabe.resq.model.LocationDTO
import timber.log.Timber
import javax.inject.Inject


class MessageUtil @Inject constructor(
    private var application: Application,
    private var preferences: Preferences
) {

    fun sendText(location: LocationDTO?) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            preferences.getContactOne()?.let { contact ->
                if (location != null) {
                    smsManager.sendTextMessage(
                        contact,
                        null,
                        application.getString(
                            R.string.emergency_text,
                            location.latitude.toString(),
                            location.longitude.toString()
                        ), null, null
                    )
                }
                Toast.makeText(
                    application, "Your Message Sent",
                    Toast.LENGTH_LONG
                ).show()
            } ?: kotlin.run {
                preferences.getContactTwo()?.let { contact2 ->
                    if (location != null) {
                        smsManager.sendTextMessage(
                            contact2, null, application.getString(
                                R.string.emergency_text,
                                location.latitude.toString(),
                                location.longitude.toString()
                            ), null, null
                        )
                    }
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
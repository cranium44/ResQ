package live.adabe.resq.util

import android.app.Application
import android.content.Context
import android.location.Location
import android.telephony.SmsManager
import android.widget.Toast
import javax.inject.Inject


class MessageUtil @Inject constructor(private var application: Application) {

    fun sendText(location: Location){
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(
                "911",
                null,
                "strMessage", null, null)
            Toast.makeText(
                application, "Your Message Sent",
                Toast.LENGTH_LONG
            ).show()
        } catch (ex: Exception) {
            Toast.makeText(
                application, ex.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
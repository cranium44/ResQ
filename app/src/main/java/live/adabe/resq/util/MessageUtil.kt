package live.adabe.resq.util

import android.content.Context
import android.location.Location
import android.telephony.SmsManager
import android.widget.Toast


object MessageUtil {

    fun sendText(context: Context, location: Location){
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(
                "911",
                null,
                "strMessage", null, null)
            Toast.makeText(
                context, "Your Message Sent",
                Toast.LENGTH_LONG
            ).show()
        } catch (ex: Exception) {
            Toast.makeText(
                context, ex.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
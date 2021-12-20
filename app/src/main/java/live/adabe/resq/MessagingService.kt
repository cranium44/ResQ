package live.adabe.resq

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import androidx.localbroadcastmanager.content.LocalBroadcastManager




class MessagingService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path.equals("/my_path")) {


            val message = String(messageEvent.data)
            val messageIntent = Intent()
            messageIntent.action = Intent.ACTION_SEND
            messageIntent.putExtra("message", message)
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent)
        } else {
            super.onMessageReceived(messageEvent)
        }
    }
}
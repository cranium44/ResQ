package live.adabe.resqwears

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService


class MessagingService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        //If the message’s path equals "/my_path"...//

        //If the message’s path equals "/my_path"...//
        if (messageEvent.path.equals("/my_path")) {

//...retrieve the message//
            val message = String(messageEvent.data)
            val messageIntent = Intent()
            messageIntent.action = Intent.ACTION_SEND
            messageIntent.putExtra("message", message)

//Broadcast the received Data Layer messages locally//
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent)
        } else {
            super.onMessageReceived(messageEvent)
        }
    }
}
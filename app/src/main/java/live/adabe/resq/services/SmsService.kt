package live.adabe.resq.services

import android.app.AlarmManager
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import dagger.hilt.android.AndroidEntryPoint
import live.adabe.resq.model.LocationDTO
import live.adabe.resq.util.MessageUtil
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SmsService: Service() {

    @Inject
    lateinit var messageUtil: MessageUtil


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        sendSMS(intent)
    }

    private fun sendSMS(intent: Intent?){
        intent?.let {
            messageUtil.sendText(it.getParcelableExtra("location"))
        }
    }
}
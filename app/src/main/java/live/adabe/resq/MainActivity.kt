package live.adabe.resq

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import live.adabe.resq.databinding.ActivityMainBinding
import live.adabe.resq.util.Preferences

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: Preferences

    val SMS_PERMISSION_CONSTANT = 100
    val LOCATION_PERMISSION_CONSTANT = 102
    val REQUEST_PERMISSION_SETTING = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(application)

        preferences.setIsSmsPermissionGranted(ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            AlertDialog.Builder(applicationContext).setTitle("Need SMS Permissions")
                .setMessage("This app needs permission to send sms to your emergency contact on your behalf.")
                .setPositiveButton(
                    "Grant permission"
                ) { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.SEND_SMS),
                        SMS_PERMISSION_CONSTANT
                    )
                }.setNegativeButton("Cancel"){ dialog, _ -> dialog.cancel()}.show()
        }else if(preferences.getIsSmsPermissionGranted()){
            AlertDialog.Builder(applicationContext).setTitle("Need SMS Permissions")
                .setMessage("This app needs permission to send sms to your emergency contact on your behalf.")
                .setPositiveButton(
                    "Grant permission"
                ) { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.SEND_SMS),
                        SMS_PERMISSION_CONSTANT
                    )
                }.setNegativeButton("Cancel"){ dialog, _ -> dialog.cancel()}.show()
        }
    }


}
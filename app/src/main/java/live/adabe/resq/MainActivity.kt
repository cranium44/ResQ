package live.adabe.resq

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint
import live.adabe.resq.databinding.ActivityMainBinding
import live.adabe.resq.navigation.NavigationService
import live.adabe.resq.util.Preferences
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var preferences: Preferences

    @Inject
    lateinit var navigationService: NavigationService

    val SMS_PERMISSION_CONSTANT = 100
    val LOCATION_PERMISSION_CONSTANT = 102
    val REQUEST_PERMISSION_SETTING = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationService.attachToActivity(this)
        preferences.run {
            setIsLocationPermissionGranted(
                ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )

            setIsSmsPermissionGranted(ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!preferences.getIsLocationPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_CONSTANT
            )
        }
        if (!preferences.getIsSmsPermissionGranted()){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CONSTANT)
        }

        if (!preferences.getIsRegistered()){
            navigationService.openSignupScreen()
        }else{
            navigationService.openSignupScreen()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navigationService.detachFromActivity()
    }

}
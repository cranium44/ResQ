package live.adabe.resq

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint
import live.adabe.resq.databinding.ActivityMainBinding
import live.adabe.resq.navigation.NavigationService
import live.adabe.resq.util.Preferences
import live.adabe.resq.util.hasPermissions
import javax.inject.Inject


@Suppress("PrivatePropertyName")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var preferences: Preferences

    @Inject
    lateinit var navigationService: NavigationService
    private val REQUEST_PERMISSION_SETTING = 101

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest
            .permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationService.attachToActivity(this)
        checkAndResetPermissions()
        preferences.run {
            setIsLocationPermissionGranted(
                ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )

            setIsSmsPermissionGranted(
                ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

    }

    private fun checkAndResetPermissions() {
        if (!hasPermissions(this, *permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_SETTING)
        } else {
            if (!preferences.getIsRegistered()) {
                navigationService.openSignupScreen()
            } else {
                navigationService.openHomeScreen()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_SETTING && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissions.forEach { permission ->
                when (permission) {
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                    }
                    Manifest.permission.SEND_SMS -> {
                    }
                }
            }
            if (!preferences.getIsRegistered()) {
                navigationService.openSignupScreen()
            } else {
                navigationService.openHomeScreen()
            }
        }
    }




    override fun onDestroy() {
        super.onDestroy()
        navigationService.detachFromActivity()
    }

}
package live.adabe.resqwears

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import live.adabe.resqwears.databinding.ActivityMainBinding
import live.adabe.resqwears.util.hasPermissions

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    private val PERMISSION_CODE = 100

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest
            .permission.ACCESS_COARSE_LOCATION, Manifest.permission.BODY_SENSORS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!hasPermissions(this, *permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }){
            Snackbar.make(binding.root, "Permissions granted", Snackbar.LENGTH_SHORT).show()
        }
    }
}
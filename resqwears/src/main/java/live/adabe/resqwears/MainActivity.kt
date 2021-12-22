package live.adabe.resqwears

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import live.adabe.resqwears.databinding.ActivityMainBinding
import live.adabe.resqwears.util.hasPermissions
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import android.content.Intent

import android.content.IntentFilter

import android.content.BroadcastReceiver
import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import java.util.concurrent.ExecutionException


class MainActivity : Activity(), DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val PERMISSION_CODE = 100
        private const val DATA_MAP_KEY = "message"
    }

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

        val putDataMapRequest = PutDataMapRequest.create("/resq")
        putDataMapRequest.dataMap.putString(DATA_MAP_KEY, "Test message for handheld")
        val putDataRequest = putDataMapRequest.asPutDataRequest()
        val pendingResults: PendingResult<DataApi.DataItemResult> =
            Wearable.DataApi.putDataItem(GoogleApiClient.Builder(this).addApi(Wearable.API).build(),
                putDataRequest)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            Snackbar.make(binding.root, "Permissions granted", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDataChanged(p0: DataEventBuffer) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


}
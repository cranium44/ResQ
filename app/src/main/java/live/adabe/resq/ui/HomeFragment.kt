package live.adabe.resq.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import live.adabe.resq.MainActivity
import live.adabe.resq.R
import live.adabe.resq.databinding.HomeFragmentBinding
import live.adabe.resq.util.AppRepository
import live.adabe.resq.util.MessageUtil
import timber.log.Timber
import java.util.concurrent.ExecutionException
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), DataApi.DataListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var provider: FusedLocationProviderClient
    private lateinit var apiClient: GoogleApiClient


    companion object {
        private const val LOCATION_PERMISSION_CONSTANT = 101
        private const val SMS_PERMISSION_CONSTANT = 102
    }

    @Inject
    lateinit var messageUtil: MessageUtil

    @Inject
    lateinit var repository: AppRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        provider = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.welcomeText.text = getString(R.string.welcome_text, repository.getUserName())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            provider.locationAvailability.addOnSuccessListener {
                if (it.isLocationAvailable) {
                    provider.lastLocation.addOnSuccessListener { location ->
                        binding.textView.text = getString(
                            R.string.emergency_text,
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
                        .ACCESS_COARSE_LOCATION
                ), LOCATION_PERMISSION_CONSTANT
            )
        }

        apiClient = GoogleApiClient.Builder(requireContext()).addApi(Wearable.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        binding.button.setOnClickListener {
            provider.lastLocation.addOnSuccessListener { location ->
                messageUtil.sendText(location)
            }
        }

        binding.wearBtn.setOnClickListener {

        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDataChanged(dataEventBuffer: DataEventBuffer) {
        dataEventBuffer.forEach { dataEvent ->  
            if (dataEvent.type == DataEvent.TYPE_CHANGED){
                val dataItem = dataEvent.dataItem
                if (dataItem.uri.path?.compareTo("/resq") ?: 1  == 0){
                    val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                    Toast.makeText(requireContext(), dataMap.getString("message"), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        Wearable.DataApi.addListener(apiClient, this)
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
}
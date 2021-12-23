package live.adabe.resq.ui

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.wearable.*
import dagger.hilt.android.AndroidEntryPoint
import live.adabe.resq.R
import live.adabe.resq.databinding.HomeFragmentBinding
import live.adabe.resq.services.SmsService
import live.adabe.resq.util.AppRepository
import live.adabe.resq.util.MessageUtil
import live.adabe.resq.util.convertToDto
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

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
                val interval: Long = 1 * 60 * 1000
                val intent = Intent(requireContext(), SmsService::class.java).putExtra(
                    "location",
                    convertToDto(location)
                )
                requireActivity().startService(intent)

                val alarmManager =
                    requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
                alarmManager.setRepeating(
                    AlarmManager.RTC, interval, AlarmManager.INTERVAL_HOUR,
                    pendingIntent
                )
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private class MyAlarm : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            Timber.d("Alarm Bell", "Alarm just fired")
        }
    }
}
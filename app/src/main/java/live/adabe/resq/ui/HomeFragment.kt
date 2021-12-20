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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
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
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var provider: FusedLocationProviderClient


    private lateinit var myHandler: Handler
    var receivedMessageNumber = 1
    var sentMessageNumber = 1

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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

        myHandler = Handler { msg ->
            val stuff = msg.data
            stuff.getString("messageText")?.let {
                Snackbar.make(
                    binding.root, it, Snackbar
                        .LENGTH_SHORT
                ).show()
            }
            true
        }

        //Register to receive local broadcasts, which we'll be creating in the next step//
        val messageFilter = IntentFilter(Intent.ACTION_SEND)
        val messageReceiver = HomeFragment().Receiver()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(messageReceiver, messageFilter)


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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        binding.button.setOnClickListener {
            provider.lastLocation.addOnSuccessListener { location ->
                messageUtil.sendText(location)
            }
        }

        binding.wearBtn.setOnClickListener {
            talkClick(it)
        }
    }

    //Define a nested class that extends BroadcastReceiver//
    inner class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

//Upon receiving each message from the wearable, display the following text//
            val message = "I just received a message from the wearable ${receivedMessageNumber++} "
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun talkClick(v: View?) {
        val message = "Sending message.... "
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

        //Sending a message can block the main UI thread, so use a new thread//
        NewThread("/my_path", message).start()
    }


    //Use a Bundle to encapsulate our message//
    fun sendmessage(messageText: String?) {
        val bundle = Bundle()
        bundle.putString("messageText", messageText)
        val msg: Message = myHandler.obtainMessage()
        msg.data = bundle
        myHandler.sendMessage(msg)
    }


    inner class NewThread(var path: String, var message: String) : Thread() {
        override fun run() {

//Retrieve the connected devices, known as nodes//
            val wearableList: Task<List<Node>> =
                Wearable.getNodeClient(requireContext()).connectedNodes
            try {
                val nodes: List<Node> = Tasks.await(wearableList)
                for (node in nodes) {
                    val sendMessageTask: Task<Int> =  //Send the message//
                        Wearable.getMessageClient(requireActivity())
                            .sendMessage(node.id, path, message.toByteArray())
                    try {

                        //Block on a task and get the result synchronously//
                        val result: Int = Tasks.await(sendMessageTask)
                        sendmessage("I just sent the wearable a message " + sentMessageNumber++)

                        //if the Task fails, then…..//
                    } catch (exception: ExecutionException) {

                        //TO DO: Handle the exception//
                    } catch (exception: InterruptedException) {

                        //TO DO: Handle the exception//
                    }
                }
            } catch (exception: ExecutionException) {

                //TO DO: Handle the exception//
            } catch (exception: InterruptedException) {

                //TO DO: Handle the exception//
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
}
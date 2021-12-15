package live.adabe.resq.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import live.adabe.resq.R
import live.adabe.resq.databinding.FragmentSignupBinding
import live.adabe.resq.navigation.NavigationService
import live.adabe.resq.ui.HomeViewModel
import live.adabe.resq.util.Preferences
import live.adabe.resq.util.Validator
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : Fragment() {


    @Inject
    lateinit var preferences: Preferences
    @Inject
    lateinit var navigationService: NavigationService

    lateinit var viewModel: HomeViewModel

    private lateinit var binding: FragmentSignupBinding

    private var name = ""
    private var contactOne = ""
    private var contactTwo = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        initViews()
        return binding.root
    }

    private fun initViews(){
        binding.apply {
            contactOneEt.editText?.addTextChangedListener{
                val contact = it.toString()
                when{
                    contact.isNullOrEmpty() -> {
                        binding.contactOneEt.error = getString(R.string.empty_field_error)
                    }

                    !Validator.isValidPhone(contact) -> {
                        binding.contactOneEt.error = getString(R.string.phone_error_text)
                    }
                    else -> {
                        contactOne = contact
                        binding.contactOneEt.isErrorEnabled = false
                    }
                }
            }
            contactTwoEt.editText?.addTextChangedListener {
                val contact = it.toString()
                when{
                    contact.isNullOrEmpty() -> {
                        binding.contactTwoEt.error = getString(R.string.empty_field_error)
                    }

                    !Validator.isValidPhone(contact) -> {
                        binding.contactTwoEt.error = getString(R.string.phone_error_text)
                    }
                    else -> {
                        contactTwo = contact
                        binding.contactTwoEt.isErrorEnabled = false
                    }
                }
            }
            nameEt.editText?.addTextChangedListener{s->
                when  {
                    s!!.trim().isNotEmpty() ->{
                        name = s.toString()
                        binding.nameEt.isErrorEnabled = false
                    }
                    else -> binding.nameEt.error = getString(R.string.empty_field_error)
                }
            }

            submitBtn.setOnClickListener {
                getInput()
            }
        }
    }

    private fun getInput(){
        if (contactOne.isEmpty()|| contactTwo.isEmpty()|| name.isEmpty()) return
        viewModel.saveUserInfo(name, contactOne, contactTwo)
        preferences.setIsRegistered(true)
        navigationService.openHomeScreen()
    }
}
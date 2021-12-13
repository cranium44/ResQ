package live.adabe.resq.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import live.adabe.resq.R
import live.adabe.resq.databinding.FragmentSignupBinding
import live.adabe.resq.util.Validator

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        binding.apply {
            contactOneEt.editText?.addTextChangedListener(watcher)
            contactTwoEt.editText?.addTextChangedListener(watcher)
            nameEt.editText?.addTextChangedListener{

            }
        }
        return binding.root
    }

    private val watcher =  object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
}
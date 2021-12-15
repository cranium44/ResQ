package live.adabe.resq.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import live.adabe.resq.ui.HomeFragment
import live.adabe.resq.ui.signup.SignupFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {

    class SignupScreen(private val bundle: Bundle?) : SupportAppScreen(){
        override fun getFragment(): Fragment {
            return SignupFragment()
        }
    }

    class HomeScreen(private val bundle: Bundle?) : SupportAppScreen(){
        override fun getFragment(): Fragment {
            return HomeFragment()
        }
    }
}
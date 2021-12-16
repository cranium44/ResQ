package live.adabe.resq.navigation

import android.content.Context
import android.os.Bundle
import live.adabe.resq.MainActivity
import live.adabe.resq.R
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class NavigationService @Inject constructor( cicerone: Cicerone<Router>) {
    private val router: Router = cicerone.router
    private val navigatorHolder: NavigatorHolder = cicerone.navigatorHolder


    fun openSignupScreen(bundle: Bundle? = null) {
        replaceScreen(Screens.SignupScreen(bundle))
    }

    fun openHomeScreen(bundle: Bundle? = null) {
        replaceScreen(Screens.HomeScreen(bundle))
    }


    fun attachToActivity(context: Context) {
        context as MainActivity
        navigatorHolder.setNavigator(SupportAppNavigator(context, R.id.fragmentContainerView))
    }

    fun detachFromActivity() {
        navigatorHolder.removeNavigator()
    }

    private fun replaceScreen(screen: SupportAppScreen) {
        router.replaceScreen(screen)
    }
}
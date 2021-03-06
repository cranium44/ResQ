package live.adabe.resq.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import live.adabe.resq.util.AppRepository
import live.adabe.resq.util.Preferences
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private var appRepository: AppRepository) : ViewModel() {


    fun saveUserInfo(name: String, contactOne: String, contactTwo: String){
        appRepository.run {
            saveFirstContact(contactOne)
            saveSecondContact(contactTwo)
            saveUserName(name)
        }
    }

}
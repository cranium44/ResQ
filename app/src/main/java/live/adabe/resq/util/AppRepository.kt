package live.adabe.resq.util

import javax.inject.Inject

class AppRepository @Inject constructor(private val preferences: Preferences){

    fun saveUserName(userName: String){
        preferences.setUserName(userName)
    }

    fun saveFirstContact(firstContact: String){
        preferences.setContactOne(firstContact)
    }

    fun saveSecondContact(secondContact: String){
        preferences.setContactTwo(secondContact)
    }

    fun getUserName() = preferences.getUserName() ?: ""

    fun getContactOne() = preferences.getContactOne()

    fun getContactTwo() = preferences.getContactTwo()
}
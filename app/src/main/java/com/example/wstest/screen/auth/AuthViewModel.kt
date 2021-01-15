package com.example.wstest.screen.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.wstest.database.AppDatabase
import com.example.wstest.database.dao.UserDao
import com.example.wstest.database.dao.UserImageDao
import com.example.wstest.database.entiry.UserEntity
import com.example.wstest.database.entiry.UserImageEntity
import com.example.wstest.screen.auth.model.UserAndImage
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("SENSELESS_COMPARISON")
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao: UserDao = AppDatabase.getInstance(application).userDao()
    private val userImageDao: UserImageDao = AppDatabase.getInstance(application).userImageDao()

    private val registerSuccessLiveEvent = LiveEvent<Boolean>()
    val registerSuccessLiveData: LiveData<Boolean> = registerSuccessLiveEvent
    private val loginSuccessLiveEvent = LiveEvent<Boolean>()
    val loginSuccessLiveData: LiveData<Boolean> = loginSuccessLiveEvent
    val userAndImageLiveData = MutableLiveData<UserAndImage>()

    fun register(userEntity: UserEntity, userImageEntity: UserImageEntity) {
        viewModelScope.launch {
            val user = userDao.findUser(userEntity.userName)
            if (user != null) {
                registerSuccessLiveEvent.postValue(false)
            } else {
                withContext(Dispatchers.IO) {
                    userDao.register(userEntity)
                    userImageDao.addUserImage(userImageEntity)
                }

                registerSuccessLiveEvent.postValue(true)
            }
        }
    }

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            val user = userDao.login(userName, password)
            if (user != null) {
                withContext(Dispatchers.IO) {
                    val image = userImageDao.findUserImage(userName)
                    userAndImageLiveData.postValue(UserAndImage(user, image))
                }
            } else {
                loginSuccessLiveEvent.postValue(false)
            }
        }
    }
}
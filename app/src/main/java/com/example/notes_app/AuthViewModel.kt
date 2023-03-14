package com.example.notes_app

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes_app.models.UserRequest
import com.example.notes_app.models.UserResponse
import com.example.notes_app.repository.UserRepository
import com.example.notes_app.utils.Constants.TAG
import com.example.notes_app.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.loginUser(userRequest)
        }
    }

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.registerUser(userRequest)
        }
    }

    fun validateUser(email: String, password: String, username: String, isLogin: Boolean):
            Pair<Boolean, String> {
        var result = Pair(true, "")

        if ((!isLogin && TextUtils.isEmpty(username))
            || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
        ) {
            result = Pair(false, "Please provide necessary the credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result = Pair(false, "Please provide valid email")
        } else if (password.length < 5) {
            result = Pair(false, "password length should be greater than 5")
        }
        Log.e(TAG, "validateUser: result$result")
        return result
    }
}
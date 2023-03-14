package com.example.notes_app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notes_app.api.UserApi
import com.example.notes_app.models.UserRequest
import com.example.notes_app.models.UserResponse
import com.example.notes_app.utils.Constants.TAG
import com.example.notes_app.utils.NetworkResult
import com.example.notes_app.utils.ResponseHandler
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


class UserRepository @Inject constructor(private val userApi: UserApi) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData
    private val responseHandler = ResponseHandler()
    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signUp(userRequest)
        Log.e(TAG, "registerUser: ${response.body()}" )
//        handleResponse(response)
        _userResponseLiveData.postValue(responseHandler.handleUserResponse(response))
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signIn(userRequest)
        Log.e(TAG, "loginUser: ${response.body()}" )
//        handleResponse(response)
        _userResponseLiveData.postValue(responseHandler.handleUserResponse(response))
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            // so retrofit only parse successful result and not the error
            //so for that we have to manual parse json
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}
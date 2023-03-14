package com.example.notes_app.utils

import org.json.JSONObject
import retrofit2.Response

class ResponseHandler {
    fun<T> handleUserResponse( response: Response<T>): NetworkResult<T> {
        return if (response.isSuccessful && response.body() != null) {
            NetworkResult.Success(response.body()!!)
        } else if (response.errorBody() != null) {
            // so retrofit only parse successful result and not the error
            //so for that we have to manual parse json
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            NetworkResult.Error(errorObj.getString("message"))
        } else {
            NetworkResult.Error("Something went wrong")
        }
    }
}
package com.example.notes_app.api

import com.example.notes_app.models.UserRequest
import com.example.notes_app.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/users/signup")
    // in post we send body and that can be done with @Body
    suspend fun signUp(@Body userRequest: UserRequest):Response<UserResponse>

    @POST("/users/signin")
    suspend fun signIn(@Body userRequest: UserRequest):Response<UserResponse>
}
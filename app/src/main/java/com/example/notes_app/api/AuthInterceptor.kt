package com.example.notes_app.api

import com.example.notes_app.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// the OkHttp interceptor is used when we want to add header by default to every request rather than adding
// header to every request via retrofit @Header annotation

// so basically what this will do is before sending the request it will add header to that request

class AuthInterceptor @Inject constructor() : Interceptor {
    @Inject
    lateinit var tokenManager: TokenManager
    override fun intercept(chain: Interceptor.Chain): Response {
        // so what we are doing her is that first we are getting existing request
        // and then we are adding header to that request.
        val request = chain.request().newBuilder()
        val token = tokenManager.getToken()

        // so 'Bearer' before token is api's requirement
        request.addHeader("Authorization", "Bearer $token")

        // and then we are telling it to proceed as we have done our work
        return chain.proceed(request.build())
    }
}
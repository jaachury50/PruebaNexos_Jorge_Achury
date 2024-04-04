package com.example.pruebanexos.network

import com.example.pruebanexos.models.AuthorizationModel
import com.example.pruebanexos.models.AuthorizationResponseModel
import com.example.pruebanexos.models.CancelAuthorizationModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface WebService {
    @POST("authorization")
    suspend fun consumeApi(@Header("Authorization") type: String, @Body request: AuthorizationModel): Response<AuthorizationResponseModel>

    @POST("annulment")
    suspend fun consumeApiAnnulation(@Header("Authorization") type: String, @Body request: CancelAuthorizationModel): Response<AuthorizationResponseModel>
}
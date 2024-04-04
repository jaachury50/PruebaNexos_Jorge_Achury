package com.example.pruebanexos.models

import com.google.gson.annotations.SerializedName

data class AuthorizationResponseModel(
    @SerializedName("receiptId")
    val receiptId: String = "",
    @SerializedName("rrn")
    val rrn: String = "",
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusDescription")
     val statusDescription: String,
)
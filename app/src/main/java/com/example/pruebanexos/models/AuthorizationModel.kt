package com.example.pruebanexos.models

import com.google.gson.annotations.SerializedName

data class AuthorizationModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("commerceCode")
    val commerceCode: String,
    @SerializedName("terminalCode")
    val terminalCode: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("card")
    val card: String
)

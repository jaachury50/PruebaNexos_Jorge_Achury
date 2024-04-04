package com.example.pruebanexos.models


import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class ListAdapterAuthorizationModel(
    @SerializedName("receiptId")
    val receiptId: String,
    @SerializedName("rrn")
    val rrn: String,
    @SerializedName("commerceCode")
    val commerceCode: String,
    @SerializedName("terminalCode")
    val terminalCode: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("card")
    val card: String,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusDescription")
    val statusDescription: String,
    @SerializedName("approvedStatus")
    val approvedStatus: Boolean
) : Serializable
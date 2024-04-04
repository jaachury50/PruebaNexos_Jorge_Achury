package com.example.pruebanexos.models

import com.google.gson.annotations.SerializedName

data class CancelAuthorizationModel(
    @SerializedName("commerceCode")
    var commerceCode: String,
    @SerializedName("terminalCode")
    var terminalCode: String,
    @SerializedName("receiptId")
    var receiptId: String,
    @SerializedName("rrn")
    var rrn: String,
)
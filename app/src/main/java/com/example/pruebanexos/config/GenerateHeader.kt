package com.example.pruebanexos.config

import android.util.Base64


object GenerateHeader{
    fun generateBase64(text: String): String{
        val byteArr = text.toByteArray()
        val encodedText = Base64.encodeToString(byteArr, Base64.NO_WRAP)
        return encodedText
    }
}
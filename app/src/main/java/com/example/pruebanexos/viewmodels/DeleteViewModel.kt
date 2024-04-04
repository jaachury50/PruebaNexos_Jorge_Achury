package com.example.pruebanexos.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebanexos.config.GenerateHeader
import com.example.pruebanexos.config.StartApp
import com.example.pruebanexos.models.AuthorizationResponseModel
import com.example.pruebanexos.models.CancelAuthorizationModel
import com.example.pruebanexos.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class DeleteViewModel: ViewModel() {

    private var _showNotification = MutableLiveData<AuthorizationResponseModel>()
    private var approvedStatus: Boolean = true
    private var _showAlert = MutableLiveData<Boolean>()
    private var _typeAlert = MutableLiveData<Int>()
    private var _textAlert = MutableLiveData<String>()
    val showNotification: LiveData<AuthorizationResponseModel> = _showNotification
    var showAlert: LiveData<Boolean> = _showAlert
    var textAlert: LiveData<String> = _textAlert
    var typeAlert: LiveData<Int> = _typeAlert

    fun cancelAuthorization(request: CancelAuthorizationModel){
        val header = "Basic " + GenerateHeader.generateBase64(request.commerceCode + request.terminalCode)
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = RetrofitClient.webService.consumeApiAnnulation(header, request)
                if (response.code().toString() == "202" || response.code().toString() == "200") {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            withContext(Dispatchers.Main) {
                                _showNotification.value = response.body()!!
                                updateAuthorization(response.body()!!, request.receiptId)
                            }
                        }
                    }
                } else {
                    _textAlert.postValue("Hubo un error en la petición verifique la información")
                    _typeAlert.postValue(0)
                    _showAlert.postValue(true)
                }
            }catch (e: IOException) {
                _textAlert.postValue("Hubo un error en la petición: ${e.message}")
                _typeAlert.postValue(0)
                _showAlert.postValue(true)
            }
        }
    }

    private fun updateAuthorization(response: AuthorizationResponseModel, id: String){
        if(response.statusCode == "00"){
            approvedStatus = false
        }
        viewModelScope.launch(Dispatchers.IO){
            val results = StartApp.db.authorizationDao().deleteAuthorization(
                approvedStatus = approvedStatus,
                receipId = id
            )
            if (results > 0){
                _textAlert.postValue("Se anulo de forma correcta la transacción.")
                _typeAlert.postValue(1)
                _showAlert.postValue(true)
            }else{
                _textAlert.postValue("Hubo un error en la anulación.")
                _typeAlert.postValue(0)
                _showAlert.postValue(true)
            }
        }
    }
}
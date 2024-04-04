package com.example.pruebanexos.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebanexos.config.GenerateHeader
import com.example.pruebanexos.config.StartApp.Companion.db
import com.example.pruebanexos.database.entities.AuthorizationEntity
import com.example.pruebanexos.models.AuthorizationModel
import com.example.pruebanexos.models.AuthorizationResponseModel
import com.example.pruebanexos.network.RetrofitClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class AuthorizationViewModel:ViewModel() {

    private var _showAlert = MutableLiveData<Boolean>()
    private var _showError = MutableLiveData<Boolean>()
    private var _textAlert = MutableLiveData<String>()
    private var _textError = MutableLiveData<String>()
    private var _typeAlert = MutableLiveData<Int>()

    var showAlert: LiveData<Boolean> = _showAlert
    var textAlert: LiveData<String> = _textAlert
    var showError: LiveData<Boolean> = _showError
    var textError: LiveData<String> = _textError
    var typeAlert: LiveData<Int> = _typeAlert

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->

    }

    fun getAuthorizations(request: AuthorizationModel) {

        if (validateInfo(request)){
            val header = "Basic " + GenerateHeader.generateBase64(request.commerceCode + request.terminalCode)
            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                try {
                    val response = RetrofitClient.webService.consumeApi(header, request)
                    if(response.code().toString() == "202" || response.code().toString() == "200"){
                        if (response.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                saveAuthorization(response.body()!!, request, true)
                                _textAlert.postValue("Su transacción ha sido aprobada correctamente")
                                _typeAlert.postValue(1)
                                _showAlert.postValue(true)

                            }
                        }
                    }else{
                        val temp = AuthorizationResponseModel(
                            receiptId = request.id,
                            rrn = "sin rrn",
                            statusCode = "99",
                            statusDescription = "Denegada"
                        )
                        saveAuthorization(temp,request, false)

                        _textAlert.postValue("Su transacción no ha sido aceptada, quedara en estado de anulado")
                        _typeAlert.postValue(0)
                        _showAlert.postValue(true)
                        _textError.postValue("Hubo un error en la petición verifique la información")
                        _showError.postValue(true)
                    }

                } catch (e: IOException) {
                    _textError.postValue("Hubo un error en la petición: ${e.message}")
                    _showError.postValue(true)
                }
            }
        }

    }

    fun validateInfo(info: AuthorizationModel): Boolean{
        if(info.id.isNullOrEmpty()
            || info.commerceCode.isNullOrEmpty()
            || info.terminalCode.isNullOrEmpty()
            || info.amount.isNullOrEmpty()
            || info.card.isNullOrEmpty()){
            _textError.postValue("Por favor diligencie todo el formulario")
            _showError.postValue(true)
            return false
        }else{
            return true
        }
    }

    fun saveAuthorization(response: AuthorizationResponseModel, request: AuthorizationModel, approved: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            var results = db.authorizationDao().insertAuthorization(
                AuthorizationEntity(
                    receiptId = response.receiptId,
                    rrn = response.rrn,
                    commerceCode = request.commerceCode,
                    terminalCode = request.terminalCode,
                    amount = request.amount,
                    card = request.card,
                    statusCode = response.statusCode,
                    statusDescription = response.statusDescription,
                    approvedStatus = approved
                )
            )
            if(results != null){
                _showError.postValue(false)
                _textError.postValue("")
            }else{
                _showError.postValue(true)
                _textError.postValue("Lo sentimos pero hubo un error al guardar la autorización")
            }
        }

    }
}
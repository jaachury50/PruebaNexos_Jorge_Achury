package com.example.pruebanexos.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebanexos.config.StartApp.Companion.db
import com.example.pruebanexos.models.ListAdapterAuthorizationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel: ViewModel() {

    private var _listAuthorizations = MutableLiveData<List<ListAdapterAuthorizationModel>>()
    private var _showError = MutableLiveData<Boolean>()
    private var _textError = MutableLiveData<String>()
    var listAuthorizations: LiveData<List<ListAdapterAuthorizationModel>> = _listAuthorizations
    var showError: LiveData<Boolean> = _showError
    var textError: LiveData<String> = _textError

    fun getAllAuthorizations(){
        viewModelScope.launch(Dispatchers.IO){
            val result = db.authorizationDao().getAllAuthorizations()
            withContext(Dispatchers.Main){
                _listAuthorizations.value = result.map { entity ->
                    ListAdapterAuthorizationModel(
                        entity.receiptId,
                        entity.rrn,
                        entity.commerceCode,
                        entity.terminalCode,
                        entity.amount,
                        entity.card,
                        entity.statusCode,
                        entity.statusDescription,
                        entity.approvedStatus
                    )
                }

                if(listAuthorizations.value.isNullOrEmpty()){
                    _textError.postValue("No existen autorizaciones para mostrar, por favor inserte alguna.")
                    _showError.postValue(true)
                }
            }


        }
    }
}
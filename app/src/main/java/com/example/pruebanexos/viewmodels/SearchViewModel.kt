package com.example.pruebanexos.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebanexos.config.StartApp.Companion.db
import com.example.pruebanexos.models.ListAdapterAuthorizationModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel: ViewModel() {

    private var _listAuthorizations = MutableLiveData<List<ListAdapterAuthorizationModel>>()
    private var _showError = MutableLiveData<Boolean>()
    private var _searchText = MutableLiveData<String>()
    private var _filteredList = MutableLiveData<List<ListAdapterAuthorizationModel>>()
    var listAuthorizations: LiveData<List<ListAdapterAuthorizationModel>> = _listAuthorizations
    var showError: LiveData<Boolean> = _showError
    val searchText: LiveData<String> = _searchText
    val filteredList: LiveData<List<ListAdapterAuthorizationModel>> = _filteredList

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->

    }

    fun setSearchText(text: String) {
        _searchText.postValue(text)
    }

    fun setFilteredList(filteredList: List<ListAdapterAuthorizationModel>) {
        _filteredList.postValue(filteredList)

        if(filteredList.isEmpty()){
            _showError.postValue(true)
        }else{
            _showError.postValue(false)
        }
    }

    fun getAllTransactions(){

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler){
            val result = db.authorizationDao().getAllAuthorizationsApproved()
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

                _filteredList.value = _listAuthorizations.value
            }

        }
    }


}
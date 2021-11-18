package com.linhphan.presentation.base

import android.content.Context
import androidx.lifecycle.*
import com.linhphan.common.Logger
import com.linhphan.presentation.util.connection.ConnectionLiveData
import com.linhphan.presentation.util.connection.ConnectionState

abstract class BaseViewModel: ViewModel() {
    companion object{
        private const val tag = "BaseViewModel"
    }
    private val _connectionState: MutableLiveData<ConnectionState> =
        MutableLiveData(
            ConnectionState(ConnectionState.CONNECTION_HAS_NOT_CHECK,false)
        )
    private var connectionLiveData : ConnectionLiveData? = null


    fun listenNetworkState(context: Context, owner: LifecycleOwner) : LiveData<ConnectionState> {
        if (connectionLiveData == null) {
            connectionLiveData = ConnectionLiveData(context)
            connectionLiveData?.observe(owner, { newConnectionState ->
                    Logger.e(tag, "network state = ${newConnectionState.type}")
                    _connectionState.postValue(newConnectionState)
                })
        }
        return _connectionState
    }
}
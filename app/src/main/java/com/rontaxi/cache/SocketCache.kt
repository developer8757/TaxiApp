package com.rontaxi.cache

import android.arch.lifecycle.MutableLiveData
import com.rontaxi.socket.SocketManager

val currentSocketState= MutableLiveData<SocketManager.SOCKET_STATE>()
    .apply {
    postValue(SocketManager.SOCKET_STATE.DISCONNECTED)
}
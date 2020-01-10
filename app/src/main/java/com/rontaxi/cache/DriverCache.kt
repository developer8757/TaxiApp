package com.rontaxi.cache

import android.arch.lifecycle.MutableLiveData


val isDriverOnline =MutableLiveData<Boolean>().apply {

    this.postValue(false)


}
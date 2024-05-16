package com.myapps.mypoi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapps.mypoi.database.repository.MyPoiRepository

class MyPoiViewModelFactory(private val repository: MyPoiRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPoiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyPoiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

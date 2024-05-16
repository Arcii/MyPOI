package com.myapps.mypoi.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.myapps.mypoi.database.model.PoiCategory
import com.myapps.mypoi.database.model.PoiLocation
import com.myapps.mypoi.database.repository.MyPoiRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MyPoiViewModel(private val repository: MyPoiRepository) : ViewModel() {

    val allCategories: LiveData<List<PoiCategory>> = repository.allCategories
    val selectedCategory = mutableStateOf<PoiCategory?>(null)

    fun selectCategory(category: PoiCategory) {
        selectedCategory.value = category
    }

    fun clearSelectedCategory() {
        selectedCategory.value = null
    }

    fun getLocationsByCategoryId(categoryId: Int): LiveData<List<PoiLocation>> {
        return repository.getLocationsByCategoryId(categoryId)
    }

    fun insertLocation(location: PoiLocation) {
        //Coroutine launch
        viewModelScope.launch {
            repository.insertLocation(location)
        }
    }

}

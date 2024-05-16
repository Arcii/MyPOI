package com.myapps.mypoi.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.myapps.mypoi.database.model.PoiCategory
import com.myapps.mypoi.database.model.PoiLocation
import com.myapps.mypoi.database.repository.MyPoiRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.compose.runtime.State

class MyPoiViewModel(private val repository: MyPoiRepository) : ViewModel() {

    val allCategories: Flow<List<PoiCategory>> = repository.allCategories

    private val _selectedCategory = mutableStateOf<PoiCategory?>(null)
    val selectedCategory: State<PoiCategory?> = _selectedCategory

    fun selectCategory(category: PoiCategory) {
        _selectedCategory.value = category
    }

    fun clearSelectedCategory() {
        _selectedCategory.value = null
    }

    fun getLocationsByCategoryId(categoryId: Int): Flow<List<PoiLocation>> {
        return repository.getLocationsByCategoryId(categoryId)
    }

    fun insertLocation(location: PoiLocation) {
        viewModelScope.launch {
            repository.insertLocation(location)
        }
    }
}


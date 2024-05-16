package com.myapps.mypoi.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapps.mypoi.database.model.PoiCategory
import com.myapps.mypoi.database.model.PoiLocation
import com.myapps.mypoi.database.repository.MyPoiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    fun getCategoryNameById(categoryId: Int): StateFlow<String?> {
        val categoryNameFlow = MutableStateFlow<String?>(null)
        viewModelScope.launch {
            val category = repository.getCategoryById(categoryId)
            categoryNameFlow.value = category?.name
        }
        return categoryNameFlow
    }

    fun getLocationsByCategoryId(categoryId: Int): Flow<List<PoiLocation>> {
        return repository.getLocationsByCategoryId(categoryId)
    }

    fun insertLocation(location: PoiLocation) {
        viewModelScope.launch {
            repository.insertLocation(location)
        }
    }

    fun insertCategory(category: PoiCategory) {
        viewModelScope.launch {
            repository.insertCategory(category)
        }
    }

    fun deleteCategory(category: PoiCategory) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

    fun updateCategory(category: PoiCategory) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    fun updateLocation(location: PoiLocation) {
        viewModelScope.launch {
            repository.updateLocation(location)
        }
    }

    fun deleteLocation(location: PoiLocation) {
        viewModelScope.launch {
            repository.deleteLocation(location)
        }
    }

}


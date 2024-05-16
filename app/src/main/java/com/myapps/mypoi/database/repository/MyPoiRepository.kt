package com.myapps.mypoi.database.repository

import com.myapps.mypoi.database.dao.MyPoiDao
import com.myapps.mypoi.database.model.PoiCategory
import com.myapps.mypoi.database.model.PoiLocation
import kotlinx.coroutines.flow.Flow

class MyPoiRepository (private val poiDao: MyPoiDao) {

    val allCategories: Flow<List<PoiCategory>> = poiDao.getAllCategories()

    suspend fun insertCategory(category: PoiCategory) {
        poiDao.insertCategory(category)
    }

    suspend fun updateCategory(category: PoiCategory) {
        poiDao.updateCategory(category)
    }

    suspend fun deleteCategory(category: PoiCategory) {
        poiDao.deleteCategory(category)
    }

    fun getLocationsByCategoryId(categoryId: Int): Flow<List<PoiLocation>> {
        return poiDao.getLocationsByCategoryId(categoryId)
    }

    suspend fun insertLocation(location: PoiLocation) {
        poiDao.insertLocation(location)
    }

    suspend fun updateLocation(location: PoiLocation) {
        poiDao.updateLocation(location)
    }

    suspend fun deleteLocation(location: PoiLocation) {
        poiDao.deleteLocation(location)
    }

}
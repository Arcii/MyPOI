package com.myapps.mypoi.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.myapps.mypoi.database.model.PoiCategory
import com.myapps.mypoi.database.model.PoiLocation

@Dao
interface MyPoiDao {

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<PoiCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: PoiCategory)

    @Update
    suspend fun updateCategory(category: PoiCategory)

    @Delete
    suspend fun deleteCategory(category: PoiCategory)

    @Query("SELECT * FROM locations WHERE categoryId = :categoryId")
    fun getLocationsByCategoryId(categoryId: Int): LiveData<List<PoiLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: PoiLocation)

    @Update
    suspend fun updateLocation(location: PoiLocation)

    @Delete
    suspend fun deleteLocation(location: PoiLocation)

}
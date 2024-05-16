package com.myapps.mypoi.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class PoiCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)

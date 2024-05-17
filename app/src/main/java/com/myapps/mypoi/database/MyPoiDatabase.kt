package com.myapps.mypoi.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.myapps.mypoi.database.dao.MyPoiDao
import com.myapps.mypoi.database.model.PoiCategory
import com.myapps.mypoi.database.model.PoiLocation

@Database(entities = [PoiCategory::class, PoiLocation::class], version = 1)
abstract class MyPoiDatabase: RoomDatabase() {
    abstract fun myPoiDao(): MyPoiDao

    companion object{
        @Volatile
        private var INSTANCE: MyPoiDatabase? = null

        fun getDatabase(context: Context): MyPoiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyPoiDatabase::class.java,
                    "my_poi_database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Insert default categories
                        defaultCategories.forEach { category ->
                            db.execSQL("INSERT INTO categories (name) VALUES (?)", arrayOf(category.name))
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }

}

val defaultCategories = listOf(
    PoiCategory(name = "Parking"),
    PoiCategory(name = "Restaurants and Dining"),
    PoiCategory(name = "Shopping and Retail"),
    PoiCategory(name = "Entertainment and Leisure"),
    PoiCategory(name = "Parks and Recreation"),
    PoiCategory(name = "Healthcare"),
    PoiCategory(name = "Education"),
    PoiCategory(name = "Financial Services"),
    PoiCategory(name = "Transportation"),
    PoiCategory(name = "Accommodation"),
    PoiCategory(name = "Religious Sites"),
    PoiCategory(name = "Government and Public Services"),
    PoiCategory(name = "Utilities and Service Providers"),
    PoiCategory(name = "Tourist Attractions"),
    PoiCategory(name = "Sports and Fitness"),
    PoiCategory(name = "Nightlife")
)
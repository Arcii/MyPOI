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
                            Log.d("MyPoiDatabase", "Inserted category: ${category.name}")
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
    PoiCategory(name = "Point Of Interest"),
    PoiCategory(name = "Restaurant"),
    PoiCategory(name = "Shop")
)
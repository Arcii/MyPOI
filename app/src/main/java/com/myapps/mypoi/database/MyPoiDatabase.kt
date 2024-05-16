package com.myapps.mypoi.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myapps.mypoi.database.dao.MyPoiDao
import com.myapps.mypoi.database.model.PoiCategory
import com.myapps.mypoi.database.model.PoiLocation

@Database(entities = [PoiCategory::class, PoiLocation::class], version = 1, exportSchema = false)
abstract class MyPoiDatabase: RoomDatabase() {
    abstract fun myPoiDao(): MyPoiDao

    companion object{
        @Volatile
        private var INSTANCE: MyPoiDatabase? = null

        fun getDatabase(context: Context): MyPoiDatabase {
            Log.d("MyPoiDatabase", "Getting database instance... {$context}")
            return INSTANCE ?: synchronized(this) {
                Log.d("MyPoiDatabase", "Entered sinchronized block.")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyPoiDatabase::class.java,
                    "my_poi_database"
                ).build()
                Log.d("MyPoiDatabase", "build finished")
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

/*.addCallback(object : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        Log.d("MyPoiDatabase", "Database created successfully.")
        // Insert default categories
        defaultCategories.forEach { category ->
            db.execSQL("INSERT INTO categories (name) VALUES (?)", arrayOf(category.name))
            Log.d("MyPoiDatabase", "Inserted category: ${category.name}")
        }
        Log.d("MyPoiDatabase", "populated with default categories")
    }
})*/
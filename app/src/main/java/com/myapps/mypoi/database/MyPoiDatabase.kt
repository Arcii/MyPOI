package com.myapps.mypoi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.myapps.mypoi.R
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
                    context.getString(R.string.my_poi_db_name)
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Insert default categories
                        val defaultCategories = getDefaultCategories(context)
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

fun getDefaultCategories(context: Context): List<PoiCategory> {
    return listOf(
        PoiCategory(name = context.getString(R.string.my_poi_db_category_parking)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_restaurants)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_shop)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_entertainment)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_parks)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_healthcare)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_education)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_financial)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_transportation)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_accommodation)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_religious_sites)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_public_services)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_utilities)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_tourist_attractions)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_sports_fitness)),
        PoiCategory(name = context.getString(R.string.my_poi_db_category_nightlife))
    )
}
package com.example.bitter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [PostItem::class], version = 1, exportSchema = false)
abstract class PostDatabase: RoomDatabase() {
    abstract fun postDao(): PostDatabaseDao

    companion object {
        private var INSTANCE: PostDatabase? = null

        fun getInstance(context: Context): PostDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PostDatabase::class.java,
                        "post_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
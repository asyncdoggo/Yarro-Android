package com.example.bitter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [PostItem::class], version = 2, exportSchema = false)
abstract class PostDatabase: RoomDatabase() {
    abstract fun postDao(): PostDatabaseDao

    companion object {
        private const val DB_NAME = "post_database"

        var instance: PostDatabase? = null
        private set

        fun setInstance(context: Context): PostDatabase? {
            if (instance == null) {
                synchronized(PostDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            PostDatabase::class.java, DB_NAME
                        ).build()
                    }
                }
            }
            return instance
        }
    }
}

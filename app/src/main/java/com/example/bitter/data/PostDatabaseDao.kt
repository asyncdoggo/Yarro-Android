package com.example.bitter.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostDatabaseDao {
    @Query("SELECT * FROM PostTable")
    fun getAllPost(): LiveData<List<PostItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PostItem)

    @Delete
    suspend fun delete(item: PostItem)

    @Update
    suspend fun update(item: PostItem)

    @Query("SELECT * FROM PostTable where postId = :id")
    suspend fun getPostById(id:String):PostItem?

    @Query("DELETE FROM PostTable")
    suspend fun deleteAll()
}
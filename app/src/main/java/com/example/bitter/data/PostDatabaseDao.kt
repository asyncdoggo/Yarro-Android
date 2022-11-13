package com.example.bitter.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDatabaseDao {
    @Query("SELECT * FROM PostTable")
    fun getAllPost(): LiveData<List<PostItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PostItem)

    @Query("SELECT MAX(postId) from PostTable")
    suspend fun getLatest() : String

    @Delete
    suspend fun delete(item: PostItem)

    @Query("UPDATE PostTable SET lc = :lc,dlc=:dlc,isLiked=:isliked,isdisLiked = :isdisliked where postId = :pid")
    suspend fun update(pid: String, lc: Int, dlc: Int, isliked: Int, isdisliked: Int)

    @Query("SELECT * FROM PostTable where postId = :id")
    suspend fun getPostById(id:String):PostItem?

    @Query("DELETE FROM PostTable")
    suspend fun deleteAll()

    @Query("SELECT * FROM PostTable where byuser = :uname")
    fun getPosts(uname:String): LiveData<List<PostItem>>
}
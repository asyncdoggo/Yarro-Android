package com.example.bitter.data

import androidx.lifecycle.LiveData

class PostRepository(private val postDatabaseDao: PostDatabaseDao) {
    val getAllPosts: LiveData<List<PostItem>> = postDatabaseDao.getAllPost()

    suspend fun getPostById(id:String) = postDatabaseDao.getPostById(id)

    suspend fun insert(postItem: PostItem){
        postDatabaseDao.insert(postItem)
    }

    suspend fun update(postItem: PostItem){
        postDatabaseDao.update(postItem)
    }

    suspend fun delete(postItem: PostItem){
        postDatabaseDao.delete(postItem)
    }

    suspend fun deleteAll(){
        postDatabaseDao.deleteAll()
    }
}
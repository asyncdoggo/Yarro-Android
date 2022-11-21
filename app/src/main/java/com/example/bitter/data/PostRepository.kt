package com.example.bitter.data

import androidx.lifecycle.LiveData

class PostRepository(private val postDatabaseDao: PostDatabaseDao) {
    val getAllPosts: LiveData<List<PostItem>> = postDatabaseDao.getAllPost()

    fun getPosts(uname:String): LiveData<List<PostItem>> {
        return postDatabaseDao.getPosts(uname)
    }
    
    suspend fun getPostById(id:Int) = postDatabaseDao.getPostById(id)

    suspend fun insert(postItem: PostItem){
        postDatabaseDao.insert(postItem)
    }

    suspend fun getLatest() = postDatabaseDao.getLatest()

    suspend fun update(pid:Int, lc: Int, dlc:Int,isliked:Int,isdisliked:Int){
        postDatabaseDao.update(pid,lc,dlc,isliked,isdisliked)
    }

    suspend fun delete(postItem: PostItem){
        postDatabaseDao.delete(postItem)
    }

    suspend fun deleteAll(){
        postDatabaseDao.deleteAll()
    }
}
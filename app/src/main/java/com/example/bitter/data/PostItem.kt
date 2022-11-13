package com.example.bitter.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "PostTable")
data class PostItem(
    @PrimaryKey
    var postId: String,

    @ColumnInfo(name = "content")
    var content: String,

    @ColumnInfo(name = "lc")
    var lc :Int,
    @ColumnInfo(name = "dlc")
    var dlc :Int,

    @ColumnInfo(name = "isLiked")
    var isliked: Int,

    @ColumnInfo(name = "isdisLiked")
    var isdisliked: Int,

    @ColumnInfo(name = "byuser")
    var byuser: String,

    @ColumnInfo(name = "datetime")
    var datetime: String
)
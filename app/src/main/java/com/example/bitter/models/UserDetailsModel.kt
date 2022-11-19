package com.example.bitter.models

import kotlinx.serialization.Serializable



@Serializable
data class UserDetailsModel(
    val status: String,
    val data: UserDetails
)

@Serializable
data class UserDetails(
    val fname: String?,
    val lname: String?,
    val age: String?,
    val gender: String?,
    val mob:String?,
    val dob: String?,
    val bio: String?
)

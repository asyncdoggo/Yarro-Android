package com.example.bitter.models

import kotlinx.serialization.Serializable

@Serializable
data class StatusResponseModel(
    val status: String? = null,
    val token: String? = null,
    val uname:String? = null
)
package com.example.bitter.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject


@Serializable
data class PostResponseModel(
    val status: String? = null,
    val data:JsonObject? = null
)

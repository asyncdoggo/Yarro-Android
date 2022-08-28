package com.example.bitter

import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


fun postForm(PostForm: JSONObject,callback: Callback) {
    val body = PostForm.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    val request = Request.Builder()
        .url("http://192.168.1.6:5005")
        .post(body)
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .build()

    val okHttpClient = OkHttpClient()
    okHttpClient.newCall(request).enqueue(callback)
}
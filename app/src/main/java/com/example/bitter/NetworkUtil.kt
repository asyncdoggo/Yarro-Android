package com.example.bitter

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException



fun postForm(PostForm: JSONObject): JSONObject {
    var returnval = JSONObject()
    val body = PostForm.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    val request = Request.Builder()
        .url("http://192.168.1.6:5005")
        .post(body)
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .build()

    val okHttpClient = OkHttpClient()
    okHttpClient.newCall(request).enqueue(
        object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val loginResponseString = String(response.body!!.bytes())

                returnval =  JSONObject(loginResponseString)
            }
        })
    return returnval

}
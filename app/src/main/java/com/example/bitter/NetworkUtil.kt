package com.example.bitter

import android.content.Context
import android.graphics.Bitmap
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import coil.memory.MemoryCache
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


fun postForm(
    PostForm: JSONObject,
    callback: Callback
) {
    val body = PostForm.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    val request = Request.Builder()
        .url(postUrl)
        .post(body)
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .build()

    val okHttpClient = OkHttpClient()
    okHttpClient.newCall(request).enqueue(callback)
}

fun postImage(
    context :Context,
    bitmap: Bitmap,
    uname:String,
    key:String
) {
    val fileData = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("uname",uname)
        .addFormDataPart("key",key)
        .addFormDataPart(
            "image",
            filename = uname,
            bitmapToPng(context,bitmap).asRequestBody("image/*".toMediaTypeOrNull())
        )
        .build()

        val request = Request.Builder()
            .url("$postUrl/sendimage")
            .post(fileData)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = String(response.body.bytes())
                val ret = JSONObject(responseString)
                println(ret)
            }

        })
}

fun bitmapToPng(context: Context, bitmap:Bitmap): File {
    //create a file to write bitmap data
    val f = File(context.cacheDir, "tempfile")
    f.createNewFile()

//Convert bitmap to byte array
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
    val bitmapdata = bos.toByteArray()

//write the bytes in file
    val fos = FileOutputStream(f)
    fos.write(bitmapdata)
    fos.flush()
    fos.close()

    return f
}

@OptIn(ExperimentalCoilApi::class)
fun removeCoilCache(context: Context,url:String){
    val imageLoader = context.imageLoader
    imageLoader.diskCache?.remove(url)
    imageLoader.memoryCache?.remove(MemoryCache.Key(url))
}
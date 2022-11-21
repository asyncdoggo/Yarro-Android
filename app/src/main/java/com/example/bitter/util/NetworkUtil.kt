package com.example.bitter.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.example.bitter.models.PostResponseModel
import com.example.bitter.models.StatusResponseModel
import com.example.bitter.models.UserDetailsModel
import com.example.bitter.postUrl
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.Headers
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun bitmapToPng(context: Context, bitmap: Bitmap): File {
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
fun removeCoilCache(context: Context) {
    val imageLoader = context.imageLoader
    imageLoader.diskCache?.clear()
    imageLoader.memoryCache?.clear()
}



// KTOR CONFIG


private val ktorHttpClient = HttpClient(Android){
    install(ContentNegotiation){
        json(Json {
            prettyPrint = true
            isLenient = true
        })

        engine {
            connectTimeout = 60_000
            socketTimeout = 60_000
        }
    }

    install(Logging){
        logger = object : Logger {
            override fun log(message: String) {
                Log.v("Logger ktor:",message)
            }
        }
        level = LogLevel.ALL
    }

    install(ResponseObserver) {
        onResponse { response ->
            Log.d("HTTP status:", "${response.status.value}")
        }
    }

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}

object ApiService{
    suspend fun login(username: String, password: String): StatusResponseModel {
        try{
            val encoded = Base64.encodeToString("$username:$password".encodeToByteArray(), Base64.DEFAULT)

            val e: HttpResponse = ktorHttpClient.post("$postUrl/api/login") {
                headers {
                    header(HttpHeaders.Authorization, "Basic $encoded".trim())
                }
            }
            val token = e.setCookie()[0].value
            val f = e.body<StatusResponseModel>()
            f.token = token
            return f
        }
        catch (e: Exception){ return StatusResponseModel(status = "failure") }
    }

    suspend fun register(username: String, password: String, email: String): StatusResponseModel {
        val e: HttpResponse = ktorHttpClient.post("$postUrl/api/register") {
           setBody(buildJsonObject {
               put("uname",username)
               put("passwd1",password)
               put("email",email)
           })
        }
        val token = e.setCookie()[0].value
        val f = e.body<StatusResponseModel>()
        f.token = token
        return f
    }

    suspend fun forgotPass(email: String): StatusResponseModel {
        return ktorHttpClient.post("$postUrl/api/resetrequest") {
           setBody(buildJsonObject {
               put("email",email)
           })
        }.body()
    }

    suspend fun getPosts(token: String?, post: Int): PostResponseModel {
        return ktorHttpClient.post("$postUrl/api/posts") {
            headers{
                header("Cookie", "token=${token}")
            }
            setBody(buildJsonObject {
                put("latest", post)
            })
        }.body()

    }

    suspend fun sendPost(token: String?, content: String): StatusResponseModel {
        return ktorHttpClient.post("$postUrl/api/newpost") {
            headers {
                header("Cookie", "token=${token}")
            }
            setBody(buildJsonObject {
                put("content", content)
            })
        }.body()
    }

    suspend fun getUserDetails(token: String?): UserDetailsModel {
        return ktorHttpClient.post("$postUrl/api/userdetails") {
            headers {
                header("Cookie", "token=${token}")
            }
        }.body()
    }

    suspend fun postImage(token : String, bitmap: Bitmap,context: Context,uname:String){
        return HttpClient(Android).submitFormWithBinaryData(
            url = "$postUrl/api/sendimage",
            formData = formData{
                append("image", bitmapToPng(context, bitmap).readBytes(), Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=${uname}")
                })
            }
        ){
            headers {
                header("Cookie", "token=${token}")
            }
        }.body()
    }

    suspend fun updateUserDetails(fname: String, lname: String, gender: String, mob: String, dob: String,bio:String,token: String?): StatusResponseModel {
        return ktorHttpClient.post("$postUrl/api/updatedata") {
            headers {
                header("Cookie", "token=${token}")
            }
           setBody(buildJsonObject {
               put("fname", fname)
               put("lname", lname)
               put("gender", gender)
               put("mob", mob)
               put("dob", dob)
               put("bio", bio)
           })
        }.body()
    }

    suspend fun getFullName(token: String?, uname: String?): JsonObject{
        return ktorHttpClient.post("$postUrl/api/fullname") {
            headers {
                header("Cookie", "token=${token}")
            }
            setBody(buildJsonObject {
                put("uname", uname)
            })
        }.body()
    }

    suspend fun likePost(postId: Int, token: String,like:Boolean): PostResponseModel {
        return ktorHttpClient.post("$postUrl/api/like") {
            headers {
                header("Cookie", "token=${token}")
            }
            setBody(buildJsonObject {
                put("pid", postId)
                if(like){
                    put("islike", 1)
                }
                else{
                    put("islike", 0)
                }
            })
        }.body()
    }

    suspend fun checkLogin(token: String):PostResponseModel {
        return ktorHttpClient.post("$postUrl/api/checklogin") {
            headers{
                header("Cookie", "token=${token}")
            }
        }.body()
    }

    suspend fun logout(token: String?): PostResponseModel {
        return ktorHttpClient.post("$postUrl/api/logout") {
            headers {
                header("Cookie", "token=${token}")
            }
        }.body()
    }

    suspend fun updateLikeData(token: String?): PostResponseModel {
        return ktorHttpClient.post("$postUrl/api/likedata") {
            headers {
                header("Cookie", "token=${token}")
            }
        }.body()
    }

    suspend fun checkUpdates(): JsonObject {
        return ktorHttpClient.get("https://api.github.com/repos/asyncdoggo/Bitter-Android/releases/latest").body()
    }

}



// !KTOR CONFIG











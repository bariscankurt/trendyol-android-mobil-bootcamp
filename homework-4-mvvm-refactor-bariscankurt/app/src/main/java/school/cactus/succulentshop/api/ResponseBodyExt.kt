package school.cactus.succulentshop.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody

fun ResponseBody.errorMessage(): String {
    val errorBody = string()
    val gson: Gson = GsonBuilder().create()
    val loginErrorResponse = gson.fromJson(errorBody, GenericErrorResponse::class.java)
    return loginErrorResponse.message[0].messages[0].message
}
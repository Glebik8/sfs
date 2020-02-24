package com.blackfish.a1pedal.API

import android.util.Log
import com.blackfish.a1pedal.tools_class.PostRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import okhttp3.Request

fun performRequestByNumber(phone: String, type: String, callback: (String) -> Unit) {
    CoroutineScope(Dispatchers.Default).async {
        val postRes = PostRes()
        val response = postRes.post(
                "http://185.213.209.188/api/friend/"
                , "{\n" +
                "  \"type\" : \"$type\",\n" +
                "  \"phone\" : \"$phone\"\n" +
                "}"
                , "Token 5bbc50db3f6f254f8cc23a68437f2062c7e4e9e8"
        )
        callback(response)
    }
}

fun performRequest(id: String, type: String, callback: (String) -> Unit) {
    CoroutineScope(Dispatchers.Default).async {
        return@async getNumberById(id) {
            performRequestByNumber(it, type, callback)
        }
    }
}


fun getNumberById(id: String, callback: (String) -> Unit) {
    CoroutineScope(Dispatchers.Default).async {
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("http://185.213.209.188/api/getuserinfo/$id")
                .addHeader("Authorization", "Token 5bbc50db3f6f254f8cc23a68437f2062c7e4e9e8")
                .get()
                .build()
        val number = client.newCall(request).execute().body()!!.string()
                .split(',').filter { it.split(":").contains("\"phone\"") }
                .first()
                .split(':')
                .last()
                .drop(1)
                .dropLast(1)
        callback(number)
    }
}

fun updateCalendar(date: String, time: String, serviceId: String, driverId: String) {
    CoroutineScope(Dispatchers.Main).async {
        val postRes = PostRes()
        val response = postRes.post(
                "http://185.213.209.188/api/createcalendarevent/"
                , "{\n" +
                "\"time\":\"$time\"\n" +
                "\"date\":\"$date\"\n" +
                "\"service_id\":$serviceId\n" +
                "\"driver_id\":$driverId\n" +
                "\"status\":\"new\"\n" +
                "} "
                , "Token 3f2907c63373f2e0b75cfbcc8fbda2ee7863c8f1"
        )
    }
}


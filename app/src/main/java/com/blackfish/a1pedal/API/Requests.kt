package com.blackfish.a1pedal.API

import com.blackfish.a1pedal.ProfileInfo.Profile_Info
import com.blackfish.a1pedal.ProfileInfo.User
import com.blackfish.a1pedal.data.*
import com.blackfish.a1pedal.tools_class.PostRes
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class Requests {

    companion object {

        private val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        private val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(logging)
        }

        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://185.213.209.188/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        val api: API = retrofit.create(API::class.java)

        fun getInfo(id: String, callback: (UserInfo) -> Unit) {
            CoroutineScope(Dispatchers.Default).launch {
                val response = api.getInfoById(id)
                callback(response)
            }
        }

        fun getFriends(callback: (FriendsInfo) -> Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                val response = api.getFriends()
                callback(response)
            }
        }

        fun performRequestByNumber(phone: String, type: String, callback: (UpdateInfo) -> Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                val response = api.performRequest(UpdateRequest(type, phone))
                callback(response)
            }
        }

        fun getNumberById(id: String, callback: (String) -> Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                val response = api.getInfoById(id)
                val number = response.phone
                callback(number)
            }
        }

        fun updateCalendar(date: String, time: String, serviceId: String, driverId: String, status: String, callback: (Response) -> Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                val response = api.updateCalendar(Request(time, date, serviceId, driverId, status))
                callback(response)
            }
        }

        fun getEvents(id: String = "", callback: (List<Response>) -> Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                val response = api.getEvents(id)
                callback(response)
            }
        }

        fun updateEvent(eventId: Int, status: String, callback: (Response) -> Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                val response = api.updateEventsStatus(EventUpdate(eventId, status))
                callback(response)
            }
        }
    }

}

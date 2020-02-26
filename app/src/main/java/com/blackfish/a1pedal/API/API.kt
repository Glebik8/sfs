package com.blackfish.a1pedal.API

import com.blackfish.a1pedal.ProfileInfo.Profile_Info
import com.blackfish.a1pedal.data.*
import retrofit2.http.*

interface API {

    @GET("friend/")
    suspend fun getFriends(
            @Header("Authorization") auth: String = "Token " + Profile_Info.getInstance().token
    ): FriendsInfo

    @POST("createcalendarevent/")
    suspend fun updateCalendar(
            @Body request: Request,
            @Header("Authorization") auth: String = "Token " + Profile_Info.getInstance().token
    ): Response

    @GET("getcalendarevents/{pk}")
    suspend fun getEvents(
            @Path("pk") id: String,
            @Header("Authorization") auth: String = "Token " + Profile_Info.getInstance().token
    ): List<Response>


    @GET("getuserinfo/{id}")
    suspend fun getInfoById(
            @Path("id") id: String,
            @Header("Authorization") auth: String = "Token " + Profile_Info.getInstance().token
    ): UserInfo

    @POST("friend/")
    suspend fun performRequest(
            @Body request: UpdateRequest,
            @Header("Authorization") auth: String = "Token " + Profile_Info.getInstance().token
    ): UpdateInfo

    @POST("editcalendareventstatus/")
    suspend fun updateEventsStatus(
            @Body request: EventUpdate,
            @Header("Authorization") auth: String = "Token " + Profile_Info.getInstance().token
    ): Response

}
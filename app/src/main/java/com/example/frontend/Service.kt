package com.example.frontend

import android.provider.ContactsContract.CommonDataKinds.Nickname
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Service {

    @POST("/api/v1/member/register")
    fun login(
        @Body loginRequestData: LoginReqeust
    ) : Call<LoginResponse>

    @POST("/api/v1/member/check")
    fun nick(
        @Query("nickname") nickname: String
    ) : Call<Unit>

}
package com.example.frontend

import android.provider.ContactsContract.CommonDataKinds.Nickname
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Service {

    @POST("/api/v1/member/login")
    fun login(
        @Body loginRequest: LoginReqeust
    ) : Call<Int>

    @POST("/api/v1/member/check")
    fun nick(
        @Query("nickname") nickname: String
    ) : Call<Unit>

    @Multipart
    @POST("/api/v1/member/register")
    fun registerMember(
        @Part("memberRegisterDto") memberRegisterDto: MemberRegisterDto,
        @Part image: MultipartBody.Part?
    ): Call<Int>
}
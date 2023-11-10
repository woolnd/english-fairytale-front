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

    @GET("/api/v1/member/{userId}/detail")
    fun detailInfo(
        @Path("userId") userId: Int
    ): Call<DetailInfoResponse>

    @PATCH("/api/v1/member/password/{memberId}")
    fun modifyPw(
        @Path("memberId") memberId: Int,
        @Body ModifyPwRequest: ModifyPwRequest
    ): Call<Unit>

    @PATCH("/api/v1/member/nickname")
    fun modifyNick(
        @Query("memberId") memberId: Int,
        @Query("nickname") nickname: String
    ): Call<Unit>

    @Multipart
    @POST("/api/v1/fairytale/create")
    fun makeFairytale(
        @Part("taleCreateDto") taleCreateDto: TaleCreateDto,
        @Part image: MultipartBody.Part?
    ): Call<TaleResponse>

    @GET("/api/v1/fairytale/{taleId}/detail")
    fun bookDetail(
        @Path("taleId") taleId: Int?
    ): Call<BookDetailResponse>
}
package com.example.frontend

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class Book(
    var title: String,
    var keywords: ArrayList<String>,
    var heart: Boolean
)

data class Books(
    val nick: String,
    var title: String,
    var keywords: ArrayList<String>,
    var heart: Boolean
)

data class Notify(
    var title: String,
    var date: String,
    var content: String
)

data class Faq(
    var title: String,
    var content: String
)

data class Search(
    var title: String,
    var keywords: String,
    var nick: String,
    var heart: Boolean
)

data class LoginReqeust(
    var email: String,
    var password: String
)

data class ModifyPw(
    var originalPassword: String,
    var newPassword: String
)

data class MemberRegisterDto(

    @field:SerializedName("phoneNumber")
    var phoneNumber: String,

    @field:SerializedName("nickname")
    var nickname: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)

data class DetailInfoResponse(
    var nickname: String,
    var email: String,
    var phoneNumber: String,
    var imageUrl: String
)

data class ModifyPwRequest(
    var originalPassword: String,
    var newPassword: String
)


package com.example.frontend

import androidx.annotation.Nullable

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

data class LoginResponse(
    var id: Int
)

data class ModifyPw(
    var originalPassword: String,
    var newPassword: String
)

data class JoinRequest(
    var name: String,
    var phoneNumber: String,
    var nickname: String,
    var email: String,
    var password: String
)

data class JoinResponse(
    var memnberId: Int
)
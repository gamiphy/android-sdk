package com.gamiphy.library.models

data class User(
    var email: String,
    var name: String,
    var hash: String = "",
    var avatar: String = ""
)
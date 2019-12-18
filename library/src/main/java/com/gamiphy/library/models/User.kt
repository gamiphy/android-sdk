package com.gamiphy.library.models

data class User(
    var email: String,
    var name: String,
    var hash: String = "",
    val referral: Referral? = null,
    var avatar: String = ""
)
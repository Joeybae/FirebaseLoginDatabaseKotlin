package com.example.firebase_login_database_kotlin.users

import com.google.firebase.database.IgnoreExtraProperties

// [START blog_user_class]
@IgnoreExtraProperties
data class User(
    var username: String? = "",
    var email: String? = ""
)
// [END blog_user_class]

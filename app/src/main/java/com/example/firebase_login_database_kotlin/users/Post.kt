package com.example.firebase_login_database_kotlin.users

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.HashMap

// [START post_class]
@IgnoreExtraProperties
data class Post(
    var uid: String? = "",
    var author: String? = "",
    var title: String? = "",
    var date: String? = "",
    var body: String? = "",
    var IMAGE: String? ="",
    var starCount: Int = 0,
    var stars: MutableMap<String, Boolean> = HashMap()
) {

    // [START post_to_map]
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "uid" to uid,
                "author" to author,
                "title" to title,
                "date" to date,
                "body" to body,
                "IMAGE" to IMAGE,
                "starCount" to starCount,
                "stars" to stars
        )
    }
    // [END post_to_map]
}
// [END post_class]

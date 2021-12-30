package com.krygodev.appforartists.core.domain.model

data class Image(
    val uid: String? = null,
    val url: String? = null,
    val authorUsername: String? = null,
    val authorUid: String? = null,
    val likes: Int? = null,
    val likedBy: List<String>? = null,
    val tags: List<String>? = null
)

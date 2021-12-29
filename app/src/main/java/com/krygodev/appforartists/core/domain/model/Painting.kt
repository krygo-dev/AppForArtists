package com.krygodev.appforartists.core.domain.model

data class Painting(
    val uid: String? = null,
    val url: String? = null,
    val author: String? = null,
    val likes: Int? = null,
    val likedBy: List<String>? = null
)

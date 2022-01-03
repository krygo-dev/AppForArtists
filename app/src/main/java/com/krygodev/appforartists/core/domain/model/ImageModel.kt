package com.krygodev.appforartists.core.domain.model

data class ImageModel(
    var id: String? = null,
    var url: String? = null,
    val authorUsername: String? = null,
    val authorUid: String? = null,
    val description: String? = null,
    val likes: Int? = null,
    val likedBy: List<String>? = null,
    val tags: List<String>? = null
)

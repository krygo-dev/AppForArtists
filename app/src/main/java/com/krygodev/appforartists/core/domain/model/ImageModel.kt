package com.krygodev.appforartists.core.domain.model

data class ImageModel(
    var id: String? = null,
    var url: String? = null,
    val authorUsername: String? = null,
    val authorUid: String? = null,
    val description: String? = null,
    val timestamp: Long? = null,
    val likes: Int = 0,
    val likedBy: List<String> = listOf(),
    val tags: List<String> = listOf()
)

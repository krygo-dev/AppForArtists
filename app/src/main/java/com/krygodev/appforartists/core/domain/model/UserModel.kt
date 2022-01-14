package com.krygodev.appforartists.core.domain.model

data class UserModel(
    val uid: String? = null,
    val email: String? = null,
    val username: String? = null,
    val userPhotoUrl: String? = null,
    val bio: String? = null,
    val images: List<String> = listOf(),
    val favorites: List<String> = listOf(),
    val starredBy: List<String> = listOf(),
    val starsSum: Int = 0,
    val starsAvg: Float = 0f,
    var chatrooms: List<String> = listOf()
)

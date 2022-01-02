package com.krygodev.appforartists.core.domain.model

data class UserModel(
    val uid: String? = null,
    val email: String? = null,
    val username: String? = null,
    val userPhotoUrl: String? = null,
    val bio: String? = null,
    val images: List<String> = listOf(),
    val favorites: List<String> = listOf()
)

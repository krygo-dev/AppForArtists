package com.krygodev.appforartists.feature_profile.domain.model

data class MessageModel(
    var id: String? = null,
    val sender: String? = null,
    val receiver: String? = null,
    val message: String? = null,
    val time: Long? = null
)

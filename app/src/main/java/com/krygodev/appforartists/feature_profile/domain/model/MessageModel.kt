package com.krygodev.appforartists.feature_profile.domain.model

data class MessageModel(
    var id: String,
    val sender: String,
    val receiver: String,
    val message: String,
    val time: Long
)

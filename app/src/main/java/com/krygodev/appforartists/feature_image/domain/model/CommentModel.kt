package com.krygodev.appforartists.feature_image.domain.model

import com.google.firebase.Timestamp

data class CommentModel(
    var id: String? = null,
    val author: String? = null,
    val content: String? = null,
    val timestamp: Timestamp? = null
)

package com.krygodev.appforartists.core.domain.util

object Constants {

    // Firestore collections
    const val USER_COLLECTION = "Users"
    const val IMAGES_COLLECTION = "Images"

    // Storage bucket
    const val IMAGES_BUCKET = "images"
    const val USER_PHOTOS_BUCKET = "user_photos"

    // Navigation routes
    const val ROOT_GRAPH_ROUTE = "root"
    const val AUTHENTICATION_GRAPH_ROUTE = "authentication"
    const val HOME_GRAPH_ROUTE = "home"

    // Navigation arguments
    const val PARAM_USER_UID = "uid"
    const val PARAM_IMAGE_ID = "id"

    // Images in profile selections
    const val SELECT_IMAGES = "images"
    const val SELECT_FAVORITES = "favorites"
}
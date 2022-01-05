package com.krygodev.appforartists.feature_image.presentation.image_details.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TagListItem(
    tag: String
) {
    Box(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Text(text = tag, modifier = Modifier.padding(6.dp))
    }
}
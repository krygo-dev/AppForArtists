package com.krygodev.appforartists.feature_profile.presentation.chatrooms.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.krygodev.appforartists.R
import com.krygodev.appforartists.core.domain.model.UserModel

@Composable
fun ChatroomListItem(
    user: UserModel,
    onCLick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable { onCLick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(
                data = if (!user.userPhotoUrl.isNullOrEmpty()) user.userPhotoUrl
                else R.mipmap.ic_default_profile_image,
                builder = {
                    transformations(CircleCropTransformation())
                }
            ),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${user.username}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
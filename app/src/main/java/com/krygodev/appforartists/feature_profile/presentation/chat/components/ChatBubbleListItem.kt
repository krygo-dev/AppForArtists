package com.krygodev.appforartists.feature_profile.presentation.chat.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krygodev.appforartists.feature_profile.domain.model.MessageModel

@Composable
fun ChatBubbleListItem(
    message: MessageModel,
    sentByCurrentUser: Boolean
) {
    val alignment = if (sentByCurrentUser) Alignment.End else Alignment.Start
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = alignment
    ) {
        Card(
            modifier = Modifier.widthIn(max = 300.dp),
            shape = RoundedCornerShape(30.0f),
            elevation = 5.dp,
            backgroundColor = if (sentByCurrentUser) Color.Black else Color.DarkGray
        ) {
            Text(
                text = "${message.message}",
                fontSize = 17.sp,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
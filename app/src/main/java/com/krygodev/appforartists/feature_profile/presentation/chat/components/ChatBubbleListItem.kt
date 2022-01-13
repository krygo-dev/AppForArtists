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
import com.krygodev.appforartists.feature_profile.domain.model.ChatroomModel
import com.krygodev.appforartists.feature_profile.domain.model.MessageModel

@Composable
fun ChatBubbleListItem(
    message: MessageModel,
    chatroom: ChatroomModel
) {
    Card(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(30.0f),
        elevation = 5.dp
    ) {
        val arrangement = if (message.sender == chatroom.uid1) Arrangement.End else Arrangement.Start
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = arrangement,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${message.message}",
                fontSize = 17.sp,
                color = Color.Black
            )
        }
    }
}
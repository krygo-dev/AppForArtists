package com.krygodev.appforartists.feature_image.presentation.image_details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.feature_image.domain.model.CommentModel

@Composable
fun CommentsListItem(
    comment: CommentModel,
    user: UserModel,
    onDelete: (CommentModel) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "${comment.authorName} : ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = comment.content,
                fontSize = 18.sp
            )
        }
        if (comment.authorUid == user.uid) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Usuń",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onDelete(comment) }
                )
            }
        }
    }
}
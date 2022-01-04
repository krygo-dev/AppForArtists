package com.krygodev.appforartists.feature_image.presentation.image_details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.feature_image.domain.model.CommentModel

@Composable
fun CommentsListItem(
    comment: CommentModel,
    user: UserModel,
    onEdit: (String) -> Unit,
    onSubmitEdit: (CommentModel) -> Unit,
    onDelete: (CommentModel) -> Unit
) {
    val editingComment = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "${comment.authorName} ",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            if (editingComment.value) {
                TextField(
                    value = comment.content,
                    onValueChange = { onEdit(it) },
                    trailingIcon = {
                        IconButton(
                            onClick = { onSubmitEdit(comment) }
                        ) {
                            Icon(imageVector = Icons.Filled.Send, contentDescription = null)
                        }
                    }
                )
            } else {
                Text(text = comment.content)
            }
        }
        if (comment.authorUid == user.uid) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Edytuj",
                    modifier = Modifier.clickable { editingComment.value = !editingComment.value }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Usuń",
                    modifier = Modifier.clickable { onDelete(comment) }
                )
            }
        }
    }
}
package com.krygodev.appforartists.feature_profile.presentation.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.krygodev.appforartists.R
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.presentation.util.Screen

@Composable
fun ProfileListItem(
    user: UserModel,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.ProfileScreen.route + "?uid=${user.uid}")
            },
        shape = RoundedCornerShape(30.0f),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
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
                    modifier = Modifier.size(150.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.username.toString(),
                    fontSize = 17.sp,
                    color = Color.Black
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
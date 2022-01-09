package com.krygodev.appforartists.feature_image.presentation.home.components

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
fun ProfileRowItem(
    profile: UserModel,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .padding(8.dp)
            .clickable {
                navController.navigate(Screen.ProfileScreen.route + "?uid=${profile.uid}")
            },
        shape = RoundedCornerShape(30.0f),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
        ) {
            Box(
                modifier = Modifier.height(140.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = if (!profile.userPhotoUrl.isNullOrEmpty()) profile.userPhotoUrl
                        else R.mipmap.ic_default_profile_image,
                        builder = {
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = profile.username.toString(),
                    fontSize = 17.sp,
                    color = Color.Black
                )
            }
        }
    }
}
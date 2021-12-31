package com.krygodev.appforartists.core.presentation.components

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.presentation.util.Screen

@Composable
fun ImageListItem(
    image: ImageModel,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.ImageDetailsScreen.route + "/${image.id}")
            },
        shape = RoundedCornerShape(30.0f),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            Box(modifier = Modifier.height(200.dp)) {
                Image(
                    painter = rememberImagePainter(
                        data = image.url,
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = image.authorUsername.toString(),
                    fontSize = 17.sp,
                    color = Color.Black
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
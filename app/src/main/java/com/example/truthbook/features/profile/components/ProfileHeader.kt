package com.example.truthbook.features.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.truthbook.R
import com.example.truthbook.data.models.User

@Composable
fun ProfileHeader( user: User) {

    Column(modifier = Modifier.fillMaxWidth()) {

        // 🔷 HEADER (Cover + Profile Image)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {

            // Cover with curve
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
            ) {

                AsyncImage(
                    model = user.coverImage.ifEmpty { R.drawable.cover_photo },
                    contentDescription = "Cover photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f))
                )
            }

            // 👤 Profile Image (75dp)
            AsyncImage(
                model = user.profileImage.ifEmpty { R.drawable.ic_profile_pic },
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = 16.dp, y = 0.dp)
                    .shadow(6.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
                    .clip(CircleShape)
            )
        }

        // 🔷 USER INFO SECTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp) // correct spacing after overlap
        ) {

            // Name + Edit Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = user.fullName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                OutlinedButton(
                    onClick = { },
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 6.dp
                    )
                ) {
                    Text("Edit Profile")
                }
            }

            Spacer(modifier = Modifier.height(1.dp))

            // Username
            Text(
                 text = "@${user.username}",
                fontSize = 14.sp,
                color = colorResource(R.color.ic_launcher_background)
            )

            Spacer(modifier = Modifier.height(6.dp))



                Text(
                    text = if (user.bio.isNullOrBlank()) "" else user.bio,
                    fontSize = 14.sp,
                    maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(  if (user.isPrivate) R.drawable.ic_lock
                    else R.drawable.ic_public),
                    contentDescription = "Public",
                    tint = Color(0xFF7B4BFF),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = if (user.isPrivate) "Private" else "Public"
                    , fontSize = 11.sp,)

                Spacer(modifier = Modifier.width(18.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_date),
                    contentDescription = "Joined date",
                    tint = Color(0xFF7B4BFF),
                    modifier = Modifier.size(13.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text("march 26", fontSize = 11.sp,)

            }
        }
    }
}
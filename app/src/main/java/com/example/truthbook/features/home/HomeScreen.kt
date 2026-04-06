package com.example.truthbook.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.truthbook.R
import com.example.truthbook.data.models.Post
import com.example.truthbook.data.models.Story
import com.example.truthbook.features.auth.TextPrimary
import com.example.truthbook.features.home.components.AddStoryItem
import com.example.truthbook.features.home.components.PostCard
import com.example.truthbook.features.home.components.StoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreen() {

    Scaffold(
        modifier = Modifier.background(Color(0xFFF8F6FF)),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .padding(top = 20.dp)
            ) {
                Text(
                    "TruthBook",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Cursive,
                    color = TextPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(44.dp)
                        .background(
                            color = Color(0xFFF3F0FF),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_notification),
                        contentDescription = "Notification",
                        tint = Color(0xFF7B4BFF),
                        modifier = Modifier.size(20.dp)
                    )

                    // Red dot
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Red, CircleShape)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(
                        bottom = WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFF3F0FF),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Home
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(
                                color = Color(0xFFE9E1FF),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_home),
                            contentDescription = "Home",
                            tint = Color(0xFF7B4BFF)
                        )
                        Text(
                            "Home",
                            fontSize = 12.sp,
                            color = Color(0xFF7B4BFF)
                        )
                    }

                    // Search
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "Search",
                            tint = Color.Gray
                        )
                        Text("Search", fontSize = 12.sp, color = Color.Gray)
                    }

                    // Space for FAB
                    Spacer(modifier = Modifier.width(40.dp))

                    // Messages
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.ic_message),
                            contentDescription = "Messages",
                            tint = Color.Gray
                        )
                        Text("Messages", fontSize = 12.sp, color = Color.Gray)
                    }

                    // Profile
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.ic_profile),
                            contentDescription = "Profile",
                            tint = Color.Gray
                        )
                        Text("Profile", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                // Center FAB
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-10).dp)
                        .size(60.dp)
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color(0xFF8B5CF6),
                            spotColor = Color(0xFF8B5CF6)
                        )
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF8B5CF6),
                                    Color(0xFFA78BFA)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->

        val stories = listOf(
            Story("1", "Emma", ""),
            Story("2", "Liam", ""),
            Story("3", "Ava", ""),
            Story("4", "Noah", "")
        )

        val posts = listOf(
            Post("1", "Emma", "", "Beautiful day 🌸", R.drawable.ic_post, "2 min ago", 10, 2),
            Post("2", "Liam", "", "Working hard 💻", R.drawable.ic_home, "10 min ago", 5, 1),
            Post("3", "Ava", "", "Travel vibes ✈️", R.drawable.ic_launcher_background
                , "1 hr ago", 20, 5)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "STORIES",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        AddStoryItem()
                    }
                    items(stories) { story ->
                        StoryItem(name = story.name)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(

                ) {

                    items(posts) { post ->
                        PostCard(post = post)
                    }
                }
            }
        }
    }
}
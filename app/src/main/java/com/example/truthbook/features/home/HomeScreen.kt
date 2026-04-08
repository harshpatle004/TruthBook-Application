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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.truthbook.R
import com.example.truthbook.data.models.Post
import com.example.truthbook.data.models.Story
import com.example.truthbook.features.home.components.AddStoryItem
import com.example.truthbook.features.home.components.HomeTopBar
import com.example.truthbook.features.home.components.PostCard
import com.example.truthbook.features.home.components.StoryItem
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProfileClick: () -> Unit
) {

    val stories = listOf(
        Story("1", "Emma", ""),
        Story("2", "Liam", ""),
        Story("3", "Ava", ""),
        Story("4", "Noah", "")
    )

    val posts = listOf(
        Post("1", "Emma", "", "Beautiful day 🌸", R.drawable.ic_post, "2 min ago", 10, 2),
        Post("2", "Liam", "", "Working hard 💻", R.drawable.ic_home, "10 min ago", 5, 1),
        Post("3", "Ava", "", "Travel vibes ✈️", R.drawable.ic_launcher_background, "1 hr ago", 20, 5)
    )

    // Exact background from design:
    // Top ~30% is a soft lavender/purple, then fades cleanly to white
    val backgroundBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFFCFC0FF), // rich soft purple at very top
            0.15f to Color(0xFFDDD2FF), // slightly lighter
            0.30f to Color(0xFFEDE8FF), // lavender
            0.45f to Color(0xFFF5F2FF), // near white lavender
            0.60f to Color(0xFFFFFFFF), // pure white from here down
            1.0f to Color(0xFFFFFFFF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
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
                        // Home (active)
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
                            Text("Home", fontSize = 12.sp, color = Color(0xFF7B4BFF))
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                            .clickable { onProfileClick() }) {
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {

                // TopBar scrolls with content
                item {
                    HomeTopBar()
                }

                // Stories section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "STORIES",
                        fontSize = 12.sp,
                        color = Color(0xFF888888),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item { AddStoryItem() }
                        items(stories) { story ->
                            StoryItem(name = story.name)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Posts
                items(posts) { post ->
                    PostCard(post = post)
                }
            }
        }
    }
}
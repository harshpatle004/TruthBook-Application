package com.example.truthbook.features.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.truthbook.data.models.User
import com.example.truthbook.features.profile.components.ProfileActions
import com.example.truthbook.features.profile.components.ProfileHeader
import com.example.truthbook.features.profile.components.ProfilePostList
import com.example.truthbook.features.profile.components.ProfileStats
import com.example.truthbook.features.profile.components.ProfileTabs



@Composable
fun ProfileScreen (user: User){

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement =  Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {

        item {
            ProfileHeader(user)
        }
        item{
            ProfileStats()
        }
        item{
            ProfileActions()
        }
        item{
            ProfileTabs()
        }
        item{
            ProfilePostList()
        }

    }
}
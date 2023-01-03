package com.example.fooditcompose.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.authentication.navigationArgs.navigateToAuthScreen
import com.example.common.navigation.homeScreenRoute
import com.example.common.navigation.loginScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column() {
            Text("Profile Screen")
            Button(onClick = {
                profileViewModel.logout()
                navController.navigateToAuthScreen(
                    popUpTo = loginScreenRoute
                )
            }) {
                Text(text = "Log Out")
            }
        }
    }
}
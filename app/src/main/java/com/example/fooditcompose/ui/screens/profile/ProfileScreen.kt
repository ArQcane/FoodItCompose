package com.example.fooditcompose.ui.screens.profile

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.authentication.navigationArgs.navigateToAuthScreen
import com.example.common.navigation.homeScreenRoute
import com.example.common.navigation.loginScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    val scrollState = rememberScrollState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val profileState by profileViewModel.profileState.collectAsState()
    LaunchedEffect(true) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.errorChannel.collect {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    scaffoldState.snackbarHostState.showSnackbar(it, "Okay")
                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = profileState.isLoading,
                transitionSpec = {
                    fadeIn() with fadeOut()
                }
            ) { isLoading ->
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
                        Log.d("user", profileState.user.user_id.toString())
                        Text(text = profileState.user.username)
                        Text(text = profileState.totalReviews.toString())
                    }
            }
        }
    }
}
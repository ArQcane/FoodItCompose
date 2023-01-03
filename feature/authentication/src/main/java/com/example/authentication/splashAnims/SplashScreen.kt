package com.example.authentication.splashAnims

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.authentication.R
import com.example.authentication.navigationArgs.navigateToAuthScreen
import com.example.common.navigation.homeScreenRoute
import kotlinx.coroutines.delay

@Composable
internal fun SplashScreen(
    navController: NavHostController,
    splashScreenViewModel: SplashScreenViewModel = hiltViewModel()
    ) {
    val isLoggedIn by splashScreenViewModel.isLoggedIn.collectAsState()

    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 3000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000)
        navController.popBackStack()
        if(!isLoggedIn) navController.navigateToAuthScreen()
        else return@LaunchedEffect navController.navigate(homeScreenRoute)
    }


    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier
                    .size(275.dp)
                    .alpha(alphaAnim.value),
                painter = painterResource(id = R.drawable.foodit_high_resolution_logo_color_on_transparent_background),
                contentDescription = "FoodIt's Logo"
            )
            LoadingAnimation(modifier = Modifier.alpha(alphaAnim.value))
        }
    }
}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: Color = MaterialTheme.colors.primaryVariant,
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp
) {
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable){
            startAnimation = true
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { travelDistance.toPx() }
    val lastCircle = circleValues.size - 1

    Row(modifier = Modifier) {
        circleValues.forEachIndexed { index, value ->
            Box(modifier = Modifier
                .size(circleSize)
                .alpha(alphaAnim.value)
                .graphicsLayer {
                    translationY = -value * distance
                }
                .background(
                    color = circleColor,
                    shape = CircleShape
                ))
            if(index != lastCircle){
                Spacer(modifier = Modifier.width(spaceBetween))
            }
        }
    }
}
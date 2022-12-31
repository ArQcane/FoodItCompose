package com.example.fooditcompose.ui.screens.home

import android.graphics.Color.parseColor
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.authentication.navigationArgs.navigateToAuthScreen
import com.example.common.components.CltButton
import com.example.common.utils.Screen
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    val scrollState = rememberScrollState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val state by homeViewModel.state.collectAsState()

    LaunchedEffect(true) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.errorChannel.collect {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    scaffoldState.snackbarHostState.showSnackbar(it, "Okay")
                }
            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize().scrollable(scrollState, orientation = Orientation.Vertical),
        scaffoldState = scaffoldState,
    ) { padding ->
        Column(
        ) {
            StackedSearchBar(navController) //SearchBarFunctionality
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                AnimatedContent(
                    targetState = state.isLoading,
                    transitionSpec = {
                        fadeIn() with fadeOut()
                    }
                ) { isLoading ->
                    if (isLoading)
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    if (!isLoading)
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.restaurantList) { restaurant ->
                                Column(modifier = Modifier.clickable { }) {
                                    Text(
                                        text = restaurant.name,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(2.dp)
                                    )
                                }
                            }
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CltButton(
                                        modifier = Modifier.fillMaxWidth(0.5f),
                                        text = "Log Out",
                                        withLoading = true,
                                        enabled = true,
                                        onClick = {
                                            homeViewModel.logout()
                                            navController.navigateToAuthScreen(
                                                popUpTo = Screen.HomeScreen.route
                                            )
                                        }
                                    )
                                }
                            }
                        }
                }
            }
        }
    }

}

@Composable
fun StackedSearchBar(navController: NavHostController) {
    Box(
        modifier = Modifier
            .height(200.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.secondary
                        )
                    )
                )
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    Icons.Filled.Home, "Home Icon", modifier = Modifier
                        .size(50.dp),
                    tint = Color(
                        parseColor("#5B3256")
                    )
                )
                Text(
                    text = "Home",
                    color = Color(parseColor("#5B3256")),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.align(CenterVertically),
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Filled.Settings, "Settings Icon", modifier = Modifier
                        .size(50.dp),
                    tint = Color(
                        parseColor("#5B3256")
                    )
                )
            }

        }
        OutlinedButton(
            onClick = { navController.navigate(Screen.SearchScreen.route) },
            border = BorderStroke(1.dp, color = Color.Black),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.45f)
                .zIndex(2f)
                .clip(RoundedCornerShape(25.dp))
                .graphicsLayer {
                    translationY = 0f
                    shadowElevation = 100f
                }
                .background(Color.White),
        ) {
            Row {
                Icon(
                    Icons.Outlined.Search,
                    modifier = Modifier.size(30.dp),
                    contentDescription = "Search Icon"
                )
                Spacer(Modifier.weight(0.5f))
                Text(
                    "Search your Restaurants Here!",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(Modifier.weight(1f))
                Text("")
            }
        }
    }
}

@Composable
fun StackedRestaurantDisplayItems() {
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .height(250.dp)
            .background(Color.White)
    ) {
    }
}
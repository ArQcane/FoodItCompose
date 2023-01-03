package com.example.restaurant.home

import android.graphics.Color.parseColor
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.common.utils.Screen
import com.example.restaurant.home.components.RestaurantCard
import com.example.restaurant.home.components.ShimmerLoadingCardPlaceholder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    val scrollState = rememberScrollState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val restaurantState by homeViewModel.restaurantState.collectAsState()

    val expensiveRestaurantState by homeViewModel.expensiveRestaurantState.collectAsState()




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

    fun navigateToRestaurantScreen(restaurantId: String) {
        navController.navigate("restaurant/$restaurantId")
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState,
    ) { padding ->
        Column() {
            StackedSearchBar(navController) //SearchBarFunctionality
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                AnimatedContent(
                    targetState = restaurantState.isLoading,
                    transitionSpec = {
                        fadeIn() with fadeOut()
                    }
                ) { isLoading ->
                    if (isLoading)
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    text = "All Restaurants",
                                    style = MaterialTheme.typography.h5,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.5f),
                                ) {
                                    LazyRow {
                                        items(10) {
                                            ShimmerLoadingCardPlaceholder()
                                        }
                                    }
                                }
                                Text(
                                    text = "Featured Restaurants",
                                    style = MaterialTheme.typography.h5,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, start = 10.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.5f),
                                ) {
                                    LazyRow {
                                        items(10) {
                                            ShimmerLoadingCardPlaceholder()
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.padding(28.dp))
                            }
                        }
                    if (!isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    text = "All Restaurants",
                                    style = MaterialTheme.typography.h5,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.5f),
                                ) {
                                    LazyRow {
                                        items(restaurantState.restaurantList) {
                                            RestaurantCard(
                                                restaurant = it,
                                                toggleFavourite = { restaurantId ->
                                                    print("restaurantId: $restaurantId")
                                                    homeViewModel.toggleFavorite(restaurantId)
                                                },
                                                navigateToRestaurantScreen = { restaurantId ->
                                                    Log.d("restaurantId:", restaurantId)
                                                    navController.navigate("restaurant/$restaurantId")
                                                }
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "Featured Restaurants",
                                    style = MaterialTheme.typography.h5,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, start = 10.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.5f),
                                ) {
                                    LazyRow {
                                        items(expensiveRestaurantState.restaurantList) {
                                            RestaurantCard(restaurant = it,
                                                toggleFavourite = { restaurantId ->
                                                    homeViewModel.toggleFavorite(restaurantId)
                                                },
                                                navigateToRestaurantScreen = { restaurantId ->
                                                    navigateToRestaurantScreen(restaurantId.toString())
                                                }
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.padding(28.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StackedSearchBar(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.2f),
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
            }
        }
        OutlinedButton(
            onClick = { navController.navigate(Screen.SearchScreen.route) },
            border = BorderStroke(1.dp, color = Color.Black),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.55f)
                .zIndex(3f)
                .graphicsLayer {
                    translationY = 40f
                    shadowElevation = 100f
                },
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
                    modifier = Modifier.align(CenterVertically)
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
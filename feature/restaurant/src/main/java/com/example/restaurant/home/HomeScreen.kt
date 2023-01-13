package com.example.restaurant.home

import android.graphics.Color.parseColor
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.common.navigation.searchScreenRoute
import com.example.restaurant.R
import com.example.restaurant.home.components.RestaurantCard
import com.example.restaurant.home.components.ShimmerLoadingCardPlaceholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
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
        scaffoldState = scaffoldState,
    ) { padding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = restaurantState.isRefreshing),
            onRefresh = { homeViewModel.refreshPage() }
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
            ) {
                StackedSearchBar(navController) //SearchBarFunctionality
                Spacer(modifier = Modifier.padding(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()

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
                                                        homeViewModel.toggleFavorite(restaurantId)
                                                    },
                                                    navigateToRestaurantScreen = { restaurantId ->
                                                        navController.navigate("restaurant/$restaurantId")
                                                    }
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "Highest Rating Restaurants",
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
                                        FeaturedRestaurantsSection(
                                            restaurantState,
                                            homeViewModel,
                                            navController
                                        )
                                    }
                                    Text(
                                        text = "Favourite Restaurants",
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
                                        FavoriteRestaurantSection(
                                            restaurantState,
                                            homeViewModel,
                                            navController
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FeaturedRestaurantsSection(
    state: HomeState,
    homeViewModel: HomeViewModel,
    navController: NavHostController
) {
    AnimatedContent(
        targetState = state.isLoading,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(durationMillis = 350)
            ) with fadeOut(
                animationSpec = tween(durationMillis = 350)
            )
        }
    ) { isLoading ->
        if (isLoading) Row(
            modifier = Modifier.horizontalScroll(
                state = rememberScrollState(),
                enabled = false
            )
        ) {
            repeat(3) {
                ShimmerLoadingCardPlaceholder(
                )
            }
        }
        if (!isLoading) LazyRow {
            items(state.featuredRestaurants.size) {
                val index = state.featuredRestaurants[it]

                RestaurantCard(
                    restaurant = state.restaurantList[index],
                    toggleFavourite = { restaurantId ->
                        homeViewModel.toggleFavorite(restaurantId)
                    },
                    navigateToRestaurantScreen = { restaurantId ->
                        navController.navigate("restaurant/$restaurantId")
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavoriteRestaurantSection(
    state: HomeState,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
) {
    AnimatedContent(
        targetState = state.isLoading,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(durationMillis = 350)
            ) with fadeOut(
                animationSpec = tween(durationMillis = 350)
            )
        }
    ) { isLoading ->
        if (isLoading) Row(
            modifier = Modifier.horizontalScroll(
                state = rememberScrollState(),
                enabled = false
            )
        ) {
            repeat(3) {
                ShimmerLoadingCardPlaceholder(
                )
            }
        }
        if (!isLoading) Box {
            LazyRow {
                items(state.favRestaurants.size) {
                    val index = state.favRestaurants[it]

                    RestaurantCard(
                        restaurant = state.restaurantList[index],
                        toggleFavourite = { restaurantId ->
                            homeViewModel.toggleFavorite(restaurantId)
                        },
                        navigateToRestaurantScreen = { restaurantId ->
                            navController.navigate(
                                "restaurant/$restaurantId"
                            )
                        }
                    )
                }
            }
            if (state.favRestaurants.isEmpty()) {
                EmptyFavourites()
            }
        }
    }
}

@Composable
fun EmptyFavourites() {
    Card(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 4.dp,
        modifier = Modifier.border(
            width = 1.dp,
            color = Color.White.copy(0.4f),
            shape = RoundedCornerShape(27.dp)
        )
            .clip(RoundedCornerShape(27.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.no_favourites),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "No Favourites Yet!",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Please Add New Restaurants,\nTo Favourites To View Restaurants",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                    .fillMaxWidth(),
                letterSpacing = 1.sp,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primary,
            )
        }
    }
}

@Composable
private fun StackedSearchBar(navController: NavHostController) {
    Box(
        modifier = Modifier
            .height(140.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.primaryVariant,
                            MaterialTheme.colors.secondary
                        )
                    ),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomEnd = 50.dp,
                        bottomStart = 50.dp
                    ),
                )
        ) {
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(start = 16.dp)) {
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
            onClick = { navController.navigate(searchScreenRoute) },
            border = BorderStroke(1.dp, color = Color.Black),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.55f)
                .zIndex(3f)
                .graphicsLayer {
                    translationY = 90f
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


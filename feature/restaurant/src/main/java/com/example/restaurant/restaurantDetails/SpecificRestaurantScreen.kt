package com.example.restaurant.restaurantDetails

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.common.components.GradientButton
import com.example.common.components.ImageBitmapFromNetwork
import com.example.common.components.CustomInputTextField
import com.example.common.navigation.homeScreenRoute
import com.example.common.theme.Shapes
import com.example.common.theme.primary
import com.example.common.theme.secondary
import com.example.domain.restaurant.TransformedRestaurantAndReview
import com.example.restaurant.R
import com.example.restaurant.home.utils.AppBarCollapsedHeight
import com.example.restaurant.home.utils.AppBarExpendedHeight
import com.example.restaurant.restaurantDetails.components.MoreRestaurauntDetailsSection
import com.example.restaurant.restaurantDetails.components.Reviews
import com.example.restaurant.restaurantDetails.reviews.create.ReviewEvent
import com.example.restaurant.restaurantDetails.utils.TabItem
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import java.lang.Float.max
import java.lang.Integer.min
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SpecificRestaurantScreen(
    navController: NavHostController,
    specificRestaurantViewModel: SpecificRestaurantViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val restaurantState by specificRestaurantViewModel.specificRestaurantState.collectAsState()

    val config = LocalConfiguration.current

    LaunchedEffect(true) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                specificRestaurantViewModel.errorChannel.collect {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    scaffoldState.snackbarHostState.showSnackbar(it, "Okay")
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) { padding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = restaurantState.isRefreshing),
            onRefresh = { specificRestaurantViewModel.refreshPage() }
        ) {
            AnimatedContent(
                targetState = restaurantState.isLoading,
                transitionSpec = {
                    fadeIn() with fadeOut()
                }
            ) { isLoading ->
                if (!isLoading) {
                    Content(
                        navController ,
                        specificRestaurantViewModel,
                        restaurantState,
                        restaurantState.transformedRestaurant,
                        scrollState
                    )
                    ParallaxToolbar(
                        navController,
                        specificRestaurantViewModel,
                        restaurantState.transformedRestaurant,
                        scrollState
                    )
                }
                if (isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            if (restaurantState.reviewBeingEdited != null) Dialog(
                properties = DialogProperties(
                    dismissOnBackPress = !restaurantState.isEditSubmitting,
                    dismissOnClickOutside = !restaurantState.isEditSubmitting
                ),
                onDismissRequest = {
                    specificRestaurantViewModel.onEvent(
                        ReviewEvent.OnCloseEditReviewDialog
                    )
                }
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((config.screenHeightDp * 0.2).dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 10.dp
                ) {
                    val focusManager = LocalFocusManager.current
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        CustomInputTextField(
                            value = restaurantState.editingReviewValue,
                            label = "Review",
                            error = restaurantState.editingReviewError,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            onValueChange = {
                                specificRestaurantViewModel.onEvent(
                                    ReviewEvent.OnEditReview(
                                        review = it
                                    )
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) {
                                TextButton(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(35.dp),
                                    contentPadding = PaddingValues(5.dp),
                                    onClick = {
                                        focusManager.clearFocus()
                                        specificRestaurantViewModel.onEvent(
                                            ReviewEvent.OnEditRating(
                                                rating = it + 1
                                            )
                                        )
                                    }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (it < restaurantState.editingRatingValue) {
                                                Icons.Default.Star
                                            } else {
                                                Icons.Default.StarBorder
                                            },
                                            contentDescription = null,
                                            tint = MaterialTheme.colors.primary
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            GradientButton(
                                text = "Submit",
                                withLoading = true,
                                enabled = !restaurantState.isEditSubmitting,
                                onClick = {
                                    focusManager.clearFocus()
                                    specificRestaurantViewModel.onEvent(
                                        ReviewEvent.OnCompleteEdit
                                    )
                                }
                            )
                            AnimatedVisibility(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp),
                                visible = restaurantState.editingRatingError != null,
                                enter = fadeIn() + slideInHorizontally(animationSpec = spring()),
                            ) {
                                restaurantState.editingRatingError?.let {
                                    Text(
                                        text = it,
                                        color = MaterialTheme.colors.error,
                                        style = MaterialTheme.typography.body1,
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

@Composable
fun ParallaxToolbar(
    navController: NavHostController,
    specificRestaurantViewModel: SpecificRestaurantViewModel,
    transformedRestaurant: TransformedRestaurantAndReview,
    scrollState: LazyListState
) {

    val imageHeight = AppBarExpendedHeight - AppBarCollapsedHeight

    val maxOffset =
        with(LocalDensity.current) { imageHeight.roundToPx() } - LocalWindowInsets.current.systemBars.layoutInsets.top

    val offset = min(scrollState.firstVisibleItemScrollOffset, maxOffset)

    val offsetProgress = max(0f, offset * 3f - 2f * maxOffset) / maxOffset

    TopAppBar(
        contentPadding = PaddingValues(),
        backgroundColor = White,
        modifier = Modifier
            .height(
                AppBarExpendedHeight
            )
            .offset { IntOffset(x = 0, y = -offset) },
        elevation = if (offset == maxOffset) 4.dp else 0.dp
    ) {
        Column {
            Box(
                Modifier
                    .height(imageHeight)
                    .graphicsLayer {
                        alpha = 1f - offsetProgress
                    }) {
                ImageBitmapFromNetwork(
                    contentScale = ContentScale.Crop,
                    url = transformedRestaurant.restaurant_logo,
                    placeholder = {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    Pair(0.4f, Transparent),
                                    Pair(1f, White)
                                )
                            )
                        )
                )
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .clip(Shapes.small)
                            .background(MaterialTheme.colors.secondary)
                            .padding(vertical = 6.dp, horizontal = 16.dp)
                    ) {
                        Row(){
                            Icon(
                                Icons.Filled.RestaurantMenu,
                                "Cuisine",
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                transformedRestaurant.cuisine,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(AppBarCollapsedHeight),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    transformedRestaurant.name,
                    fontSize = 26.sp,
                    fontWeight = Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(horizontal = (16 + 28 * offsetProgress).dp)
                        .scale(1f - 0.25f * offsetProgress)
                )

            }
        }
    }
    val previousScreen = navController.previousBackStackEntry?.destination?.route
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(AppBarCollapsedHeight)
            .padding(horizontal = 16.dp)
    ) {
        CircularButton(R.drawable.ic_arrow_back, onClick = {
            navController.navigate(
                previousScreen
                    ?: homeScreenRoute
            )
        })
        FavouriteButton(
            transformedRestaurant = transformedRestaurant,
            toggleFavourite = { specificRestaurantViewModel.toggleFavorite(transformedRestaurant.id.toString()) })
    }
}

@Composable
fun FavouriteButton(
    transformedRestaurant: TransformedRestaurantAndReview,
    color: Color = Gray,
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    toggleFavourite: (String) -> Unit,
) {
    TextButton(
        onClick = { toggleFavourite(transformedRestaurant.id.toString()) },
        contentPadding = PaddingValues(),
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(backgroundColor = White, contentColor = color),
        elevation = elevation,
        modifier = Modifier
            .width(38.dp)
            .height(38.dp)
    ) {
        Icon(
            imageVector = getFavoriteIcon(transformedRestaurant.isFavouriteByCurrentUser),
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
        )
    }
}

private fun getFavoriteIcon(isFavourited: Boolean): ImageVector {
    if (isFavourited) return Icons.Default.Favorite
    return Icons.Default.FavoriteBorder
}

@Composable
fun CircularButton(
    @DrawableRes iconResouce: Int,
    color: Color = Gray,
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    onClick: () -> Unit = {}
) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(),
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(backgroundColor = White, contentColor = color),
        elevation = elevation,
        modifier = Modifier
            .width(38.dp)
            .height(38.dp)
    ) {
        Icon(painterResource(id = iconResouce), tint = colors.primary, contentDescription = null)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Content(
    navController: NavHostController,
    specificRestaurantViewModel: SpecificRestaurantViewModel,
    specificRestaurantState: SpecificRestaurantState,
    transformedRestaurantAndReview: TransformedRestaurantAndReview,
    scrollState: LazyListState
) {
    LazyColumn(contentPadding = PaddingValues(top = AppBarExpendedHeight), state = scrollState) {
        item {
            BasicInfo(transformedRestaurantAndReview)
            Description(transformedRestaurantAndReview)
            TabHeader(
                navController = navController,
                specificRestaurantViewModel,
                specificRestaurantState = specificRestaurantState,
                transformedRestaurantAndReview
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TabHeader(
    navController: NavHostController,
    specificRestaurantViewModel: SpecificRestaurantViewModel,
    specificRestaurantState: SpecificRestaurantState,
    transformedRestaurant: TransformedRestaurantAndReview
) {
    val pagerState = rememberPagerState()

    val tabs = listOf(
        TabItem.MoreRestaurantDetailScreen() {
            MoreRestaurauntDetailsSection(
                transformedRestaurant
            )
        },
        TabItem.Reviews() {
            Reviews(
                navController = navController,
                specificRestaurantViewModel = specificRestaurantViewModel,
                specificRestaurantState = specificRestaurantState,
                transformedRestaurant = transformedRestaurant
            )
        }
    )

    val coroutineScope = rememberCoroutineScope()
    Column {
        TabRow(
            modifier = Modifier
                .clip(Shapes.medium)
                .background(Transparent),
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = secondary,
            contentColor = colors.primary
        ) {
            tabs.forEachIndexed { index, tabItem ->
                val color = remember {
                    Animatable(primary)
                }
                LaunchedEffect(key1 = pagerState.currentPage == index) {
                    color.animateTo(if (pagerState.currentPage == index) Color.White else secondary)
                }

                Tab(
                    modifier = Modifier
                        .height(48.dp)
                        .background(
                            color = color.value,
                            shape = Shapes.medium
                        ),
                    selected = pagerState.currentPage == index,
                    selectedContentColor = colors.primary,
                    unselectedContentColor = colors.primary.copy(
                        alpha = 0.5f
                    ),
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Text(
                        text = tabItem.title,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }
        HorizontalPager(
            verticalAlignment = Alignment.Top,
            count = tabs.size,
            state = pagerState,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) { page ->
            tabs[page].composable()
        }
    }
}

@Composable
fun Description(transformedRestaurant: TransformedRestaurantAndReview) {
    Text(
        text = transformedRestaurant.biography,
        fontSize = 16.sp,
        fontWeight = Medium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

@Composable
fun BasicInfo(transformedRestaurant: TransformedRestaurantAndReview) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        InfoColumn(
            R.drawable.ic_baseline_star_24,
            String.format("%.2f", transformedRestaurant.averageRating), "Stars"
        )
        InfoColumn(
            R.drawable.ic_baseline_speaker_notes_24,
            transformedRestaurant.ratingCount.toString(),
            "Ratings made"
        )
        InfoColumn(
            R.drawable.ic_baseline_attach_money_24,
            transformedRestaurant.avg_price.toString(),
            "Pricy-ness"
        )
    }
}

@Composable
fun InfoColumn(@DrawableRes iconResouce: Int, mainText: String, subText: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconResouce),
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.height(24.dp)
        )
        Text(text = mainText, fontWeight = Bold)
        Text(text = subText, fontSize = 12.sp, fontWeight = Medium, color = Gray)
    }
}
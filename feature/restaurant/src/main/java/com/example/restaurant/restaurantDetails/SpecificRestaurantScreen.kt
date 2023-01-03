package com.example.restaurant.restaurantDetails

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.common.components.CltImageFromNetwork
import com.example.common.navigation.homeScreenRoute
import com.example.common.navigation.restaurantDetailRoute
import com.example.common.theme.Shapes
import com.example.domain.restaurant.TransformedRestaurantAndReview
import com.example.domain.review.TransformedReview
import com.example.restaurant.R
import com.example.restaurant.home.components.ShimmerLoadingCardPlaceholder
import com.example.restaurant.home.utils.AppBarCollapsedHeight
import com.example.restaurant.home.utils.AppBarExpendedHeight
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.collect
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
    ) {
        AnimatedContent(
            targetState = restaurantState.isLoading,
            transitionSpec = {
                fadeIn() with fadeOut()
            }
        ) { isLoading ->
            if (!isLoading) {
                Content(restaurantState.transformedRestaurant, scrollState)
                ParallaxToolbar(navController, specificRestaurantViewModel, restaurantState.transformedRestaurant, scrollState)
            }
            if (isLoading) {
                ShimmerLoadingCardPlaceholder()
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
                CltImageFromNetwork(
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
                    Text(
                        transformedRestaurant.cuisine,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(Shapes.small)
                            .background(MaterialTheme.colors.secondary)
                            .padding(vertical = 6.dp, horizontal = 16.dp)
                    )
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
                    color= MaterialTheme.colors.primary,
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
        CircularButton(R.drawable.ic_arrow_back, onClick = { navController.navigate(previousScreen
            ?: homeScreenRoute) })
        FavouriteButton(transformedRestaurant= transformedRestaurant, toggleFavourite = { specificRestaurantViewModel.toggleFavorite(transformedRestaurant.id.toString()) })
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
        Icon(painterResource(id = iconResouce),tint= colors.primary, contentDescription =  null)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Content(
    transformedRestaurantAndReview: TransformedRestaurantAndReview,
    scrollState: LazyListState
) {
    LazyColumn(contentPadding = PaddingValues(top = AppBarExpendedHeight), state = scrollState) {
        item {
            BasicInfo(transformedRestaurantAndReview)
            Description(transformedRestaurantAndReview)
//            ServingCalculator()
            TabHeader()
//            IngredientsList(transformedRestaurant)
//            ShoppingListButton()
            Reviews(transformedRestaurantAndReview)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Reviews(transformedRestaurant: TransformedRestaurantAndReview) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
        ) {
            Text(
                text = "Reviews",
                fontWeight = Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 16.dp)
            )
            for (items in transformedRestaurant.reviews) {
                reviewCard(items)
                Spacer(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun reviewCard(review: TransformedReview) {
    val cleanImage: String =
        review.user.profile_pic.replace("data:image/png;base64,", "")
            .replace("data:image/jpeg;base64,", "")
    val decodedString: ByteArray = Base64.getDecoder().decode(cleanImage)
    val decodedByte =
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size).asImageBitmap()


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(color = White)
    )
    {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    contentScale = ContentScale.Fit,
                    bitmap = decodedByte,
                    contentDescription = "User Profile Picture",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(
                            CircleShape
                        )
                )
                Text(
                    review.user.username,
                    fontSize = 18.sp,
                    fontWeight = Bold,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = review.rating.toString(),
                    fontWeight = Medium,
                )
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = MaterialTheme.colors.primary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = review.review,
                    fontWeight = Medium,
                )
            }
        }
    }
}

//@Composable
//fun ShoppingListButton() {
//    Button(
//        onClick = { /*TODO*/ },
//        elevation = null,
//        shape = Shapes.small,
//        colors = ButtonDefaults.buttonColors(
//            backgroundColor = LightGray,
//            contentColor = Color.Black
//        ), modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Text(text = "Add to shopping list", modifier = Modifier.padding(8.dp))
//    }
//}

@Composable
fun TabHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clip(Shapes.medium)
            .background(LightGray)
            .fillMaxWidth()
            .height(44.dp)
    ) {
        TabButton("Ratings", true, Modifier.weight(1f))
        TabButton("Tools", false, Modifier.weight(1f))
    }
}

@Composable
fun TabButton(text: String, active: Boolean, modifier: Modifier) {
    Button(
        onClick = { /*TODO*/ },
        shape = Shapes.medium,
        modifier = modifier.fillMaxHeight(),
        elevation = null,
        colors = if (active) ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = White
        ) else ButtonDefaults.buttonColors(
            backgroundColor = LightGray,
            contentColor = DarkGray
        )
    ) {
        Text(text)
    }
}

@Composable
fun Description(transformedRestaurant: TransformedRestaurantAndReview) {
    Text(
        text = transformedRestaurant.biography,
        fontWeight = Medium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

@Composable
fun googleMap() {
    TODO()
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
            transformedRestaurant.averageRating.toString(), "Stars"
        )
        InfoColumn(
            R.drawable.ic_baseline_speaker_notes_24,
            transformedRestaurant.ratingCount.toString() ,
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
        Text(text = subText, fontSize= 12.sp, fontWeight = Medium, color = Gray)
    }
}
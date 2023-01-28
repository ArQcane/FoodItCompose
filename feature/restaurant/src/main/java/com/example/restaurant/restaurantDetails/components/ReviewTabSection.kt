package com.example.restaurant.restaurantDetails.components

import android.graphics.BitmapFactory
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.common.components.CltButton
import com.example.common.navigation.createReviewRoute
import com.example.common.navigation.homeScreenRoute
import com.example.domain.restaurant.TransformedRestaurantAndReview
import com.example.domain.review.TransformedReview
import com.example.restaurant.restaurantDetails.SpecificRestaurantState
import com.example.restaurant.restaurantDetails.SpecificRestaurantViewModel
import com.example.restaurant.restaurantDetails.reviews.create.CreateReviewScreen
import com.example.restaurant.restaurantDetails.reviews.create.ReviewEvent
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Reviews(
    navController: NavHostController,
    specificRestaurantViewModel: SpecificRestaurantViewModel,
    specificRestaurantState: SpecificRestaurantState,
    transformedRestaurant: TransformedRestaurantAndReview
) {
    fun navigateToCreateReview(restaurantId: String) {
        navController.navigate("createReview/$restaurantId")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Reviews",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 16.dp)
            )
            CltButton(onClick = { navController.navigate("createReview/${transformedRestaurant.id}") }) {
                Text(text = "Create a review now!", color = Color.White)
            }
            if (transformedRestaurant.reviews.isEmpty()) {
                EmptyReviews()
            }
            if (transformedRestaurant.reviews.isNotEmpty()) {
                Column() {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp, top = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = buildAnnotatedString(restaurant = transformedRestaurant))
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.End
                            ) {
                                Spacer(modifier = Modifier.height(10.dp))
                                repeat(5) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row {
                                            Text(text = "${5 - it}", fontSize = 12.sp)
                                            repeat(5 - it) {
                                                Icon(
                                                    modifier = Modifier.size(15.dp),
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colors.primary
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(5.dp))
                                        LinearProgressIndicator(
                                            progress = calculatePercentOfUsers(
                                                reviews = transformedRestaurant.reviews,
                                                rating = 5 - it.toDouble()
                                            ),
                                            modifier = Modifier
                                                .width(150.dp)
                                                .height(10.dp)
                                                .clip(CircleShape),
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    for (index in 0 until transformedRestaurant.reviews.size) {
                        reviewCard(
                            review = transformedRestaurant.reviews[index],
                            currentUserId = specificRestaurantState.currentUserId.toString(),
                            updateReview = {
                                specificRestaurantViewModel.onEvent(
                                    ReviewEvent.OpenEditReviewDialog(
                                        index = index
                                    )
                                )
                            },
                            deleteReview = {
                                specificRestaurantViewModel.onEvent(
                                    ReviewEvent.DeleteReview(index = index)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun reviewCard(
    review: TransformedReview,
    currentUserId: String,
    updateReview: () -> Unit,
    deleteReview: () -> Unit
) {
    var isMenuExpanded by remember {
        mutableStateOf(false)
    }
    val cleanImage: String =
        review.user.profile_pic.replace("data:image/png;base64,", "")
            .replace("data:image/jpeg;base64,", "")
    val decodedString: ByteArray = Base64.getDecoder().decode(cleanImage)
    val decodedByte =
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size).asImageBitmap()



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .heightIn(min = 150.dp)
            .background(color = Color.White)
    )
    {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
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
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
                if (currentUserId == review.iduser.toString()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(contentAlignment = Alignment.Center) {
                        IconButton(
                            modifier = Modifier.offset(x = 10.dp),
                            onClick = { isMenuExpanded = true }
                        ) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    isMenuExpanded = false
                                    updateReview()
                                }
                            ) {
                                Text(text = "Update Review")
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            DropdownMenuItem(
                                onClick = {
                                    isMenuExpanded = false
                                    deleteReview()
                                }
                            ) {
                                Text(text = "Delete Review")
                            }
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = review.rating.toString(),
                    fontWeight = FontWeight.Medium,
                )
                Row() {
                    repeat(review.rating.toInt()) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = review.review,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun EmptyReviews() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            shape = RoundedCornerShape(10.dp),
            elevation = 10.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            color = MaterialTheme.colors.secondary.copy(
                                alpha = 0.9f
                            )
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (it == 0) 1f else 0.7f)
                                .height(15.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    color = MaterialTheme.colors.secondary.copy(
                                        alpha = 0.8f
                                    )
                                )
                        )
                        if (it == 0) Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
        val onBackground = MaterialTheme.colors.onBackground
        Text(text = buildAnnotatedString {
            withStyle(ParagraphStyle(textAlign = TextAlign.Center)) {
                withStyle(SpanStyle(fontSize = 24.sp)) {
                    append("Currently no reviews existing...\n")
                }
                withStyle(
                    SpanStyle(
                        color = onBackground.copy(
                            alpha = 0.7f
                        )
                    )
                ) {
                    append("Add a your review now!")
                }
            }
        })
    }
}

private fun calculatePercentOfUsers(reviews: List<TransformedReview>, rating: Double): Float {
    val groupedReviews = reviews.groupBy { it.rating }
    val countOfUsersWithRating = groupedReviews[rating]?.size?.toFloat() ?: 0f
    return countOfUsersWithRating / reviews.size
}

@Composable
private fun buildAnnotatedString(restaurant: TransformedRestaurantAndReview) =
    buildAnnotatedString {
        val onBackground = MaterialTheme.colors.onBackground
        val primary = MaterialTheme.colors.primary
        withStyle(
            ParagraphStyle(
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
            )
        ) {
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 54.sp,
                    color = primary.copy(
                        alpha = 0.8f
                    )
                )
            ) {
                append(String.format("%.2f", restaurant.averageRating))
                append("\n")
            }
            withStyle(
                SpanStyle(
                    fontSize = 18.sp,
                    color = onBackground.copy(
                        alpha = 0.7f
                    )
                )
            ) {
                append("out of 5 stars")
                append("\n")
            }
            withStyle(
                SpanStyle(
                    fontSize = 14.sp,
                    color = onBackground.copy(
                        alpha = 0.5f
                    )
                )
            ) {
                append("${restaurant.ratingCount} reviews")
            }
        }
    }
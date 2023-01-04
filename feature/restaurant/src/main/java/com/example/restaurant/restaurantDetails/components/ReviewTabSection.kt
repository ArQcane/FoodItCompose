package com.example.restaurant.restaurantDetails.components

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.restaurant.TransformedRestaurantAndReview
import com.example.domain.review.TransformedReview
import java.util.*

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
                fontWeight = FontWeight.Bold,
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
            .background(color = Color.White)
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
                    fontWeight = FontWeight.Bold,
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
                    fontWeight = FontWeight.Medium,
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
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}
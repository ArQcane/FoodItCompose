package com.example.restaurant.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.components.CltImageFromNetwork
import com.example.domain.restaurant.TransformedRestaurant

@Composable
fun SearchedRestaurantGridCard(
    restaurant: TransformedRestaurant,
    navigateToRestaurant: (String) -> Unit,
    toggleFavourite: (String) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(android.graphics.Color.parseColor("#FFE2E2")),
        modifier = Modifier
            .height(260.dp)
            .padding(10.dp)
            .clickable { navigateToRestaurant(restaurant.id.toString()) },
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.wrapContentSize(),
                    color = Color(android.graphics.Color.parseColor("#CCD4E5"))
                ) {
                    Text(
                        text = restaurant.location,
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = restaurant.name,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Average Price Point: $${restaurant.avg_price.toInt()}",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.subtitle1,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Cuisine: ${restaurant.cuisine}",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.subtitle1,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = restaurant.averageRating.toString(),
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "/5")
                    Text(text = "(${restaurant.ratingCount})")
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(){
                    OutlinedButton(
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = androidx.compose.ui.graphics.Color.Black,
                            backgroundColor = androidx.compose.ui.graphics.Color.White
                        ),
                        onClick = { navigateToRestaurant(restaurant.id.toString()) }
                    ) {
                        Text(
                            text = "Read More",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.h4
                        )
                    }
                    TextButton(
                        modifier = Modifier
                            .offset(x = 5.dp)
                            .clip(CircleShape)
                            .size(40.dp),
                        onClick = { toggleFavourite(restaurant.id.toString()) }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getFavoriteIcon(restaurant.isFavouriteByCurrentUser),
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary,
                            )
                        }
                    }
                }

            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(width = 140.dp, height = 140.dp)
            ) {
                CltImageFromNetwork(
                    url = restaurant.restaurant_logo,
                    placeholder = {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }
    }
}

private fun getFavoriteIcon(isFavorited: Boolean): ImageVector {
    if (isFavorited) return Icons.Default.Favorite
    return Icons.Default.FavoriteBorder
}
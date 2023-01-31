package com.example.restaurant.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.common.components.ImageBitmapFromNetwork
import com.example.domain.restaurant.TransformedRestaurant


@Composable
fun RestaurantCard(
    restaurant: TransformedRestaurant,
    toggleFavourite: (String) -> Unit,
    navigateToRestaurantScreen: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable(onClick = { navigateToRestaurantScreen(restaurant.id.toString()) }),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.width(190.dp)) {
            ImageBitmapFromNetwork(
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
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
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
                    Text(text = "(${restaurant.ratingCount})", color = MaterialTheme.colors.primaryVariant, modifier = Modifier.padding(start = 4.dp))
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Price:", fontWeight = FontWeight.Bold)
                    repeat(restaurant.avg_price.toInt()) {
                        Icon(
                            imageVector = Icons.Filled.AttachMoney,
                            contentDescription = "Rating",
                            tint = MaterialTheme.colors.primaryVariant
                        )
                    }
                }
            }

        }
    }
}


private fun getFavoriteIcon(isFavourited: Boolean): ImageVector {
    if (isFavourited) return Icons.Default.Favorite
    return Icons.Default.FavoriteBorder
}
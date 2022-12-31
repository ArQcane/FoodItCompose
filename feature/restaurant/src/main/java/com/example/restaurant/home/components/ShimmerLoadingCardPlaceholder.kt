package com.example.restaurant.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.common.components.CltShimmerAnimation

@Composable
fun ShimmerLoadingCardPlaceholder(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.width(175.dp)) {
            CltShimmerAnimation(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(width = 2.dp, color = MaterialTheme.colors.primary)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CltShimmerAnimation(
                        modifier = Modifier
                            .weight(1f)
                            .height(25.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    CltShimmerAnimation(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                CltShimmerAnimation(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                )
            }
        }
    }
}
package com.example.restaurant.search.components


import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color


import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.components.CltImageFromNetwork
import com.example.common.components.CltShimmerAnimation

@Composable
fun ShimmerSearchPlaceholder() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(parseColor("#FFE2E2")),
        modifier = Modifier
            .height(260.dp)
            .padding(10.dp),
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
                ) {
                    CltShimmerAnimation(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                CltShimmerAnimation(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.height(2.dp))

                CltShimmerAnimation(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.height(2.dp))

                CltShimmerAnimation(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    CltShimmerAnimation(
                        modifier = Modifier
                            .weight(1f)
                            .height(25.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                CltShimmerAnimation(
                    modifier = Modifier
                        .background(color = Color.White)
                        .width(75.dp)
                        .height(25.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(width = 140.dp, height = 140.dp)
            ) {
                CltShimmerAnimation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }
    }
}
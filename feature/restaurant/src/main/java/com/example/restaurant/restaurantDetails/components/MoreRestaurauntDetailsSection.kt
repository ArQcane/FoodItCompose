package com.example.restaurant.restaurantDetails.components

import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.components.CltButton
import com.example.common.components.CltInput
import com.example.domain.restaurant.TransformedRestaurantAndReview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MoreRestaurauntDetailsSection(
    transformedRestaurantAndReview: TransformedRestaurantAndReview
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(1.35, 103.87),
            10f
        )
    }
    var isScrollEnabled by remember {
        mutableStateOf(true)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Map location of Restaurant",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(top = 8.dp,start = 8.dp, bottom = 16.dp)
                .align(Alignment.Start)
        )
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(width = 2.dp, color = MaterialTheme.colors.secondary)
                .motionEventSpy {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            isScrollEnabled = false
                        }
                        MotionEvent.ACTION_UP -> {
                            isScrollEnabled = true
                        }
                    }
                },
            cameraPositionState = cameraPositionState,
        ) {
            Marker(
                title = transformedRestaurantAndReview.name,
                state = MarkerState(
                    position = LatLng(
                        transformedRestaurantAndReview.location_lat.toDouble(),
                        transformedRestaurantAndReview.location_long.toDouble()
                    )
                )
            )
        }
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Opening Hours: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
            )
            Text(
                text = transformedRestaurantAndReview.opening_hours,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
            )
        }
        Row(
            modifier = Modifier.padding(start = 4.dp, end = 4.dp),

            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Region Restaurant situated: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
            )
            Text(
                text = transformedRestaurantAndReview.region,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
            )
        }
    }
}
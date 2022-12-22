package com.example.fooditcompose.ui.screens.home

import android.R
import android.graphics.Color.parseColor
import android.graphics.fonts.FontStyle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.authentication.navigationArgs.navigateToAuthScreen
import com.example.common.utils.Screen
import java.time.format.TextStyle

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()


    Scaffold(
        scaffoldState = scaffoldState
    ) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Button(onClick = {
//                homeViewModel.logout()
//                navController.navigateToAuthScreen()
//            }) {
//                Text(text = "Log Out")
//            }
//        }
        Column() {
            StackedSearchBar(navController) //SearchBarFunctionality
            StackedRestaurantDisplayItems() //Display basic restaurants
            StackedRestaurantDisplayItems() //Display featured restaurants
        }

    }

}

@Composable
fun StackedSearchBar(navController: NavHostController) {
    Box(
        modifier = Modifier
            .height(200.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.secondary
                        )
                    )
                )
        ) {
            Row(verticalAlignment = Alignment.Top) {
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
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Filled.Settings, "Settings Icon", modifier = Modifier
                        .size(50.dp),
                    tint = Color(
                        parseColor("#5B3256")
                    )
                )
            }

        }
        OutlinedButton(
            onClick = { navController.navigate(Screen.SearchScreen.route) },
            border = BorderStroke(1.dp, color = Color.Black),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.45f)
                .zIndex(2f)
                .clip(RoundedCornerShape(25.dp))
                .graphicsLayer {
                    translationY = 0f
                    shadowElevation = 100f
                }
                .background(Color.White),
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
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(Modifier.weight(1f))
                Text("")
            }
        }
    }
}

@Composable
fun StackedRestaurantDisplayItems() {
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .height(250.dp)
            .background(Color.White)
    ) {

    }
}
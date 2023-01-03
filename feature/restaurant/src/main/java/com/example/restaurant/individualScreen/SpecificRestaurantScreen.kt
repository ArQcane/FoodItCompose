package com.example.restaurant.individualScreen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.example.domain.restaurant.Restaurant
import com.example.restaurant.R
import com.example.restaurant.home.HomeViewModel
import com.example.restaurant.home.components.RestaurantCard
import com.example.restaurant.home.components.ShimmerLoadingCardPlaceholder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SpecificRestaurantScreen(
    navController: NavHostController,
    specificRestaurantViewModel: SpecificRestaurantViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()

    val scrollState = rememberScrollState()

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
        topBar = {
            TopAppBar(
                title = { Text(text = "Details of Todo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 30.dp)) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = restaurantState.transformedRestaurant.name,
                            style = MaterialTheme.typography.h5
                        )
                        Text(
                            text = restaurantState.transformedRestaurant.biography,
                            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Light)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }

}
package com.example.restaurant.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.common.components.CustomInputTextField
import com.example.restaurant.search.components.SearchedRestaurantGridCard
import com.example.restaurant.search.components.ShimmerSearchPlaceholder
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by searchViewModel.searchState.collectAsState()

    LaunchedEffect(true) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.errorChannel.collect {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    scaffoldState.snackbarHostState.showSnackbar(it, "Okay")
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
    ) { padding ->
        Column() {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.15f),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(
                            Color.White
                        )
                ) {
                    CustomInputTextField(
                        value = state.searchedQuery,
                        leadingIcon = {
                            IconButton(
                                onClick = { searchViewModel.onEvent(SearchEvent.OnSearch) },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Search"
                                )
                            }
                        },
                        label = "Search Your Restaurants Here!",
                        modifier = Modifier
                            .fillMaxWidth().padding(horizontal = 20.dp),
                        error = state.searchedQueryError,
                        onValueChange = {
                            searchViewModel.onEvent(
                                SearchEvent.OnSearchedQueryChanged(searchedQuery = it)
                            )
                        }
                    )
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(26.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .scrollable(
                            state = rememberScrollState(),
                            orientation = Orientation.Vertical
                        ),
                    contentPadding = PaddingValues(5.dp)
                ) {
                    if (state.isLoading)
                        items(10) {
                            ShimmerSearchPlaceholder()
                        }
                    if (!state.isLoading) {
                        items(state.searchedRestaurantList) {
                            SearchedRestaurantGridCard(
                                restaurant = it,
                                toggleFavourite = { restaurantId ->
                                    searchViewModel.toggleFavorite(restaurantId)
                                },
                                navigateToRestaurant = { restaurantId ->
                                    navController.navigate("restaurant/$restaurantId")
                                }
                            )
                        }
                        item() {
                            Spacer(modifier = Modifier.padding(28.dp))
                        }
                    }
                }

            }
        }
    }
}
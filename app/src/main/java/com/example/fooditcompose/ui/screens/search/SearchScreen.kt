package com.example.fooditcompose.ui.screens.search

import android.graphics.Color.parseColor
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.authentication.login.LoginEvent
import com.example.common.components.CltInput
import com.example.common.utils.Screen
import com.example.fooditcompose.ui.screens.search.components.SearchedRestaurantGridCard
import com.example.fooditcompose.ui.screens.search.components.ShimmerSearchPlaceholder
import com.example.restaurant.home.components.RestaurantCard
import com.example.restaurant.home.components.ShimmerLoadingCardPlaceholder
import kotlinx.coroutines.flow.collect
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
                            Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colors.primary,
                                    MaterialTheme.colors.secondary
                                )
                            )
                        )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .fillMaxHeight(0.7f)
                            .background(Color(parseColor("#F3F3F3")), RoundedCornerShape(5.dp))
                            .clip(shape = RoundedCornerShape(5.dp))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(onClick = { searchViewModel.onEvent(SearchEvent.OnSearch) }, modifier = Modifier.size(30.dp)) {
                                Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
                            }
                            Spacer(Modifier.weight(0.5f))
                            CltInput(
                                value = state.searchedQuery,
                                label = "Search Your Restaurants Here!",
                                modifier = Modifier
                                    .fillMaxWidth(),
                                error = state.searchedQueryError,
                                onValueChange = {
                                    searchViewModel.onEvent(
                                        SearchEvent.OnSearchedQueryChanged(searchedQuery = it)
                                    )
                                    searchViewModel.onEvent(SearchEvent.OnSearch)
                                }
                            )
                            Spacer(Modifier.weight(1f))
                            Text("")
                        }
                    }
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
                    if (!state.isLoading)
                        items(state.searchedRestaurantList) {
                            SearchedRestaurantGridCard(
                                restaurant = it,
                            )
                        }
                }
                Spacer(modifier = Modifier.padding(28.dp))
            }
        }
    }
}
package com.example.restaurant.restaurantDetails.reviews.create

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.common.components.GradientButton
import com.example.common.components.ImageBitmapFromNetwork
import com.example.common.components.CustomInputTextField
import com.example.restaurant.R
import com.example.restaurant.restaurantDetails.CircularButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun CreateReviewScreen(
    navController: NavHostController,
    createReviewViewModel: CreateReviewViewModel = hiltViewModel(),
) {

    val scaffoldState = rememberScaffoldState()
    val lifecycleOwner = LocalLifecycleOwner.current

    val state by createReviewViewModel.createReviewState.collectAsState()

    LaunchedEffect(true) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                createReviewViewModel.errorChannel.collect {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    scaffoldState.snackbarHostState.showSnackbar(it, "Okay")
                }
            }
        }
    }

    LaunchedEffect(state.isUpdated) {
        if (!state.isUpdated) return@LaunchedEffect
        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        scaffoldState.snackbarHostState.showSnackbar("Successfully Added Review!", "Dismiss")
    }


    Scaffold(
        scaffoldState = scaffoldState,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.1f),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            CircularButton(R.drawable.ic_arrow_back, onClick = {
                                navController.popBackStack()
                            })
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                "Create A new Review!",
                                color = White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
                AnimatedContent(
                    targetState = state.isLoading,
                    transitionSpec = {
                        fadeIn() with fadeOut()
                    }
                ){ isLoading ->
                    if(isLoading){
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    if(!isLoading){
                        Column {
                            DescriptionBox(createReviewViewModel)
                            Spacer(modifier = Modifier.padding(16.dp))
                            SendReviewBox(createReviewViewModel)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DescriptionBox(createReviewViewModel: CreateReviewViewModel) {
    val state by createReviewViewModel.createReviewState.collectAsState()
    Box(
        modifier = Modifier
            .height(260.dp)
            .padding(vertical = 10.dp)
            .background(White)
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
                Text(
                    text = state.transformedRestaurant.name,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h2,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Customer Reviews",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.subtitle1,
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Comment your experience in this establishment",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.subtitle1,
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(width = 140.dp, height = 140.dp)
            ) {
                ImageBitmapFromNetwork(
                    url = state.transformedRestaurant.restaurant_logo,
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SendReviewBox(
    createReviewViewModel: CreateReviewViewModel
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val bringIntoViewRequester = remember {
        BringIntoViewRequester()
    }
    val state by createReviewViewModel.createReviewState.collectAsState()

    Box(modifier = Modifier.background(White)) {
        Column() {
            CustomInputTextField(
                modifier = Modifier.onFocusEvent {
                    if (!it.isFocused) return@onFocusEvent
                    scope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }
                },
                value = state.review,
                label = "Review",
                error = state.reviewError,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                onValueChange = {
                    createReviewViewModel.onEvent(
                        ReviewEvent.OnReviewChangedEvent(
                            review = it
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) {
                    TextButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(35.dp),
                        contentPadding = PaddingValues(5.dp),
                        onClick = {
                            focusManager.clearFocus()
                            createReviewViewModel.onEvent(
                                ReviewEvent.OnRatingChangedEvent(
                                    rating = it + 1
                                )
                            )
                        }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (it < state.rating) {
                                    Icons.Default.Star
                                } else {
                                    Icons.Default.StarBorder
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                GradientButton(
                    text = "Submit",
                    withLoading = true,
                    enabled = !state.isSubmitting,
                    onClick = {
                        createReviewViewModel.onEvent(
                            ReviewEvent.OnSubmit
                        )

                    }
                )
            }
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                visible = state.ratingError != null,
                enter = fadeIn() + slideInHorizontally(animationSpec = spring()),
            ) {
                state.ratingError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        }
    }
}
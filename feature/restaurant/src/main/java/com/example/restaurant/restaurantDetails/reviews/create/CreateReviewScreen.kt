package com.example.restaurant.restaurantDetails.reviews.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.common.components.CltButton
import com.example.common.components.CltInput
import com.example.restaurant.restaurantDetails.SpecificRestaurantViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateReviewScreen(
    specificRestaurantViewModel: SpecificRestaurantViewModel,
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val bringIntoViewRequester = remember {
        BringIntoViewRequester()
    }

    val state by specificRestaurantViewModel.specificRestaurantState.collectAsState()

    Column {
        CltInput(
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
                specificRestaurantViewModel.onEvent(
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
                        specificRestaurantViewModel.onEvent(
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
            CltButton(
                text = "Submit",
                withLoading = true,
                enabled = !state.isSubmitting,
                onClick = {
                    specificRestaurantViewModel.onEvent(
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
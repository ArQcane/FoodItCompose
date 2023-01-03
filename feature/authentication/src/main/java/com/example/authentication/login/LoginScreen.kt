package com.example.authentication.login

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.authentication.R
import com.example.common.components.CltButton
import com.example.common.components.CltInput
import com.example.common.navigation.homeScreenRoute
import com.example.common.navigation.registerScreenRoute
import com.example.common.navigation.resetPasswordFromEmailRoute
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by loginViewModel.loginState.collectAsState()

    LaunchedEffect(state.isLoggedIn) {
        if (!state.isLoggedIn) return@LaunchedEffect
        navController.navigate(homeScreenRoute)
    }

    LaunchedEffect(true) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.errorChannel.collect {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    scaffoldState.snackbarHostState.showSnackbar(it, "Dismiss")
                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Image(
                modifier = Modifier.padding(40.dp),
                contentScale = ContentScale.Fit,
                painter = painterResource(id = R.drawable.foodit_high_resolution_logo_color_on_transparent_background),
                contentDescription = "FoodIt's Logo"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                    .background(MaterialTheme.colors.secondaryVariant),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        text = "Welcome Back!",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = colors.primary
                    )
                    Text(
                        text = "Log Back In!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colors.primaryVariant
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    CltInput(
                        value = state.username,
                        label = "Username",
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = state.usernameError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        onValueChange = {
                            loginViewModel.onEvent(
                                LoginEvent.OnUsernameChange(username = it)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    CltInput(
                        value = state.user_pass,
                        onValueChange = {
                            loginViewModel.onEvent(
                                LoginEvent.OnUserPassChange(user_pass = it)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = "Password",
                        error = state.userPassError,
                        isPassword = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        TextButton(
                            onClick = { navController.navigate(resetPasswordFromEmailRoute) }) {
                            Text(
                                text = "Forgot Password?",
                                fontSize = 12.sp,
                                color = colors.primaryVariant
                            )
                        }
                        TextButton(
                            onClick = { navController.navigate(registerScreenRoute) }) {
                            Text(
                                text = "Don't have an account yet?",
                                fontSize = 12.sp,
                                color = colors.primaryVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    CltButton(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading,
                        onClick = {
                            focusManager.clearFocus()
                            loginViewModel.onEvent(LoginEvent.OnSubmit)
                        }
                    ) {
                        AnimatedContent(targetState = state.isLoading) { isLoading ->
                            if (isLoading)
                                return@AnimatedContent CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 3.dp
                                )
                            Text(text = "Login", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
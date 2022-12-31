package com.example.authentication.forgetPassword

import android.media.Image
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.authentication.R
import com.example.authentication.login.LoginEvent

import com.example.authentication.navigationArgs.navigateToAuthScreen
import com.example.common.components.CltButton
import com.example.common.components.CltInput
import com.example.common.utils.Screen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ResetPasswordWithEmailScreen(
    navController: NavHostController,
    resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by resetPasswordViewModel.resetPasswordState.collectAsState()

    LaunchedEffect(state.isSent) {
        if (!state.isSent) return@LaunchedEffect
        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        scaffoldState.snackbarHostState.showSnackbar("Request Has been sent if email exists!", "Dismiss").also {
            navController.navigateToAuthScreen()
        }
    }
    LaunchedEffect(true) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                resetPasswordViewModel.errorChannel.collect {
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
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    Text(
                        text = "Reset Password?", style = TextStyle(
                            color = MaterialTheme.colors.primary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )
                    )
                    Text(
                        text = "Enter the email associated with\n" +
                                "your account and we'll send an \n" +
                                "email with instructions to reset\n" +
                                "your password. ", style = TextStyle(
                            color = Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                }
                Image(
                    modifier = Modifier
                        .height(140.dp)
                        .width(140.dp),
                    contentScale = ContentScale.Fit,
                    painter = painterResource(id = R.drawable.foodit_website_favicon_color),
                    contentDescription = "FoodIt's Logo"
                )
            }
            Spacer(modifier = Modifier.padding(12.dp))
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.icon_awesome_mail_bulk),
                contentDescription = "FoodIt's Logo"
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Upon sending the instructions to the email,\n" +
                        "please check your email address", style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                )
            )
            Spacer(modifier = Modifier.padding(16.dp))
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
                ){
                    Text(
                        text = "Forgotten Password?",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    CltInput(
                        value = state.email,
                        label = "Email",
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = state.emailError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        onValueChange = {
                            resetPasswordViewModel.onEvent(
                                ResetPasswordEvent.OnEmailChange(email = it)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    CltButton(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading,
                        onClick = {
                            focusManager.clearFocus()
                            resetPasswordViewModel.onEvent(ResetPasswordEvent.OnSubmit)
                        }
                    ) {
                        AnimatedContent(targetState = state.isLoading) { isLoading ->
                            if (isLoading)
                                return@AnimatedContent CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 3.dp
                                )
                            Text(text = "Send Instructions!", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
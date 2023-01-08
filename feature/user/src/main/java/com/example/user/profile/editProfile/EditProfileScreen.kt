package com.example.user.profile.editProfile

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color.parseColor
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import com.example.common.components.CltButton
import com.example.common.components.CltImagePicker
import com.example.common.components.CltInput
import com.example.common.navigation.homeScreenRoute
import com.example.common.navigation.profileScreenRoute
import com.example.user.profile.ProfileViewModel
import com.example.user.profile.profileScreenContent
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    val scrollState = rememberScrollState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val editProfileState by editProfileViewModel.editProfileState.collectAsState()
    LaunchedEffect(true) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editProfileViewModel.errorChannel.collect {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    scaffoldState.snackbarHostState.showSnackbar(it, "Okay")
                }
            }
        }
    }
    LaunchedEffect(editProfileState.isUpdated) {
        if (!editProfileState.isUpdated) return@LaunchedEffect
        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        scaffoldState.snackbarHostState.showSnackbar("Successfully Updated account!", "Dismiss")
            .also {
                navController.navigate(profileScreenRoute)
            }
    }
    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            CustomAppBar(navController, editProfileViewModel, editProfileState)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomAppBar(
    navController: NavHostController,
    editProfileViewModel: EditProfileViewModel,
    editProfileState: EditProfileState
) {
    val previousScreen = navController.previousBackStackEntry?.destination?.route
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
                    .fillMaxHeight(0.08f),
                contentAlignment = Alignment.Center
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
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            modifier = Modifier.padding(start = 16.dp),
                            onClick = {
                                navController.navigate(
                                    previousScreen
                                        ?: homeScreenRoute
                                )
                            }) {
                            Icon(
                                Icons.Filled.ArrowBackIos, "Home Icon", modifier = Modifier
                                    .size(30.dp),
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Edit Profile",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.align(Alignment.CenterVertically),
                        )
                    }
                }
            }
            EditProfileScreenContent(navController, editProfileViewModel, editProfileState)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditProfileScreenContent(
    navController: NavHostController,
    editProfileViewModel: EditProfileViewModel,
    editProfileState: EditProfileState
) {
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = "Update Your Account Details",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Please leave no field empty",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.padding(12.dp))
            CltImagePicker(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .wrapContentHeight()
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f)
                    .border(width = 2.dp, color = MaterialTheme.colors.primary),
                value = editProfileState.profile_pic,
                onValueChange = {
                    editProfileViewModel.onEvent(
                        event = EditProfileEvent.OnProfilePicChange(profile_pic = it)
                    )
                },
                error = editProfileState.profilePicError
            )
            Spacer(modifier = Modifier.padding(4.dp))
            CltInput(
                value = editProfileState.first_name,
                label = "First Name",
                modifier = Modifier
                    .fillMaxWidth(),
                error = editProfileState.firstNameError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onValueChange = {
                    editProfileViewModel.onEvent(
                        EditProfileEvent.OnFirstNameChange(first_name = it)
                    )
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))
            CltInput(
                value = editProfileState.last_name,
                label = "Last Name",
                modifier = Modifier
                    .fillMaxWidth(),
                error = editProfileState.lastNameError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onValueChange = {
                    editProfileViewModel.onEvent(
                        EditProfileEvent.OnLastNameChange(last_name = it)
                    )
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))
            CltInput(
                value = editProfileState.mobile_number.toString(),
                label = "User Mobile Number",
                modifier = Modifier
                    .fillMaxWidth(),
                error = editProfileState.mobileNumberError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onValueChange = {
                    if (it != "") {
                        editProfileViewModel.onEvent(
                            EditProfileEvent.OnMobileNumberChange(mobile_number = it.toLong())
                        )
                    } else {
                        editProfileViewModel.onEvent(
                            EditProfileEvent.OnMobileNumberChange(mobile_number = 65)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))
            CltInput(
                value = editProfileState.address,
                label = "User Address",
                modifier = Modifier
                    .fillMaxWidth(),
                error = editProfileState.addressError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onValueChange = {
                    editProfileViewModel.onEvent(
                        EditProfileEvent.OnAddressChange(address = it)
                    )
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))
            CltButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = !editProfileState.isLoading,
                onClick = {
                    focusManager.clearFocus()
                    editProfileViewModel.onEvent(EditProfileEvent.OnSubmit)
                }
            ) {
                AnimatedContent(targetState = editProfileState.isLoading) { isLoading ->
                    if (isLoading)
                        return@AnimatedContent CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 3.dp
                        )
                    Text(text = "Update!", color = Color.White)
                }
            }
        }
    }
}
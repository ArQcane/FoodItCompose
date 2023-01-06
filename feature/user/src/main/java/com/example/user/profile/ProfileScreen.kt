package com.example.user.profile

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.Color.parseColor
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.common.navigation.loginScreenRoute
import com.example.common.navigation.navigateToAuthScreen
import com.example.common.theme.Shapes
import com.example.user.profile.components.dialogs.CustomAlertDialog
import com.example.user.profile.components.dialogs.InputAlertDialog
import kotlinx.coroutines.launch
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    val scrollState = rememberScrollState()

    val lifecycleOwner = LocalLifecycleOwner.current
    val profileState by profileViewModel.profileState.collectAsState()
    LaunchedEffect(true) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.errorChannel.collect {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    scaffoldState.snackbarHostState.showSnackbar(it, "Okay")
                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
    ) {
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
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Filled.VerifiedUser, "Home Icon", modifier = Modifier
                                    .size(50.dp),
                                tint = Color(
                                    parseColor("#5B3256")
                                )
                            )
                            Text(
                                text = "Profile",
                                color = Color(parseColor("#5B3256")),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                modifier = Modifier.align(Alignment.CenterVertically),
                            )
                        }
                    }
                }
                AnimatedContent(
                    targetState = profileState.isLoading,
                    transitionSpec = {
                        fadeIn() with fadeOut()
                    }
                ) { isLoading ->
                    if (isLoading)
                        Column(verticalArrangement = Arrangement.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colors.primary)
                        }

                    if (!isLoading) {
                        profileScreenContent(navController, profileViewModel, profileState)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun profileScreenContent(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    profileState: ProfileState
) {

    val cleanImage: String =
        profileState.user.profile_pic.replace("data:image/png;base64,", "")
            .replace("data:image/jpeg;base64,", "")
    val decodedString: ByteArray = Base64.getDecoder().decode(cleanImage)
    val decodedByte =
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            .asImageBitmap()

    val context = LocalContext.current
    var showAlertDialog by remember {
        mutableStateOf(false)
    }
    var showInputDialog by remember {
        mutableStateOf(false)
    }
    Column() {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.25f)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.padding(top = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        contentScale = ContentScale.Crop,
                        bitmap = decodedByte,
                        contentDescription = "User Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(
                                CircleShape
                            )
                    )
                    Text(
                        profileState.user.username,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 12.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .border(width = 1.dp, color = Color.LightGray)
                .background(Color.White)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Total Reviews Made: ${profileState.totalReviews}",
                    color = MaterialTheme.colors.primary,

                    )
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    shape = Shapes.medium,
                    border = BorderStroke(1.dp, color = MaterialTheme.colors.primary),
                    modifier = Modifier.width(150.dp),
                ) {
                    Text("Edit Profile")
                }
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))
        UtilsActionBox(
            navController,
            profileViewModel,
            Icons.Filled.DeleteForever,
            "Deactivate Account?",
            "Note all data will be wiped upon confirmation",
            onClick = {
                showAlertDialog = !showAlertDialog
            }
        )
        UtilsActionBox(
            navController,
            profileViewModel,
            Icons.Filled.Password,
            "Reset Password?",
            "Follow the neccessary steps given to you later",
            onClick = {
                showInputDialog = !showInputDialog
            }
        )
        UtilsActionBox(
            navController,
            profileViewModel,
            Icons.Filled.Logout,
            "Log Out?",
            "Log out of the current account on this device",
            onClick = {
                profileViewModel.logout()
                navController.navigateToAuthScreen(
                    popUpTo = loginScreenRoute
                )
            }
        )
    }
    if (showAlertDialog) {
        CustomAlertDialog({
            showAlertDialog = !showAlertDialog
        }, {
            profileViewModel.deleteUser()
            navController.navigateToAuthScreen(
                popUpTo = loginScreenRoute
            )
        })
    }
    if (showInputDialog) {
        InputAlertDialog(
            {
                showInputDialog = !showInputDialog
            },
            { email ->
                if (email == profileState.user.email) {
                    profileViewModel.resetPassword(email)
                    Toast.makeText(context, "Successfully sent a request", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Entered wrong email address", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }
}

@Composable
fun UtilsActionBox(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    icon: ImageVector,
    mainText: String,
    subText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Delete Forever",
                modifier = Modifier.size(30.dp)
            )
            Column() {
                Text(
                    mainText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
                Text(
                    subText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
            Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "Arrow")
        }
    }
}
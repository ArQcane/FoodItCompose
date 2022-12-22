package com.example.authentication.forgetPassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.authentication.navigationArgs.loginScreenRoute
import com.example.common.utils.Screen


@Composable
fun ResetPasswordWithEmailScreen(
    email: String?,
    navController: NavHostController
){
    if(email == null) return run{
        navController.navigate(loginScreenRoute)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "email: $email")
        }
    }
}
package com.example.authentication.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.authentication.R
import com.example.authentication.navigationArgs.loginScreenRoute
import com.example.common.utils.Screen

@Composable
fun RegisterScreen(navController: NavHostController) {

    var usernameState by remember {
        mutableStateOf("")
    }
    var passwordState by remember {
        mutableStateOf("")
    }

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
                    text = "Are You A New User?",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "Make a new account!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primaryVariant
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "Username",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Italic
                )
                OutlinedTextField(
                    value = usernameState,
                    onValueChange = { usernameState = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(30.dp))
                        .border(
                            color = MaterialTheme.colors.primaryVariant,
                            width = 1.dp,
                            shape = RoundedCornerShape(30.dp)
                        ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                    ),
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "Password",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Italic
                )
                OutlinedTextField(
                    value = passwordState,
                    onValueChange = { passwordState = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(30.dp))
                        .border(
                            color = MaterialTheme.colors.primaryVariant,
                            width = 1.dp,
                            shape = RoundedCornerShape(30.dp)
                        ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                    ),
                )
                TextButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = { navController.navigate(loginScreenRoute) }) {
                    Text(
                        text = "Already have an account?",
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primaryVariant
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(300.dp)
                        .clip(RoundedCornerShape(50)),
                )
                {
                    Text(
                        text = "Register!",
                        fontSize = 18.sp,
                        color = Color.White,
                    )
                }
            }
        }
    }
}
package com.example.fooditcompose

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.authentication.navigationArgs.authScreenComposable
import com.example.common.navigation.loginScreenRoute
import com.example.common.theme.FoodItComposeTheme
import com.example.data.di.RepositoryModule
import com.example.domain.user.UserRepository
import com.example.fooditcompose.di.TestRepoModule
import com.example.restaurant.navigation.logInNavComposable
import com.example.test.tag.Tags
import com.example.test.user.TestUserRepo
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
class LoginTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var testUserRepo: UserRepository

    @OptIn(ExperimentalAnimationApi::class)
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            FoodItComposeTheme() {
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = loginScreenRoute
                ) {
                    authScreenComposable(navController)
                    logInNavComposable(navController)
                }
            }
        }
    }

    @Test
    fun loginWithEmptyField_showError() {
        composeRule.onNodeWithTag(Tags.LOGIN_BTN).assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Username required").assertIsDisplayed()
        composeRule.onNodeWithText("Password required").assertIsDisplayed()
    }

    @Test
    fun loginWithInvalidField_showError() {
        runBlocking {
            delay(1000L)
            composeRule.onNodeWithTag(Tags.USERNAME_FIELD).performTextInput("testUser123")
            composeRule.onNodeWithTag(Tags.PASSWORD_FIELD).performTextInput("wrong password")
            composeRule.onNodeWithTag(Tags.LOGIN_BTN).assertIsDisplayed().performClick()
            composeRule.onNodeWithText("Account does not exist").assertIsDisplayed()
        }
    }

    @Test
    fun loginWithValidField_navigateToHomeScreen() {
        runBlocking {
            val user = (testUserRepo as TestUserRepo).users.last()
            composeRule.onNodeWithTag(Tags.USERNAME_FIELD).performTextInput(user.username)
            composeRule.onNodeWithTag(Tags.PASSWORD_FIELD).performTextInput("12341234")
            composeRule.onNodeWithTag(Tags.LOGIN_BTN).assertIsDisplayed().performClick()
            composeRule.awaitIdle()
            composeRule.onNodeWithText("Home").assertIsDisplayed()
        }
    }
}
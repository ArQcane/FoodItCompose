package com.example.fooditcompose

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.example.authentication.navigationArgs.authScreenComposable
import com.example.common.navigation.homeScreenRoute
import com.example.common.navigation.loginScreenRoute
import com.example.common.navigation.profileScreenRoute
import com.example.common.theme.FoodItComposeTheme
import com.example.data.di.RepositoryModule
import com.example.domain.user.UserRepository
import com.example.restaurant.navigation.logInNavComposable
import com.example.test.tag.Tags
import com.example.test.user.TestUserRepo
import com.example.user.navigation.profileNavComposable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
class EditProfileFirstNameTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var device: UiDevice

    @OptIn(ExperimentalAnimationApi::class)
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            FoodItComposeTheme() {
                val navController = rememberAnimatedNavController()
                LaunchedEffect(true) {
                    navController.navigate(profileScreenRoute)
                }
                AnimatedNavHost(
                    navController = navController,
                    startDestination = homeScreenRoute
                ) {
                    logInNavComposable(navController)
                    profileNavComposable(navController)
                }
            }
        }
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun whenEditProfile_shouldUpdateUsername() {
        runBlocking {
            composeRule.onNodeWithTag(Tags.EDIT_PROFILE_NAVIGATION_BTN)
                .assertIsDisplayed()
                .performClick()
            composeRule.awaitIdle()
            composeRule.onNodeWithTag(Tags.FIRST_NAME_INPUT)
                .performTextClearance()
            composeRule.onNodeWithTag(Tags.FIRST_NAME_INPUT)
                .performTextInput("TESTFIRSTNAMECHANGE")
            composeRule.onNodeWithTag(Tags.UPDATE_PROFILE_BTN)
                .performClick()
            composeRule.awaitIdle()
            device.pressBack()
            composeRule.awaitIdle()
            composeRule.onNodeWithText("TESTFIRSTNAMECHANGE")
                .assertIsDisplayed()
        }
    }
}
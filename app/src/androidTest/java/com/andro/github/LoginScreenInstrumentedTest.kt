package com.andro.github

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andro.github.ui.activity.MainActivity
import com.andro.github.ui.screens.LoginScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenInstrumentedTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginPageShouldAllowInput() {
        var usernameInput = ""
        var passwordInput = ""

        composeTestRule.activity.setContent {
            LoginScreen({ username, password ->
                usernameInput = username
                passwordInput = password
            }) {}
        }

        composeTestRule.onNodeWithText("Username").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()

        composeTestRule.onNodeWithText("Username").performTextInput("testUser")

        composeTestRule.onNodeWithText("Password").performTextInput("testPass")

        composeTestRule.onNodeWithText("Username").assertTextContains("testUser")
        composeTestRule
            .onNodeWithText("Password")
            .assertTextContains("••••••••")

        composeTestRule.onNodeWithTag("LoginButton").performClick()

        assert(usernameInput == "testUser")
        assert(passwordInput == "testPass")
    }

    @Test
    fun testPasswordVisibilityToggle() {
        composeTestRule.activity.setContent {
            LoginScreen({ _, _ -> }) {}
        }

        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Password").performTextInput("testPass")

        // initial is invisible
        composeTestRule.onNodeWithText("testPass").assertDoesNotExist()
        composeTestRule.onNodeWithText("••••••••").assertExists()

        // toggle to visible
        composeTestRule.onNodeWithText("👁️").performClick()
        composeTestRule.onNodeWithText("testPass").assertExists()
        composeTestRule.onNodeWithText("🙈").assertExists()

        // toggle to visible
        composeTestRule.onNodeWithText("🙈").performClick()
        composeTestRule.onNodeWithText("testPass").assertDoesNotExist()
        composeTestRule.onNodeWithText("••••••••").assertExists()
    }

    @Test
    fun testOAthLinkClick() {
        var clicked = false
        val oauthLink = "OAuth2 Login"
        composeTestRule.activity.setContent {
            LoginScreen({ _, _ -> }) {
                clicked = true
            }
        }
        composeTestRule.onNodeWithText(oauthLink).assertExists()
        composeTestRule.onNodeWithText(oauthLink).performClick()

        assert(clicked)
    }
}

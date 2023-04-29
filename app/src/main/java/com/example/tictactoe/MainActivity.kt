package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tictactoe.app_navigation.AppNavigator
import com.example.tictactoe.ui.theme.Route
import com.example.tictactoe.ui.theme.TicTacToeTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberAnimatedNavController()
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            TicTacToeTheme {
                Scaffold(topBar = {
                    TopAppBar(backgroundColor = MaterialTheme.colors.primaryVariant) {
                        if (navController.previousBackStackEntry != null) {
                            Icon(imageVector = Icons.Default.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Setting Icon",
                                modifier = Modifier.clickable {
                                    navController.navigateUp()
                                })
                        }
                        Text(
                            text = getString(R.string.app_name),
                            fontSize = 22.sp,
                            textAlign = TextAlign.Left,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 8.dp),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (navController.previousBackStackEntry == null) {
                            Icon(painter = painterResource(id = R.drawable.baseline_settings_24),
                                tint = Color.White,
                                contentDescription = "Setting Icon",
                                modifier = Modifier.clickable {
                                    navController.navigate(Route.SETTING_SCREEN)
                                })
                        }
                    }
                }) {
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                        AppNavigator(navController)
                    }
                    it
                }
            }
        }
    }
}




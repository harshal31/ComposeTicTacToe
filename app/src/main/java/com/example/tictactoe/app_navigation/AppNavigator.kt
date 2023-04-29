package com.example.tictactoe.app_navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import animatedComposable
import com.example.tictactoe.game_screen.TicTacToeGameScreen
import com.example.tictactoe.select_grid_size.SelectGridScreenSize
import com.example.tictactoe.setting_screen.GameSettingScreen
import com.example.tictactoe.ui.theme.Route
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigator(navController: NavHostController) {
    AnimatedNavHost(navController, startDestination = Route.GRID_SCREEN) {

        animatedComposable(Route.GRID_SCREEN) {
            SelectGridScreenSize {
                navController.navigate(Route.GAME_SCREEN.plus("/${it}"))
            }
        }

        animatedComposable(Route.GAME_SCREEN.plus("/{size}"), arguments = listOf(navArgument("size") { type = NavType.IntType })) { backStackEntry ->
            val size = backStackEntry.arguments?.getInt("size") ?: 3
            TicTacToeGameScreen(size)
        }

        animatedComposable(Route.SETTING_SCREEN) {
            GameSettingScreen()
        }

    }
}
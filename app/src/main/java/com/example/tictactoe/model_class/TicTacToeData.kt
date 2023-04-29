/**
 * Copyright 2023 Lenovo, All Rights Reserved *
 */
package com.example.tictactoe.model_class

import androidx.compose.ui.graphics.Color

enum class GameWinner {
    X_WIN, O_WIN, NO_WINNER
}

sealed class TicTacToeGameState {
    data class XWin(val ticBox: List<TicBox>): TicTacToeGameState()
    data class OWin(val ticBox: List<TicBox>): TicTacToeGameState()
    object ContinuePlaying: TicTacToeGameState()
    data class GameOverWithoutResult(val message: String = "Game is over"): TicTacToeGameState()
}

data class TicBox(val col: Int, val row: Int, var id: String = "", var isWin: Boolean = false)

data class GameSetting(val settingName: String, var colorCode: Color, var index: Int = -1)


data class Digit(val digitChar: Char, val fullNumber: Int, val place: Int) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Digit -> digitChar == other.digitChar
            else -> super.equals(other)
        }
    }
}

operator fun Digit.compareTo(other: Digit): Int {
    return fullNumber.compareTo(other.fullNumber)
}
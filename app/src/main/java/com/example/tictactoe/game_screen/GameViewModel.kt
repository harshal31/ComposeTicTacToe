/**
 * Copyright 2023 Lenovo, All Rights Reserved *
 */
package com.example.tictactoe.game_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.tictactoe.game_utils.TicTacToe
import com.example.tictactoe.game_utils.TicTacToeUtils
import com.example.tictactoe.model_class.TicBox
import com.example.tictactoe.model_class.TicTacToeGameState
import dagger.hilt.android.lifecycle.HiltViewModel
import mapToSnapshotList
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {
    var ticTacToeGameState: MutableState<TicTacToeGameState> = mutableStateOf(TicTacToeGameState.ContinuePlaying)
    val ticBoxList = mutableStateListOf<SnapshotStateList<TicBox>>()
    val currentAlphaTurnState = mutableStateOf(TicTacToeUtils.X_MARK)
    var previousMark = ""
    var isClicked = false

    fun updateListBox(size: Int) {
        ticBoxList.addAll(TicTacToeUtils.fillInitialTicBoxData(size).mapToSnapshotList())
    }

    fun updateIsWinState(ticBox: List<TicBox>) {
        ticBox.forEach {
            ticBoxList[it.col][it.row].isWin = true
        }
    }

    fun getCrossMarkAlphaState(): Float {
        return when {
            currentAlphaTurnState.value == TicTacToeUtils.X_MARK -> {
                if (ticTacToeGameState.value is TicTacToeGameState.OWin) 0.3f
                else 1f
            }
            ticTacToeGameState.value is TicTacToeGameState.XWin -> 1f
            ticTacToeGameState.value is TicTacToeGameState.GameOverWithoutResult -> 1f
            else -> 0.3f
        }
    }

    fun getCircleMarkAlphaState(): Float {
        return when {
            currentAlphaTurnState.value == TicTacToeUtils.O_MARK -> {
                if (ticTacToeGameState.value is TicTacToeGameState.XWin) 0.3f
                else 1f
            }
            ticTacToeGameState.value is TicTacToeGameState.OWin -> 1f
            ticTacToeGameState.value is TicTacToeGameState.GameOverWithoutResult -> 1f
            else -> 0.3f
        }
    }
}
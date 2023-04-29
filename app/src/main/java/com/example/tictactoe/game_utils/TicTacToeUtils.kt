package com.example.tictactoe.game_utils

import com.example.tictactoe.model_class.GameWinner
import com.example.tictactoe.model_class.TicBox
import com.example.tictactoe.model_class.TicTacToeGameState

object TicTacToeUtils {
    const val X_MARK = "X"
    const val O_MARK = "O"

    fun isAnyPlayerWin(list: MutableList<MutableList<TicBox>>): TicTacToeGameState {
        return list.findWinnerAndReturnTicTacToeGameState(isWin(list))
    }

    fun fillInitialTicBoxData(gridSize: Int = 3): MutableList<MutableList<TicBox>> {
        val list = mutableListOf<MutableList<TicBox>>()
        for (i in 0 until gridSize) {
            val insList = mutableListOf<TicBox>()
            for (j in 0 until gridSize) {
                insList.add(TicBox(i, j, ""))
            }
            list.add(insList)
        }
        return list
    }

    private fun MutableList<MutableList<TicBox>>.findWinnerAndReturnTicTacToeGameState(gamePair: Pair<GameWinner, List<TicBox>>): TicTacToeGameState {
        val (winnerState, ticBoxList) = gamePair
        return when (winnerState) {
            GameWinner.X_WIN -> TicTacToeGameState.XWin(ticBoxList)
            GameWinner.O_WIN -> TicTacToeGameState.OWin(ticBoxList)
            GameWinner.NO_WINNER -> {
                val (crossWinnerState, crossTicBoxList) = isCrossWin(this)
                when (crossWinnerState) {
                    GameWinner.X_WIN -> TicTacToeGameState.XWin(crossTicBoxList)
                    GameWinner.O_WIN -> TicTacToeGameState.OWin(crossTicBoxList)
                    GameWinner.NO_WINNER -> {
                        if (this.flatten().any { it.id.isEmpty() }) {
                            TicTacToeGameState.ContinuePlaying
                        } else {
                            TicTacToeGameState.GameOverWithoutResult()
                        }
                    }
                }
            }
        }
    }

    private fun isCrossWin(list: MutableList<MutableList<TicBox>>): Pair<GameWinner, List<TicBox>> {
        val lastIndex = list.lastIndex
        var ticBox: Pair<GameWinner, List<TicBox>> = Pair(GameWinner.NO_WINNER, emptyList())
        for (i in 0..lastIndex) {
            var lastPt = list.lastIndex
            val crossMidLeft = mutableListOf<TicBox>()
            val crossMidRight = mutableListOf<TicBox>()
            val crossLeft = mutableListOf<TicBox>()
            val crossRight = mutableListOf<TicBox>()

            for ((start, j) in (i..lastIndex).withIndex()) {
                crossLeft.add(list[j][start])
                crossRight.add(list[j][lastPt])

                crossMidLeft.add(list[start][j])
                crossMidRight.add(list[start][lastIndex - j])
                --lastPt
            }

            for (indexCo in 0..crossMidLeft.lastIndex) {
                ticBox = crossLeft.compareRowOrColAndReturnIsWin(indexCo, list.isFrom3X3grid())
                if (ticBox.second.isNotEmpty()) {
                    return ticBox
                }

                ticBox = crossRight.compareRowOrColAndReturnIsWin(indexCo, list.isFrom3X3grid())
                if (ticBox.second.isNotEmpty()) {
                    return ticBox
                }

                ticBox = crossMidLeft.compareRowOrColAndReturnIsWin(indexCo, list.isFrom3X3grid())
                if (ticBox.second.isNotEmpty()) {
                    return ticBox
                }

                ticBox = crossMidRight.compareRowOrColAndReturnIsWin(indexCo, list.isFrom3X3grid())
                if (ticBox.second.isNotEmpty()) {
                    return ticBox
                }
            }
        }
        return ticBox
    }

    private fun isWin(list: MutableList<MutableList<TicBox>>): Pair<GameWinner, List<TicBox>> {
        var ticBox: Pair<GameWinner, List<TicBox>> = Pair(GameWinner.NO_WINNER, emptyList())
        for (col in list.indices) {
            val colList = mutableListOf<TicBox>()
            for (index in list.indices) {
                colList.add(list[index][col])
            }
            for (indexCo in 0..colList.lastIndex) {
                ticBox = colList.compareRowOrColAndReturnIsWin(indexCo, list.isFrom3X3grid())
                if (ticBox.second.isNotEmpty()) {
                    return ticBox
                }
                ticBox = list[col].compareRowOrColAndReturnIsWin(indexCo, list.isFrom3X3grid())
                if (ticBox.second.isNotEmpty()) {
                    return ticBox
                }
            }
        }
        return ticBox
    }

    private fun MutableList<TicBox>.compareRowOrColAndReturnIsWin(
        from: Int = 0,
        isFrom3By3Grid: Boolean = true
    ): Pair<GameWinner, List<TicBox>> {
        when {
            this.size == 3 && isFrom3By3Grid -> {
                val isXWin = this.all { it.id == X_MARK }
                if (isXWin) {
                    return Pair(GameWinner.X_WIN, this)
                }

                val isOWin = this.all { it.id == O_MARK }
                if (isOWin) {
                    return Pair(GameWinner.O_WIN, this)
                }

                return Pair(GameWinner.NO_WINNER, emptyList())
            }

            this.size in 4..7 -> {
                val till = from + 3
                if (till <= this.lastIndex) {
                    val sliceRange = this.slice(from..till)
                    val isXWin = sliceRange.all { it.id == X_MARK }
                    if (isXWin) {
                        return Pair(GameWinner.X_WIN, sliceRange)
                    }

                    val isOWin = sliceRange.all { it.id == O_MARK }
                    if (isOWin) {
                        return Pair(GameWinner.O_WIN, sliceRange)
                    }

                    return Pair(GameWinner.NO_WINNER, emptyList())
                } else {
                    return Pair(GameWinner.NO_WINNER, emptyList())
                }
            }

            else -> {
                return Pair(GameWinner.NO_WINNER, emptyList())
            }
        }
    }

    private fun List<List<TicBox>>.isFrom3X3grid() = this.size == 3
}

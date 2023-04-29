package com.example.tictactoe.game_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import clickAndVibrate
import com.example.tictactoe.game_utils.TicTacToeUtils
import com.example.tictactoe.model_class.DataStoreManager
import com.example.tictactoe.model_class.TicBox
import com.example.tictactoe.model_class.TicTacToeGameState
import hexToColor
import mapToMutableList

@Composable
fun TicTacToeGameScreen(size: Int) {
    val viewModel = hiltViewModel<GameViewModel>()
    val boxColor = DataStoreManager.getDataFromStore<String>(LocalContext.current, DataStoreManager.BOX_COLOR_KEY)
        .collectAsStateWithLifecycle(initialValue = "")
    val circleMark = DataStoreManager.getDataFromStore<String>(LocalContext.current, DataStoreManager.O_MARK_KEY)
        .collectAsStateWithLifecycle(initialValue = "")
    val crossMark = DataStoreManager.getDataFromStore<String>(LocalContext.current, DataStoreManager.X_MARK_KEY)
        .collectAsStateWithLifecycle(initialValue = "")

    LaunchedEffect(key1 = viewModel) {
        viewModel.updateListBox(size)
    }

    when (viewModel.ticTacToeGameState.value) {
        is TicTacToeGameState.ContinuePlaying -> Log.d("Play", "Continue playing")
        is TicTacToeGameState.GameOverWithoutResult -> Toast.makeText(LocalContext.current, "Game over", Toast.LENGTH_SHORT).show()
        is TicTacToeGameState.OWin -> {
            viewModel.updateIsWinState((viewModel.ticTacToeGameState.value as TicTacToeGameState.OWin).ticBox)
            Toast.makeText(LocalContext.current, "Player O win", Toast.LENGTH_SHORT).show()
        }
        is TicTacToeGameState.XWin -> {
            viewModel.updateIsWinState((viewModel.ticTacToeGameState.value as TicTacToeGameState.XWin).ticBox)
            Toast.makeText(LocalContext.current, "Player X win", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center) {
        item {
            Text(
                text = "Player X",
                color = crossMark.value.hexToColor() ?: MaterialTheme.colors.onBackground,
                fontSize = 35.sp,
                textAlign = TextAlign.Left,
                maxLines = 1,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 10.dp)
                    .alpha(viewModel.getCrossMarkAlphaState())
            )
        }

        itemsIndexed(viewModel.ticBoxList) { colIndex, colItem ->
            Row {
                colItem.forEachIndexed { rowIndex, rowItem ->
                    Box(modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .border(2.0.dp, MaterialTheme.colors.onBackground)
                        .aspectRatio(1f)
                        .background(boxColor.value.hexToColor() ?: Color.Transparent)
                        .clickable {
                            if (viewModel.ticTacToeGameState.value == TicTacToeGameState.ContinuePlaying && viewModel.ticBoxList[colIndex][rowIndex].id.isEmpty()) {
                                viewModel.previousMark = when (viewModel.previousMark) {
                                    TicTacToeUtils.X_MARK -> {
                                        viewModel.currentAlphaTurnState.value = TicTacToeUtils.X_MARK
                                        TicTacToeUtils.O_MARK
                                    }

                                    TicTacToeUtils.O_MARK -> {
                                        viewModel.currentAlphaTurnState.value = TicTacToeUtils.O_MARK
                                        TicTacToeUtils.X_MARK
                                    }

                                    else -> {
                                        viewModel.currentAlphaTurnState.value = TicTacToeUtils.O_MARK
                                        TicTacToeUtils.X_MARK
                                    }
                                }
                                viewModel.isClicked = true
                                viewModel.ticBoxList[colIndex][rowIndex] = rowItem.copy(id = viewModel.previousMark)
                                viewModel.ticTacToeGameState.value = TicTacToeUtils.isAnyPlayerWin(viewModel.ticBoxList.mapToMutableList())
                            }
                        }) {
                        if (viewModel.isClicked) {
                            LocalContext.current.clickAndVibrate()
                            viewModel.isClicked = false
                        }
                        DrawMarkBasedOnTicSign(item = viewModel.ticBoxList[colIndex][rowIndex])
                    }
                }
            }
        }

        item {
            Text(
                text = "Player O",
                color = circleMark.value.hexToColor() ?: MaterialTheme.colors.onBackground,
                fontSize = 35.sp,
                textAlign = TextAlign.End,
                maxLines = 1,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(end = 10.dp, top = 10.dp)
                    .alpha(viewModel.getCircleMarkAlphaState())
            )
        }
    }
}

@Composable
fun DrawMarkBasedOnTicSign(item: TicBox) {
    val infiniteTransition = rememberInfiniteTransition()
    val alphaChange by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(300), repeatMode = RepeatMode.Reverse
        )
    )
    Log.d("itemClick", "click item")
    val alpha = if (item.isWin) alphaChange else 1f
    when (item.id) {
        TicTacToeUtils.X_MARK -> DrawCross(modifier = Modifier.aspectRatio(1f), alpha)
        TicTacToeUtils.O_MARK -> DrawCircle(modifier = Modifier.aspectRatio(1f), alpha)
    }
}

@Composable
fun DrawCross(modifier: Modifier, alphaChange: Float = 1f) {
    val crossMark = DataStoreManager.getDataFromStore<String>(LocalContext.current, DataStoreManager.X_MARK_KEY)
        .collectAsStateWithLifecycle(initialValue = "")

    val animVal = remember { Animatable(0f) }

    LaunchedEffect(animVal) {
        animVal.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 300, easing = LinearEasing))
    }
    val crossColor = crossMark.value.hexToColor() ?: MaterialTheme.colors.onBackground


    Canvas(modifier = modifier.padding(6.dp)) {
        val width = animVal.value * size.width
        val height = animVal.value * size.height

        drawLine(
            color = crossColor,
            start = Offset(0f, 0f),
            end = Offset(width, height),
            strokeWidth = 15f,
            alpha = alphaChange
        )
    }

    Canvas(modifier = modifier.padding(6.dp)) {
        drawLine(
            color = crossColor,
            start = Offset(0f, size.height),
            end = Offset(size.width, 0f),
            strokeWidth = 15f,
            alpha = alphaChange
        )
    }
}

@Composable
fun DrawCircle(modifier: Modifier, alphaChange: Float = 1f) {
    val circleMark = DataStoreManager.getDataFromStore<String>(LocalContext.current, DataStoreManager.O_MARK_KEY)
        .collectAsStateWithLifecycle(initialValue = "")

    val animateFloat = remember { Animatable(0f) }

    LaunchedEffect(animateFloat) {
        animateFloat.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 300, easing = LinearEasing))
    }
    val circleArcColor = circleMark.value.hexToColor() ?: MaterialTheme.colors.onBackground
    Canvas(modifier = modifier) {
        drawArc(
            color = circleArcColor,
            startAngle = 0f,
            alpha = alphaChange,
            sweepAngle = 360f * animateFloat.value,
            topLeft = Offset(size.width / 9, size.height / 9),
            useCenter = false,
            size = Size(size.width / 1.3f, size.height / 1.3f),
            style = Stroke(15f)
        )
    }
}
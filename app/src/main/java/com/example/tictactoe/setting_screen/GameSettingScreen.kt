package com.example.tictactoe.setting_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.model_class.DataStoreManager
import com.example.tictactoe.model_class.DataStoreManager.saveDataToStore
import com.example.tictactoe.model_class.GameSetting
import com.raedapps.alwan.AlwanState
import com.raedapps.alwan.ui.AlwanDialog
import hexToColor
import toHexCode


@Composable
fun GameSettingScreen() {
    val viewModel = hiltViewModel<GameSettingViewModel>()
    val (gameSetting, shouldDisplayDialog) = viewModel.displayState.value

    if (shouldDisplayDialog && gameSetting != null) {
        Log.d("state chane", "state change for ${gameSetting.settingName}")
        OpenColorPicker(gameSetting = gameSetting, viewModel = viewModel)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
            itemsIndexed(viewModel.list) { index, gameSetting ->
                gameSetting.index = index
                GameSettingItem(
                    gameSetting = gameSetting,
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    viewModel.displayState.value = Pair(gameSetting, true)
                }
            }
        }
    }
}

@Composable
fun GameSettingItem(gameSetting: GameSetting, modifier: Modifier, onClick: (GameSetting) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
    ) {
        Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
            Text(
                text = gameSetting.settingName,
                fontSize = 18.sp,
                textAlign = TextAlign.Left,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(20.dp),
                color = MaterialTheme.colors.onBackground
            )

            Box(modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    color = DataStoreManager
                        .getDataFromStore<String>(LocalContext.current, gameSetting.settingName)
                        .collectAsStateWithLifecycle(initialValue = "").value.hexToColor() ?: Color.Transparent
                )
                .border(0.5.dp, MaterialTheme.colors.onBackground, shape = RoundedCornerShape(6.dp))
                .clickable {
                    onClick(gameSetting)
                })
        }
        Divider(color = MaterialTheme.colors.onBackground, thickness = 0.5.dp)
    }
}


@Composable
fun OpenColorPicker(gameSetting: GameSetting, viewModel: GameSettingViewModel) {
    val color = DataStoreManager.getDataFromStore<String>(LocalContext.current, gameSetting.settingName)
        .collectAsStateWithLifecycle(initialValue = "")

    val isPositiveBtnClick = remember { mutableStateOf(false) }
    val selectedColor = remember { mutableStateOf(gameSetting.colorCode) }

    AlwanDialog(modifier = Modifier
        .clip(RoundedCornerShape(15.dp))
        .border(0.dp, Color.Transparent, RoundedCornerShape(15.dp)),
        onDismissRequest = { },
        onColorChanged = {
            isPositiveBtnClick.value = false
            selectedColor.value = it
        },
        positiveButtonText = "OK",
        onPositiveButtonClick = {
            viewModel.list[gameSetting.index] = GameSetting(gameSetting.settingName, selectedColor.value, gameSetting.index)
            isPositiveBtnClick.value = true
        },
        state = AlwanState(color.value.hexToColor() ?: Color.Red),
        negativeButtonText = "CANCEL",
        onNegativeButtonClick = {
            viewModel.displayState.value = Pair(null, false)
            isPositiveBtnClick.value = false
        })

    if (isPositiveBtnClick.value) {
        viewModel.viewModelScope.saveDataToStore(
            LocalContext.current, gameSetting.settingName, selectedColor.value.toHexCode()
        )
        viewModel.displayState.value = Pair(null, false)
    }
}


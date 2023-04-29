package com.example.tictactoe.setting_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.tictactoe.model_class.DataStoreManager
import com.example.tictactoe.model_class.GameSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameSettingViewModel @Inject constructor() : ViewModel() {
    val list = mutableListOf(
        GameSetting(DataStoreManager.BOX_COLOR_KEY, Color.Black),
        GameSetting(DataStoreManager.X_MARK_KEY, Color.Black),
        GameSetting(DataStoreManager.O_MARK_KEY, Color.Black)
    )

    val displayState = mutableStateOf(Pair<GameSetting?, Boolean>(null, false))
}


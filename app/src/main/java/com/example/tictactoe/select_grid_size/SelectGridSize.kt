package com.example.tictactoe.select_grid_size

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.model_class.DataStoreManager
import hexToColor


@Composable
fun SelectGridScreenSize(onGridSelection: (gridSize: Int) -> Unit) {
    val center = Arrangement.Center
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Choose Grid Size",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.size(30.dp))
        CreateBoxBySize(size = 3, textTitle = "3 X 3", onGridSelection)
        Spacer(modifier = Modifier.size(30.dp))
        CreateBoxBySize(size = 5, textTitle = "5 X 5", onGridSelection)
        Spacer(modifier = Modifier.size(30.dp))
        CreateBoxBySize(size = 7, textTitle = "7 X 7", onGridSelection)
    }
}

@Composable
fun CreateBoxBySize(size: Int, textTitle: String, onGridSelection: (gridSize: Int) -> Unit) {
    val color = DataStoreManager.getDataFromStore<String>(LocalContext.current, DataStoreManager.BOX_COLOR_KEY).collectAsState(initial = "")
    val paddingSize = when (size) {
        3 -> 11.dp
        5 -> 6.dp
        else -> 4.dp
    }

    val boxSize = when (size) {
        3 -> 24.dp
        5 -> 15.dp
        else -> 10.dp
    }


    Column(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(10.dp))
            .background(color = color.value.hexToColor() ?: Color.Transparent)
            .border(1.4.dp, MaterialTheme.colors.onBackground, shape = RoundedCornerShape(10.dp))
            .clickable { onGridSelection(size) }
            .padding(start = paddingSize, top = paddingSize, end = paddingSize, bottom = 10.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = textTitle,
            color = MaterialTheme.colors.onBackground,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.wrapContentSize()
        )

        LazyColumn(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center) {
            items(size) {
                Row {
                    for (i in 0 until size) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 2.dp, vertical = 1.dp)
                                .size(boxSize)
                                .border(1.4.dp, MaterialTheme.colors.onBackground)
                        )
                    }
                }
            }
        }
    }
}
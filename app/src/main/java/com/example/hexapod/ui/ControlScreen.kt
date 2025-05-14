package com.example.hexapod.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hexapod.WebSocketHandler
import com.example.hexapod.data.GlobalData
import com.example.hexapod.ui.theme.FirstTheme


@Composable fun ControlScreen(onReturnClicked: () -> Unit){
    var sliderPosition = remember { mutableStateListOf<Float>().apply { repeat(18) { add(GlobalData.sliderPosition[it])} } }

   LazyColumn(Modifier.padding(start = 30.dp, end = 30.dp)) {

       for (i in 0 until sliderPosition.size){
           item {

               if (i % 3 == 0){
                   Text(text = "Leg " + (i/3+1).toInt().toString(), Modifier.padding(top = 10.dp, bottom = 10.dp))
               }

               Slider(
                   value = sliderPosition[i],
                   onValueChange = { 
                        sliderPosition[i] = it
                        GlobalData.sliderPosition[i] = it
                        WebSocketHandler.sendServo(i,sliderPosition[i].toInt())
                        },
                   valueRange = 0f..180f
               )

               Text(text = sliderPosition[i].toInt().toString())
           }
       }
   }

}

@Preview
@Composable
fun controlScreenPreview() {
    FirstTheme { ControlScreen({}) }
}
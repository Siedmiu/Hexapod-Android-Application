package com.example.hexapod.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hexapod.data.GlobalData
import com.example.hexapod.ui.theme.FirstTheme


@Composable fun ControlScreen(onReturnClicked: () -> Unit){

    var sliderPosition by remember { mutableFloatStateOf(GlobalData.sliderPosition) }
    var sliderPosition2 by rememberSaveable { mutableFloatStateOf(GlobalData.sliderPosition2) }
    var sliderPosition3 by remember { mutableFloatStateOf(GlobalData.sliderPosition3) }
    var sliderPosition4 by remember { mutableFloatStateOf(GlobalData.sliderPosition4) }
    var sliderPosition5 by remember { mutableFloatStateOf(GlobalData.sliderPosition5) }
    var sliderPosition6 by remember { mutableFloatStateOf(GlobalData.sliderPosition6) }
    var sliderPosition7 by remember { mutableFloatStateOf(GlobalData.sliderPosition7) }
    var sliderPosition8 by remember { mutableFloatStateOf(GlobalData.sliderPosition8) }
    var sliderPosition9 by remember { mutableFloatStateOf(GlobalData.sliderPosition9) }
    var sliderPosition10 by remember { mutableFloatStateOf(GlobalData.sliderPosition10) }
    var sliderPosition11 by remember { mutableFloatStateOf(GlobalData.sliderPosition11) }
    var sliderPosition12 by remember { mutableFloatStateOf(GlobalData.sliderPosition12) }
    var sliderPosition13 by remember { mutableFloatStateOf(GlobalData.sliderPosition13) }
    var sliderPosition14 by remember { mutableFloatStateOf(GlobalData.sliderPosition14) }
    var sliderPosition15 by remember { mutableFloatStateOf(GlobalData.sliderPosition15) }
    var sliderPosition16 by remember { mutableFloatStateOf(GlobalData.sliderPosition16) }
    var sliderPosition17 by remember { mutableFloatStateOf(GlobalData.sliderPosition17) }
    var sliderPosition18 by remember { mutableFloatStateOf(GlobalData.sliderPosition18) }

   LazyColumn(Modifier.padding(10.dp)) {
       item {
           Text(text = "Leg 1", Modifier.padding(top = 10.dp, bottom = 10.dp))
           Slider(
               value = sliderPosition,
               onValueChange = { sliderPosition = it
                   GlobalData.sliderPosition = sliderPosition},
               valueRange = 0f..180f,
           )
           Text(text = sliderPosition.toInt().toString())
       }

       item {
           Slider(
               value = sliderPosition2,
               onValueChange = { sliderPosition2 = it
                   GlobalData.sliderPosition2 = sliderPosition2},
               valueRange = 0f..180f
           )

           Text(text = sliderPosition2.toInt().toString())
       }

       item {
           Slider(
               value = sliderPosition3,
               onValueChange = { sliderPosition3 = it
                   GlobalData.sliderPosition3 = sliderPosition3},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition3.toInt().toString())
       }
       item {
           Text(text = "Leg 2", Modifier.padding(top = 10.dp, bottom = 10.dp))
           Slider(
               value = sliderPosition4,
               onValueChange = { sliderPosition4 = it
                   GlobalData.sliderPosition4 = sliderPosition4},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition4.toInt().toString())
       }
       item {
           Slider(
               value = sliderPosition5,
               onValueChange = { sliderPosition5 = it
                   GlobalData.sliderPosition5 = sliderPosition5},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition5.toInt().toString())
       }
       item {
           Slider(
               value = sliderPosition6,
               onValueChange = { sliderPosition6 = it
                   GlobalData.sliderPosition6 = sliderPosition6},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition6.toInt().toString())
       }
       item {
           Text(text = "Leg 3", Modifier.padding(top = 10.dp, bottom = 10.dp))
           Slider(
               value = sliderPosition7,
               onValueChange = { sliderPosition7 = it
                   GlobalData.sliderPosition7 = sliderPosition7},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition7.toInt().toString())
       }
       item {
           Slider(
               value = sliderPosition8,
               onValueChange = { sliderPosition8 = it
                   GlobalData.sliderPosition8 = sliderPosition8},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition8.toInt().toString())
       }
       item {
           Slider(
               value = sliderPosition9,
               onValueChange = { sliderPosition9 = it
                   GlobalData.sliderPosition9 = sliderPosition9},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition9.toInt().toString())
       }
       item {
           Text(text = "Leg 4", Modifier.padding(top = 10.dp, bottom = 10.dp))
           Slider(
               value = sliderPosition10,
               onValueChange = { sliderPosition10 = it
                   GlobalData.sliderPosition10 = sliderPosition10},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition10.toInt().toString())
       }

       item {
           Slider(
               value = sliderPosition11,
               onValueChange = { sliderPosition11 = it
                   GlobalData.sliderPosition11 = sliderPosition11
                               },
               valueRange = 0f..180f
           )
           Text(text = sliderPosition11.toInt().toString())
       }
       item {
           Slider(
               value = sliderPosition12,
               onValueChange = { sliderPosition12 = it
                   GlobalData.sliderPosition12 = sliderPosition12},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition12.toInt().toString())
       }
       item {
           Text(text = "Leg 5", Modifier.padding(top = 10.dp, bottom = 10.dp))
           Slider(
               value = sliderPosition13,
               onValueChange = { sliderPosition13 = it
                   GlobalData.sliderPosition13 = sliderPosition13},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition13.toInt().toString())
       }
       item {
           Slider(
               value = sliderPosition14,
               onValueChange = { sliderPosition14 = it
                   GlobalData.sliderPosition14 = sliderPosition14},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition14.toInt().toString())
       }
       item {
           Slider(
               value = sliderPosition15,
               onValueChange = { sliderPosition15 = it
                   GlobalData.sliderPosition15 = sliderPosition15},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition15.toInt().toString())
       }
       item {
           Text(text = "Leg 6", Modifier.padding(top = 10.dp, bottom = 10.dp))
           Slider(
               value = sliderPosition16,
               onValueChange = { sliderPosition16 = it
                   GlobalData.sliderPosition16 = sliderPosition16},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition16.toInt().toString())
       }

       item {
           Slider(
               value = sliderPosition17,
               onValueChange = { sliderPosition17 = it
                   GlobalData.sliderPosition17 = sliderPosition17},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition17.toInt().toString())
       }

       item {
           Slider(
               value = sliderPosition18,
               onValueChange = { sliderPosition18 = it
                   GlobalData.sliderPosition18 = sliderPosition18},
               valueRange = 0f..180f
           )
           Text(text = sliderPosition18.toInt().toString())
       }
   }

}

@Preview
@Composable
fun controlScreenPreview() {
    FirstTheme { ControlScreen({}) }
}
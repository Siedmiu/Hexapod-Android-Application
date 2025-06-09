package com.example.hexapod.ui

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hexapod.R
import com.example.hexapod.WebSocketHandler
import com.example.hexapod.data.GlobalData
import com.example.hexapod.ui.theme.FirstTheme
import com.github.Mindinventory.circularslider.CircularProgressBar
import java.util.Timer
import java.util.TimerTask

@Composable
fun MainScreen() {
    var isConnected by remember { mutableStateOf(WebSocketHandler.isConnected()) }

    val timer = remember { Timer() }

    val radioOptions = listOf("Bigate", "Ripplegate", "Trigate", "Wavegate")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    var posX1 by remember { mutableStateOf(GlobalData.positionX.toString()) }
    var posY1 by remember { mutableStateOf(GlobalData.positionY.toString()) }

    var angle by rememberSaveable { mutableStateOf(GlobalData.angle1) }

    val maxAngle = 180

    DisposableEffect(Unit) {
        timer.schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post {
                    isConnected = WebSocketHandler.isConnected()
                }
            }
        }, 0, 1000)

        onDispose {
            timer.cancel()
            timer.purge()
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Status: \n",
                fontSize = 40.sp,
                lineHeight = 60.sp,
                textAlign = TextAlign.Center,
            )
        }

        val textCon = if (isConnected) "Connected" else "Disconnected"
        item {
            Text(
                text = textCon,
                fontSize = 60.sp,
                lineHeight = 60.sp,
                textAlign = TextAlign.Center,
                color = if (isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )

            Image(
                painter = painterResource(R.drawable.hexapod),
                contentDescription = "Icon"
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Row {
                Button(
                    onClick = { WebSocketHandler.sendMessage(" ")},
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                ) {
                    Text(text = stringResource(R.string.button11), fontSize = 12.sp)
                }
                Button(
                    onClick = {
                        if (selectedOption == "Bigate") {
                            WebSocketHandler.sendMessage("bigateForw")
                        } else if (selectedOption == "Ripplegate") {
                            WebSocketHandler.sendMessage("ripplegateForw")
                        } else if (selectedOption == "Trigate") {
                            WebSocketHandler.sendMessage("trigateForw")
                        } else if (selectedOption == "Wavegate") {
                            WebSocketHandler.sendMessage("wavegateForw")
                        } },
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                ) {
                    Text(text = stringResource(R.string.button12), fontSize = 12.sp)
                }
                Button(
                    onClick = { WebSocketHandler.sendMessage(" ") },
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                ) {
                    Text(text = stringResource(R.string.button13), fontSize = 12.sp)
                }
            }
        }
        item {
            Row {
                Button(
                    onClick = { WebSocketHandler.sendMessage("left",angle.toString())},
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                ) {
                    Text(text = stringResource(R.string.button21), fontSize = 12.sp)
                }
                Button(
                    onClick = { WebSocketHandler.sendMessage("stop") },
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                ) {
                    Text(text = stringResource(R.string.button22), fontSize = 12.sp)
                }
                Button(
                    onClick = { WebSocketHandler.sendMessage("right",angle.toString()) },
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                ) {
                    Text(text = stringResource(R.string.button23), fontSize = 12.sp)
                }
            }
        }
        item {
            Row {
                Button(
                    onClick = { WebSocketHandler.sendMessage("point add",posX1,posY1) },
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                ) {
                    Text(text = stringResource(R.string.button31), fontSize = 12.sp)
                }
                Button(
                    onClick = {
                        if (selectedOption == "Bigate") {
                            WebSocketHandler.sendMessage("bigateBack")
                        } else if (selectedOption == "Ripplegate") {
                            WebSocketHandler.sendMessage("ripplegateBack")
                        } else if (selectedOption == "Trigate") {
                            WebSocketHandler.sendMessage("trigateBack")
                        } else if (selectedOption == "Wavegate") {
                            WebSocketHandler.sendMessage("wavegateBack")
                        } },
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                ) {
                    Text(text = stringResource(R.string.button32), fontSize = 12.sp)
                }
                Button(
                    onClick = { WebSocketHandler.sendMessage("point execute") },
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                ) {
                    Text(text = stringResource(R.string.button33), fontSize = 12.sp)
                }
            }
        }
        item {
            Row(Modifier.selectableGroup()) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RadioButton(
                        selected = ("Bigate" == selectedOption),
                        onClick = { onOptionSelected("Bigate") })
                    Text(text = "Bigate")
                }
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RadioButton(
                        selected = ("Ripplegate" == selectedOption),
                        onClick = { onOptionSelected("Ripplegate") })
                    Text(text = "Ripplegate")
                }
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RadioButton(
                        selected = ("Trigate" == selectedOption),
                        onClick = { onOptionSelected("Trigate") })
                    Text(text = "Trigate")
                }
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RadioButton(
                        selected = ("Wavegate" == selectedOption),
                        onClick = { onOptionSelected("Wavegate") })
                    Text(text = "Wavegate")
                }

            }
        }

        item {
            Row() {
                OutlinedTextField(
                    value = posX1,
                    onValueChange = {
                        posX1 = it
                        val check1 = it.toFloatOrNull()
                        if (check1 != null) {
                            GlobalData.positionX = it.toFloat()
                        }
                    },
                    label = { Text("Position X") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f),
                )
                OutlinedTextField(
                    value = posY1,
                    onValueChange = {
                        posY1 = it
                        val check2 = it.toFloatOrNull()
                        if (check2 != null) {
                            GlobalData.positionY = it.toFloat()
                        }
                    },
                    label = { Text("Position Y") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f),
                )
            }
        }

        item {
            CircularProgressBar(
                maxNum = maxAngle,
                currentProgressToBeReturned = {
                    angle = (it * maxAngle / 100).toInt()
                    GlobalData.angle1 = angle
                },
                currentUpdatedValue = angle.toString()
            )

            // Cicrular Progress Bar from https://github.com/Mindinventory/AndroidCircularSlider
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    FirstTheme { MainScreen() }
}
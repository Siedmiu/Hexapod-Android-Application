package com.example.hexapod

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hexapod.ui.theme.FirstTheme
import java.util.Timer
import java.util.TimerTask

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebSocketHandler.connect()

        setContent {
            FirstTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MMenu1()
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        WebSocketHandler.disconnect()
    }
}

@Composable
fun MMenu1() {
    var isConnected by remember { mutableStateOf(WebSocketHandler.isConnected()) }

    val timer = remember { Timer() }

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
    
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Status: \n",
            fontSize = 40.sp,
            lineHeight = 60.sp,
            textAlign = TextAlign.Center,
        )
        
        val textCon = if (isConnected) "Connected" else "Disconnected"
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

        Spacer(modifier = Modifier.height(64.dp))

        Row {
            Button(
                onClick = { WebSocketHandler.sendLedColor(0xFF0000) },
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = stringResource(R.string.button11), fontSize = 12.sp)
            }
            Button(
                onClick = { WebSocketHandler.sendLedColor(0x00FF00) },
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = stringResource(R.string.button12), fontSize = 12.sp)
            }
            Button(
                onClick = { WebSocketHandler.sendLedColor(0x0000FF) },
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = stringResource(R.string.button13), fontSize = 12.sp)
            }
        }

        Row {
            Button(
                onClick = { WebSocketHandler.sendLedColor(0xFFFF00) },
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = stringResource(R.string.button21), fontSize = 12.sp)
            }
            Button(
                onClick = { WebSocketHandler.sendLedColor(0xFF00FF) },
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = stringResource(R.string.button22), fontSize = 12.sp)
            }
            Button(
                onClick = { WebSocketHandler.sendLedColor(0x00FFFF) },
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = stringResource(R.string.button23), fontSize = 12.sp)
            }
        }

        Row {
            Button(
                onClick = { WebSocketHandler.sendLedColor(0xFFFFFF) },
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = stringResource(R.string.button31), fontSize = 12.sp)
            }
            Button(
                onClick = { WebSocketHandler.sendLedColor(0x000000) },
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = stringResource(R.string.button32), fontSize = 12.sp)
            }
            Button(
                onClick = { WebSocketHandler.sendLedColor(0x123456) },
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = stringResource(R.string.button33), fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MMenu1Preview() {
    FirstTheme {
        MMenu1()
    }
}


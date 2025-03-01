package com.example.first


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.first.ui.theme.FirstTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}

@Composable
fun isConnected():Boolean{
    // proper algorithm for connection checking to be added

    if (false) return true
    else return false
}

@Composable
fun MMenu1() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "Status: \n ",
                fontSize = 40.sp,
                lineHeight = 60.sp,
                textAlign = TextAlign.Center,
            )
            var textCon = "Unconnected"
            if (isConnected()) textCon = "Connected"
            Text(
                text = textCon,
                fontSize = 60.sp,
                lineHeight = 60.sp,
                textAlign = TextAlign.Center,
            )
            Image(
                // Some fancy icon of our robot will be here one day
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Icon"
            )

            Spacer(modifier = Modifier.height(64.dp))
            
            Row(){
                Button(onClick = {}, modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(R.string.button11), fontSize = 12.sp) }
                Button(onClick = {}, modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(R.string.button12), fontSize = 12.sp) }
                Button(onClick = {}, modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(R.string.button13), fontSize = 12.sp) }
            }

            Row(){
                Button(onClick = {}, modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(R.string.button21), fontSize = 12.sp) }
                Button(onClick = {}, modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(R.string.button22), fontSize = 12.sp) }
                Button(onClick = {}, modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(R.string.button23), fontSize = 12.sp) }
            }

            Row(){
                Button(onClick = {}, modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(R.string.button31), fontSize = 12.sp) }
                Button(onClick = {}, modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(R.string.button32), fontSize = 12.sp) }
                Button(onClick = {}, modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(R.string.button33), fontSize = 12.sp) }
            }

        }



}


@Preview(showBackground = true)
@Composable
fun MMenu1Preview(){
    FirstTheme {
        MMenu1()
    }
}


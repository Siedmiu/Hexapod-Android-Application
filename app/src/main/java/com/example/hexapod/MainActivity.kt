package com.example.hexapod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hexapod.ui.theme.FirstTheme
import com.example.hexapod.ui.theme.MyNavBar
import com.example.hexapod.ui.theme.NavHostContainer

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

enum class HexScreen(@StringRes val title: Int) {
    Main(title = R.string.main),
    Info(title = R.string.info),
    Control(title = R.string.control)
}

@Composable
fun MMenu1(navController: NavHostController = rememberNavController()) {

    Scaffold(
        bottomBar = { MyNavBar(navController) } // Navigation bar at the bottom
    ) {
        NavHostContainer(navController, Modifier.padding(it))
    }


}

@Preview(showBackground = true)
@Composable
fun MMenu1Preview() {
    FirstTheme {
        MMenu1()
    }
}

@Preview
@Composable
fun NavBarPreview() {
    FirstTheme { MyNavBar(navController = rememberNavController()) }
}


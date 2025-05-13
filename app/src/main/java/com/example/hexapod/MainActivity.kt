package com.example.hexapod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hexapod.ui.InfoScreen
import com.example.hexapod.ui.MainScreen
import com.example.hexapod.ui.theme.FirstTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hexapod.ui.theme.MyNavBar
import com.example.hexapod.ui.theme.NavHostContainer
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

enum class HexScreen(@StringRes val title: Int){
    Main(title = R.string.main),
    Info(title = R.string.info)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MMenu1(navController: NavHostController = rememberNavController()) {

    Scaffold(
        bottomBar = { MyNavBar(navController) } // Użycie NavBar jako dolnego paska
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
fun navBarPreview(){
    FirstTheme { MyNavBar(navController = rememberNavController()) }
}


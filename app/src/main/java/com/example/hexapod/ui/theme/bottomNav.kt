package com.example.hexapod.ui.theme

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hexapod.HexScreen
import com.example.hexapod.ui.MainScreen
import com.example.hexapod.ui.InfoScreen

@Composable
fun bottomNavigation(){

    BottomAppBar {
        Button(onClick = { },
            modifier = Modifier.padding(12.dp)) {}
        Button(onClick = { },
            modifier = Modifier.padding(12.dp)) {}
        Button(onClick = { },
            modifier = Modifier.padding(12.dp)) {}
        Button(onClick = { },
            modifier = Modifier.padding(12.dp)) {}
    }

}


@Composable
fun MyNavBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(HexScreen.Main.name) },
            label = { Text("Main") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Screen 1") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(HexScreen.Info.name) },
            label = { Text("Info") },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Screen 2") }
        )
    }
}

// Kontener dla ekranu nawigacyjnego
@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = HexScreen.Main.name,
        modifier = modifier
    ) {
        composable(HexScreen.Main.name) {
            MainScreen(onNavigateToSecondScreen = {
                   navController.navigate(HexScreen.Info.name)
               }) }
        composable(HexScreen.Info.name) {
            InfoScreen(onReturnClicked = {
                    navController.navigate(HexScreen.Main.name)
                })
                }
    }
}

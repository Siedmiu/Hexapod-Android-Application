package com.example.hexapod.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hexapod.WebSocketHandler
import com.example.hexapod.ui.theme.FirstTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(onNavigateBack: () -> Unit = {}, onReturnClicked: () -> Unit = onNavigateBack) {
    var ipAddress by remember { mutableStateOf(WebSocketHandler.getIP()) }
    var port by remember { mutableStateOf(WebSocketHandler.getPort().toString()) }
    var isIpValid by remember { mutableStateOf(true) }
    var isPortValid by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 64.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = ipAddress,
                onValueChange = { 
                    ipAddress = it
                    isIpValid = isValidIpAddress(it)
                },
                label = { Text("IP Address") },
                isError = !isIpValid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                supportingText = {
                    if (!isIpValid) {
                        Text("Enter a valid IP address (e.g., 192.168.1.1)")
                    }
                }
            )

            OutlinedTextField(
                value = port,
                onValueChange = { 
                    port = it
                    isPortValid = it.toIntOrNull() in 1..65535
                },
                label = { Text("Port") },
                isError = !isPortValid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                supportingText = {
                    if (!isPortValid) {
                        Text("Enter a valid port number (1-65535)")
                    }
                }
            )

            Button(
                onClick = {
                    if (isIpValid && isPortValid) {
                        WebSocketHandler.updateIP(ipAddress)
                        port.toIntOrNull()?.let { WebSocketHandler.updatePort(it) }
                        onNavigateBack()
                    }
                },
                enabled = isIpValid && isPortValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Connection Settings")
            }
        }
    
}

// Helper function to validate IP address format
private fun isValidIpAddress(ip: String): Boolean {
    val pattern = """^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$""".toRegex()
    return pattern.matches(ip)
}

@Preview
@Composable
fun InfoScreenPreview() {
    FirstTheme {
        InfoScreen(onNavigateBack = {})
    }
}
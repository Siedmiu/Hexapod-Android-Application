package com.example.hexapod.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hexapod.R
import com.example.hexapod.ui.theme.FirstTheme

@Composable
fun InfoScreen(onReturnClicked: () -> Unit,verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.SpaceBetween){
    Column() {
        Row { Text(
            text = stringResource(R.string.info),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 32.sp
        ) }
        Spacer(modifier = Modifier.height(16.dp))
        Row { Text(text = stringResource(R.string.position),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 32.sp
        )}
        Spacer(modifier = Modifier.height(16.dp))
        Row { Text(text = stringResource(R.string.state),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 32.sp
        )}
        Spacer(modifier = Modifier.height(32.dp))
        Row{
            Button(onClick = onReturnClicked) {
                Text(text = stringResource(R.string.returnButton), fontSize = 12.sp)}
        }
    }
}

@Preview
@Composable
fun InfoScreenPreview(){
    FirstTheme { InfoScreen({}) }
}
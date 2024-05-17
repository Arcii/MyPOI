package com.myapps.mypoi.screen.utility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.myapps.mypoi.R

@Composable
fun MyPoiTopBar(screen: String, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = screen,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun MyPoiBottomBar(modifier: Modifier = Modifier) {
    BottomAppBar (
        containerColor = MaterialTheme.colorScheme.primary
    ){
        //Empty BottomBar
    }
}

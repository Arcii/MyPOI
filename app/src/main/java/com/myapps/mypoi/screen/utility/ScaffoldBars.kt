package com.myapps.mypoi.screen.utility

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapps.mypoi.R

@Composable
fun MyPoiTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 30.sp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun MyPoiBottomBar(screen: String, backNavigation: () -> Unit = {}, modifier: Modifier = Modifier) {
    BottomAppBar (
        containerColor = MaterialTheme.colorScheme.primary
    ){
        Row {
            if (screen != stringResource(id = R.string.poi_categories_title_string)) {
                BackButton(
                    onClick = backNavigation,
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = screen,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 30.sp),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String
) {
    androidx.compose.material.IconButton(
        onClick = onClick,
        modifier = Modifier.padding(start = 10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

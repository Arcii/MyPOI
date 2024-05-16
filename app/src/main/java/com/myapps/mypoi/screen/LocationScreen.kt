package com.myapps.mypoi.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapps.mypoi.database.model.PoiLocation
import com.myapps.mypoi.viewmodel.MyPoiViewModel

@Composable
fun LocationScreen(categoryId: String, viewModel: MyPoiViewModel, contentPadding: PaddingValues) {

    val locations by viewModel.getLocationsByCategoryId(categoryId.toInt()).collectAsState(initial = emptyList())

    Surface (
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
    ){
        LazyColumn {
            items(locations){location ->
                LocationItem(location = location)
            }
        }
    }
}

@Composable
fun LocationItem(location: PoiLocation, modifier: Modifier = Modifier) {
    Text(text = location.name)
}

@Preview(showBackground = true)
@Composable
fun PreviewLocationScreen() {
    val locations = listOf(
        PoiLocation(1, "Location 1", 123.2, 123.3, 2),
        PoiLocation(2, "Location 2", 213.3, 65.3, 2),
        PoiLocation(3, "Location 3", 12.3, 67.4, 2)
    )
    LazyColumn {
        items(locations) { location ->
            LocationItem(location = location)
        }
    }
}
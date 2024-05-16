package com.myapps.mypoi.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.myapps.mypoi.database.model.PoiLocation
import com.myapps.mypoi.viewmodel.MyPoiViewModel

@Composable
fun AddLocationScreen(
    viewModel: MyPoiViewModel,
    onLocationSaved: () -> Unit,
    contentPadding: PaddingValues,
    onBack: () -> Unit,
    latitude: String,
    longitude: String,
    modifier: Modifier = Modifier
) {
    Surface (
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
    ) {
        Column {

            var locationName by remember { mutableStateOf(TextFieldValue()) }
            OutlinedTextField(
                value = locationName,
                onValueChange = { locationName = it },
                label = { Text("Location Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Row {
                Text(text = "Latitude : ")
                Text(text = latitude)
            }
            Row {
                Text(text = "Longitude : ")
                Text(text = longitude)
            }

            val categories by viewModel.allCategories.collectAsState(initial = emptyList())
            var selectedCategoryId by remember { mutableStateOf(-1) }
            LazyColumn(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                items(categories) { category ->
                    Row {
                        Text(
                            text = category.name,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = selectedCategoryId == category.id,
                            onClick = { selectedCategoryId = category.id }
                        ) 
                    }
                }
            }

            Button(
                onClick = {
                    val newLocation = PoiLocation(
                        name = locationName.text,
                        latitude = latitude.toDouble(),
                        longitude = longitude.toDouble(),
                        categoryId = selectedCategoryId
                    )
                    viewModel.insertLocation(newLocation)

                    onLocationSaved()
                }
            ) {
                Text(text = "Save Location")
            }
        }
    }

}
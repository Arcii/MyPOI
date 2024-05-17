package com.myapps.mypoi.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.myapps.mypoi.R
import com.myapps.mypoi.database.model.PoiLocation
import com.myapps.mypoi.screen.utility.MyPoiBottomBar
import com.myapps.mypoi.screen.utility.MyPoiTopBar
import com.myapps.mypoi.ui.theme.MyPOITheme
import com.myapps.mypoi.viewmodel.MyPoiViewModel

@Composable
fun LocationScreen(
    categoryId: String,
    viewModel: MyPoiViewModel,
    locationPermissionLauncher: ActivityResultLauncher<String>,
    context: Context,
    modifier: Modifier = Modifier
) {
    val locations by viewModel.getLocationsByCategoryId(categoryId.toInt()).collectAsState(initial = emptyList())
    val categoryName by viewModel.getCategoryNameById(categoryId.toInt()).collectAsState(initial = null)
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<PoiLocation?>(null) }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    val googleMapsPackageName = stringResource(id = R.string.google_maps_package)
    val defaultLatLon = stringResource(id = R.string.default_lat_lon).toDouble()

    MyPOITheme {
        Scaffold(
            topBar = { MyPoiTopBar(categoryName ?: "") },
            bottomBar = { MyPoiBottomBar() },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            getCurrentLocation(context, defaultLatLon) { receivedLatitude, receivedLongitude ->
                                latitude = receivedLatitude
                                longitude = receivedLongitude
                                showAddDialog = true
                            }
                        } else {
                            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_location_descriptor))
                }
            }
        ) { innerPadding ->
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyColumn {
                    items(locations) { location ->
                        LocationItem(
                            location = location,
                            onEditClick = {
                                selectedLocation = location
                                showEditDialog = true
                            },
                            onDeleteClick = { viewModel.deleteLocation(location) },
                            onLocationClick = {
                                val gmmIntentUri = Uri.parse("geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}(${Uri.encode(location.name)})")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                mapIntent.setPackage(googleMapsPackageName)
                                context.startActivity(mapIntent)
                            }
                        )
                    }
                }
            }
            if (showAddDialog) {
                AddLocationDialog(
                    latitude = latitude,
                    longitude = longitude,
                    onDismiss = { showAddDialog = false },
                    onConfirm = { name, latitude, longitude ->
                        viewModel.insertLocation(
                            PoiLocation(
                                id = 0,
                                name = name,
                                latitude = latitude,
                                longitude = longitude,
                                categoryId = categoryId.toInt()
                            )
                        )
                        showAddDialog = false
                    }
                )
            }
            if (showEditDialog) {
                selectedLocation?.let { location ->
                    EditLocationDialog(
                        location = location,
                        onDismiss = { showEditDialog = false },
                        onConfirm = { name, latitude, longitude ->
                            viewModel.updateLocation(
                                location.copy(
                                    name = name,
                                    latitude = latitude,
                                    longitude = longitude
                                )
                            )
                            showEditDialog = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LocationItem(
    location: PoiLocation,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onLocationClick: () -> Unit
) {
    Card(
        backgroundColor = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clickable(onClick = onLocationClick)
            ) {
                Text(
                    text = location.name,
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.edit_descriptor),
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_descriptor),
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }
}

@Composable
fun AddLocationDialog(
    latitude: Double,
    longitude: Double,
    onDismiss: () -> Unit,
    onConfirm: (String, Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(id = R.string.add_location_descriptor)) },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(id = R.string.name_string)) }
                )
                Row {
                    Text(text = "${stringResource(id = R.string.cap_latitude_string)} : ")
                    Text(text = latitude.toString())
                }
                Row {
                    Text(text = "${stringResource(id = R.string.cap_longitude_string)} : ")
                    Text(text = longitude.toString())
                }
            }
        },
        confirmButton = {
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onConfirm(name, latitude, longitude)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(id = R.string.add_string))
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(id = R.string.cancel_string))
            }
        }
    )
}

@Composable
fun EditLocationDialog(
    location: PoiLocation,
    onDismiss: () -> Unit,
    onConfirm: (String, Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(location.name) }
    var latitude by remember { mutableStateOf(location.latitude.toString()) }
    var longitude by remember { mutableStateOf(location.longitude.toString()) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(id = R.string.edit_location_string)) },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(id = R.string.name_string)) }
                )
                TextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text(stringResource(id = R.string.cap_latitude_string)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text(stringResource(id = R.string.cap_longitude_string)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onConfirm(name, latitude.toDouble(), longitude.toDouble())
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(id = R.string.update_string))
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(id = R.string.cancel_string))
            }
        }
    )
}

private fun getCurrentLocation(context: Context, defaultLatLon: Double, onLocationReceived: (Double, Double) -> Unit) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    onLocationReceived(location.latitude, location.longitude)
                } else {
                    onLocationReceived(defaultLatLon, defaultLatLon)
                }
            }
            .addOnFailureListener { exception ->
                onLocationReceived(defaultLatLon, defaultLatLon)
            }
    } else {
        onLocationReceived(defaultLatLon, defaultLatLon)
    }
}
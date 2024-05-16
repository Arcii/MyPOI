package com.myapps.mypoi.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.myapps.mypoi.R
import com.myapps.mypoi.database.model.PoiCategory
import com.myapps.mypoi.screen.utility.MyPoiBottomBar
import com.myapps.mypoi.screen.utility.MyPoiTopBar
import com.myapps.mypoi.ui.theme.MyPOITheme
import com.myapps.mypoi.viewmodel.MyPoiViewModel

@Composable
fun CategoryScreen(viewModel: MyPoiViewModel,modifier: Modifier = Modifier, navigateToLocationScreen: (String) -> Unit) {

    val categories by viewModel.allCategories.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var categoryName by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<PoiCategory?>(null) }

    MyPOITheme {
        Scaffold(
            topBar =  { MyPoiTopBar() } ,
            bottomBar = { MyPoiBottomBar() },
            floatingActionButton = {
                FloatingActionButton(onClick = {showDialog = true}) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ){ innerPadding ->
            Surface (
                modifier = Modifier
                    .padding(innerPadding)
            ){
                Column {
                    Text(
                        text = stringResource(id = R.string.poi_categories_title_string),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    LazyColumn {
                        items(categories) { category ->
                            CategoryItem(
                                category = category,
                                onItemClick = {
                                    navigateToLocationScreen(category.id.toString())
                                },
                                onDeleteClick = {
                                    viewModel.deleteCategory(category)
                                },
                                onEditClick = {
                                    categoryToEdit = category
                                    categoryName = category.name
                                    showEditDialog = true
                                }
                            )
                        }
                    }
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(stringResource(id = R.string.add_category_string)) },
                    text = {
                        Column {
                            Text(stringResource(id = R.string.enter_category_name_string))
                            TextField(
                                value = categoryName,
                                onValueChange = { categoryName = it },
                                singleLine = true
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            enabled = categoryName.isNotBlank(),
                            onClick = {
                                if (categoryName.isNotBlank()) {
                                    viewModel.insertCategory(PoiCategory(name = categoryName))
                                    categoryName = ""
                                    showDialog = false
                                }
                            }
                        ) {
                            Text(stringResource(id = R.string.add_string))
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text(stringResource(id = R.string.cancel_string))
                        }
                    }
                )
            }
            if (showEditDialog) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    title = { Text(stringResource(id = R.string.edit_category_string)) },
                    text = {
                        Column {
                            Text(stringResource(id = R.string.edit_category_name_string))
                            TextField(
                                value = categoryName,
                                onValueChange = { categoryName = it },
                                singleLine = true
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            enabled = categoryName.isNotBlank(),
                            onClick = {
                                categoryToEdit?.let {
                                    if (categoryName.isNotBlank()) {
                                        viewModel.updateCategory(it.copy(name = categoryName))
                                        categoryName = ""
                                        showEditDialog = false
                                    }
                                }
                            }
                        ) {
                            Text(stringResource(id = R.string.save_string))
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showEditDialog = false }) {
                            Text(stringResource(id = R.string.cancel_string))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: PoiCategory,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        backgroundColor = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clickable { onItemClick.invoke() }
            ) {
                Text(
                    text = category.name,
                    color = MaterialTheme.colorScheme.onTertiary,
                )
            }
            IconButton(onClick = { onEditClick.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.edit_descriptor),
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
            IconButton(onClick = { onDeleteClick.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_descriptor),
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }
}
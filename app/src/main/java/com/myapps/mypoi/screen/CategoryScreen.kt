package com.myapps.mypoi.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapps.mypoi.R
import com.myapps.mypoi.database.model.PoiCategory
import com.myapps.mypoi.screen.utility.MyPoiBottomBar
import com.myapps.mypoi.screen.utility.MyPoiTopBar
import com.myapps.mypoi.ui.theme.MyPOITheme
import com.myapps.mypoi.viewmodel.MyPoiViewModel

@Composable
fun CategoryScreen(
    viewModel: MyPoiViewModel,
    modifier: Modifier = Modifier,
    navigateToLocationScreen: (String) -> Unit
) {
    val categories by viewModel.allCategories.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var categoryName by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<PoiCategory?>(null) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    val successfulInsertion: String = stringResource(id = R.string.category_added_successfully)
    val successfulEdit:String = stringResource(id = R.string.category_edited_successfully)
    val successfulDelete:String = stringResource(id = R.string.category_removed_successfully)

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    MyPOITheme {
        Scaffold(
            topBar = { MyPoiTopBar() },
            bottomBar = { MyPoiBottomBar(stringResource(id = R.string.poi_categories_title_string)) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyColumn (
                    modifier = Modifier.padding(bottom = 80.dp)
                ){
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onItemClick = { navigateToLocationScreen(category.id.toString()) },
                            onDeleteClick = {
                                viewModel.deleteCategory(category)
                                snackbarMessage = successfulDelete
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

            if (showDialog) {
                CategoryDialog(
                    title = stringResource(id = R.string.add_category_string),
                    confirmButtonText = stringResource(id = R.string.add_string),
                    categoryName = categoryName,
                    onCategoryNameChange = { categoryName = it },
                    onConfirm = {
                        if (categoryName.isNotBlank()) {
                            viewModel.insertCategory(PoiCategory(name = categoryName))
                            categoryName = ""
                            showDialog = false
                            snackbarMessage = successfulInsertion
                        }
                    },
                    onDismiss = { showDialog = false }
                )
            }

            if (showEditDialog) {
                CategoryDialog(
                    title = stringResource(id = R.string.edit_category_string),
                    confirmButtonText = stringResource(id = R.string.save_string),
                    categoryName = categoryName,
                    onCategoryNameChange = { categoryName = it },
                    onConfirm = {
                        categoryToEdit?.let {
                            if (categoryName.isNotBlank()) {
                                viewModel.updateCategory(it.copy(name = categoryName))
                                categoryName = ""
                                showEditDialog = false
                                snackbarMessage = successfulEdit
                            }
                        }
                    },
                    onDismiss = { showEditDialog = false }
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
        backgroundColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onItemClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = category.name,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.edit_descriptor),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_descriptor),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
fun CategoryDialog(
    title: String,
    confirmButtonText: String,
    categoryName: String,
    onCategoryNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = {
            Column {
                Text(text = stringResource(id = R.string.enter_category_name_string))
                TextField(
                    value = categoryName,
                    onValueChange = onCategoryNameChange,
                    singleLine = true,
                    label = { Text(text = stringResource(id = R.string.category_string)) }
                )
            }
        },
        confirmButton = {
            Button(
                enabled = categoryName.isNotBlank(),
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = stringResource(id = R.string.cancel_string))
            }
        }
    )
}

@Preview
@Composable
fun CategoryScreenPreview(modifier: Modifier = Modifier){
    val categories = listOf(
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1"),
        PoiCategory(0,"Category 1")
    )
    MyPOITheme (darkTheme = false){
        Scaffold(
            topBar = { MyPoiTopBar() },
            bottomBar = { MyPoiBottomBar(stringResource(id = R.string.poi_categories_title_string)) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) { innerPadding ->
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.padding(bottom = 80.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onItemClick = {},
                            onDeleteClick = {},
                            onEditClick = {}
                        )
                    }
                }
            }
        }
    }
}
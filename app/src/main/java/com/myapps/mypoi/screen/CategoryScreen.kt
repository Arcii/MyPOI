package com.myapps.mypoi.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapps.mypoi.database.model.PoiCategory
import com.myapps.mypoi.viewmodel.MyPoiViewModel

@Composable
fun CategoryScreen(viewModel: MyPoiViewModel, navigateToLocationScreen: (String) -> Unit, contentPadding: PaddingValues) {

    val categories by viewModel.allCategories.collectAsState(initial = emptyList())

    Surface (
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
    ){
        LazyColumn {
            items(categories) { category ->
                CategoryItem(category = category) {
                    navigateToLocationScreen(category.id.toString())
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: PoiCategory, onItemClick: () -> Unit) {
    Text(
        text = category.name,
        modifier = Modifier.clickable { onItemClick.invoke() }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryScreen() {
    val categories = listOf(
        PoiCategory(1, "Category 1"),
        PoiCategory(2, "Category 2"),
        PoiCategory(3, "Category 3")
    )
    LazyColumn {
        items(categories) { category ->
            CategoryItem(category = category){}
        }
    }
}
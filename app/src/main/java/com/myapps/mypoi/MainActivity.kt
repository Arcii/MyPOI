package com.myapps.mypoi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.myapps.mypoi.database.MyPoiDatabase
import com.myapps.mypoi.database.repository.MyPoiRepository
import com.myapps.mypoi.screen.AddLocationScreen
import com.myapps.mypoi.screen.CategoryScreen
import com.myapps.mypoi.screen.LocationScreen
import com.myapps.mypoi.ui.theme.MyPOITheme
import com.myapps.mypoi.viewmodel.MyPoiViewModel
import com.myapps.mypoi.viewmodel.MyPoiViewModelFactory

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val myPoiDatabase = MyPoiDatabase.getDatabase(applicationContext)

        /*val myPoiDatabase = Room.databaseBuilder(
                applicationContext,
                MyPoiDatabase::class.java,
                "mypoidatabase.db"
            ).build()*/


        val myPoiDao = myPoiDatabase.myPoiDao()
        val myPoiRepository = MyPoiRepository(myPoiDao)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MyPOITheme{
                val myPoiViewModel = viewModel(factory = MyPoiViewModelFactory(myPoiRepository), modelClass = MyPoiViewModel::class.java)
                Scaffold (
                    topBar =  { MyPoiTopBar() } ,
                    bottomBar = {
                        SavePositionButton{ latitude, longitude ->
                            navController.navigate("addLocationScreen/$latitude/$longitude")
                        }
                    }
                ){ innerPadding ->
                    NavHost(navController = navController, startDestination = "categoryScreen") {
                        composable("categoryScreen") {
                            CategoryScreen(
                                viewModel = myPoiViewModel,
                                navigateToLocationScreen = {
                                    navController.navigate("locationScreen/$it")
                                },
                                contentPadding = innerPadding
                            )
                        }
                        composable("locationScreen/{categoryId}") { backStackEntry ->
                            val categoryId = backStackEntry.arguments?.getString("categoryId")
                            LocationScreen(
                                categoryId = categoryId ?: "",
                                viewModel = myPoiViewModel,
                                contentPadding = innerPadding)
                        }
                        composable("addLocationScreen/{latitude}/{longitude}") { backStackEntry ->
                            val latitude = backStackEntry.arguments?.getString("latitude") ?: ""
                            val longitude = backStackEntry.arguments?.getString("longitude") ?: ""
                            AddLocationScreen(
                                viewModel = myPoiViewModel,
                                onLocationSaved = {
                                    navController.popBackStack()
                                },
                                onBack = {
                                    navController.popBackStack()
                                },
                                contentPadding = innerPadding,
                                latitude = latitude,
                                longitude = longitude
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MyPoiTopBar() {
        TopAppBar(
            title = {
                Text(
                    text = "MyPOI",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }

    @Composable
    fun SavePositionButton(onSavePositionButtonPressed: (String,String) -> Unit) {
        Button(
            onClick = { onSavePositionButtonPressed("40.7128", "-74.0060") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Save my position"
            )
        }
    }

}
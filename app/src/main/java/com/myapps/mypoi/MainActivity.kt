package com.myapps.mypoi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myapps.mypoi.database.MyPoiDatabase
import com.myapps.mypoi.database.repository.MyPoiRepository
import com.myapps.mypoi.screen.CategoryScreen
import com.myapps.mypoi.screen.LocationScreen
import com.myapps.mypoi.ui.theme.MyPOITheme
import com.myapps.mypoi.viewmodel.MyPoiViewModel
import com.myapps.mypoi.viewmodel.MyPoiViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myPoiDatabase = MyPoiDatabase.getDatabase(applicationContext)
        val myPoiDao = myPoiDatabase.myPoiDao()
        val myPoiRepository = MyPoiRepository(myPoiDao)

        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Log.d("Permission", "Permission Granted")
            } else {
                Log.d("Permission", "Permission Denied")
            }
        }

        setContent {
            val navController = rememberNavController()
            MyPOITheme{
                val myPoiViewModel = viewModel(factory = MyPoiViewModelFactory(myPoiRepository), modelClass = MyPoiViewModel::class.java)

                NavHost(navController = navController, startDestination = "categoryScreen") {
                    composable("categoryScreen") {
                        CategoryScreen(
                            viewModel = myPoiViewModel,
                            navigateToLocationScreen = {
                                navController.navigate("locationScreen/$it")
                            }
                        )
                    }
                    composable("locationScreen/{categoryId}") { backStackEntry ->
                        val categoryId = backStackEntry.arguments?.getString("categoryId")
                        LocationScreen(
                            categoryId = categoryId ?: "",
                            viewModel = myPoiViewModel,
                            locationPermissionLauncher = locationPermissionLauncher,
                            context = LocalContext.current,
                            navigateToCategoriesScreen = {
                                navController.navigate("categoryscreen")
                            }
                        )
                    }
                }
            }
        }
    }
}
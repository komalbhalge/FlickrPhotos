package com.kb.flickrphotos.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.kb.flickrphotos.R
import com.kb.flickrphotos.data.model.Photo
import com.kb.flickrphotos.ui.theme.FlickrPhotosTheme
import com.kb.flickrphotos.ui.view.GridDetailCard
import com.kb.flickrphotos.ui.view.HomeScreen
import com.kb.flickrphotos.ui.viewmodel.MainActivityViewModel
import com.kb.flickrphotos.utils.ApiParams
import com.kb.flickrphotos.utils.NavigationUtils.HOME_SCREEN_ROUTE
import com.kb.flickrphotos.utils.NavigationUtils.PHOTO_DETAIL_ID_KEY
import com.kb.flickrphotos.utils.NavigationUtils.PHOTO_DETAIL_ROUTE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val mainActivityViewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiParams.setApiKey()
        setContent {
            FlickrPhotosTheme {
                NavigatePage()
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    fun NavigatePage() {
        val navHostController = rememberNavController()

        NavHost(
            navController = navHostController,
            startDestination = HOME_SCREEN_ROUTE
        ) {

            //Launch the home screen first with Navhost
            composable(HOME_SCREEN_ROUTE) {
                HomeScreen(
                    mainActivityViewModel,
                    navHostController
                )
            }

            //Add another screen(photo detail) here
            composable(
                route = "${PHOTO_DETAIL_ROUTE}/{${PHOTO_DETAIL_ID_KEY}}",
                arguments = listOf(
                    navArgument(PHOTO_DETAIL_ID_KEY) {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                //Fetch the arguments(photo detail) and pass to the view
                val arguments = requireNotNull(backStackEntry.arguments)
                arguments.getString(PHOTO_DETAIL_ID_KEY)?.let { json ->
                    val item = Gson().fromJson(json, Photo::class.java)
                    GridDetailCard(data = item, R.drawable.placeholder)
                }
            }
        }
    }
}


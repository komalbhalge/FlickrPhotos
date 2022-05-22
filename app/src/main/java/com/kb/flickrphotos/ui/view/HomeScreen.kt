package com.kb.flickrphotos.ui.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kb.flickrphotos.R
import com.kb.flickrphotos.ui.GridItemCard
import com.kb.flickrphotos.ui.viewmodel.MainActivityViewModel
import com.kb.flickrphotos.utils.ConnectivityUtils
import com.kb.flickrphotos.utils.Status

@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    viewModel: MainActivityViewModel,
    navController: NavController,
) {
    val TAG = "HomeScreen"

    val context = LocalContext.current

    var searchedTag by remember {
        mutableStateOf("Electrolux") // Search "Electrolux" by default
    }
    val showProgressBarState = remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp)
    ) {
        //InputTextField
        OutlinedTextField(
            value = searchedTag,
            label = {
                Text(text = context.getString(R.string.hint_search))
            }, onValueChange = {
                searchedTag = it
            },
            textStyle = TextStyle(color = Color.Blue),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        //Show progress while dowloading the photos completes
        if (showProgressBarState.value) {
            ShowProgress()
        }

        //Check internet connectivity before any network call and show toast message with status
        if (ConnectivityUtils.isNetworkAvailable(context)) {
            viewModel.controller.searchButtonOnClick(
                searchedTag,
                viewModel
            )
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.message_no_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
        //Capture photo response
        val photosResponse = viewModel.searchedPhotosData

        when (photosResponse.status) {
            Status.LOADING -> {
                showProgressBarState.value = true
            }
            Status.SUCCESS -> {
                showProgressBarState.value = false
                photosResponse.data?.let { list ->
                    LazyVerticalGrid(cells = GridCells.Fixed(3)) {
                        items(list.size) { index ->
                            val item = list[index]
                            GridItemCard(
                                navController = navController,
                                data = item,
                                controller = viewModel.controller,
                                placeholderId = R.drawable.placeholder
                            )
                        }
                    }
                }
            }
            Status.ERROR -> {
                showProgressBarState.value = false
                Log.e(TAG, "ERROR" + photosResponse.message)
            }
        }
    }
}

@Composable
fun ShowProgress() {
    Box(
        contentAlignment = Alignment.Center, // you apply alignment to all children
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(0.3f),
            strokeWidth = 8.dp
        )
    }

}


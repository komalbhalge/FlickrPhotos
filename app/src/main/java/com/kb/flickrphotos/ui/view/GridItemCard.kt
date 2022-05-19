package com.kb.flickrphotos.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.kb.flickrphotos.controllers.Controller
import com.kb.flickrphotos.data.model.Photo
import com.kb.flickrphotos.utils.NavigationUtils
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalFoundationApi
@Composable
fun GridItemCard(
    navController: NavController,
    data: Photo,
    modifier: Modifier = Modifier,
    controller: Controller,
    placeholderId: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(3.dp)
                .combinedClickable(
                    onClick = { //Navigate to Details Screen on click of the photo
                        /* NOTE:
                           Because of this (https://issuetracker.google.com/issues/217815060?pli=1) known issue
                           by google, sending links inside arguments make the app crash
                           when we receive the data/argument on the other side.
                           As a solution to this, creating and sending a partial image url
                           and alterred the Description as well
                         */
                        val customisedPhotoDetails = data
                        customisedPhotoDetails.photo_url =
                            generateImageID(data.id ?: "", data.secret ?: "")
                        if (customisedPhotoDetails.description?.Content?.contains("\"") ?: false) {
                            customisedPhotoDetails.description?.Content = ""
                        }
                        val itemVal = Gson().toJson(customisedPhotoDetails)
                        navController.navigate("${NavigationUtils.PHOTO_DETAIL_ROUTE}/$itemVal")
                    },
                    onLongClick = { //Display Download dialog on long press
                        controller.showDownloadDialog(
                            context = context,
                            data.photo_url ?: "",
                            //generating an Photo ID using other photo/data parameters
                            generateImageID(data.id ?: "", data.secret ?: "")
                        )
                    },
                ),
            shape = RoundedCornerShape(5.dp)

        ) {
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
            ) {

                if (data.photo_url.isNullOrEmpty()) {
                    //Display a placeholder when photo url is absent
                    val painter = painterResource(id = placeholderId)
                    Image(
                        painter = painter, contentDescription = data.title,
                        contentScale = ContentScale.Crop
                    )
                } else {
                    GlideImage(
                        imageModel = data.photo_url,
                        contentScale = ContentScale.Crop,
                        placeHolder = ImageBitmap.imageResource(id = placeholderId),
                        error = ImageBitmap.imageResource(placeholderId)
                    )
                }
            }
        }
    }
}

fun generateImageID(id: String, secret: String): String {
    return id.plus("_").plus(secret).plus(".jpg")
}

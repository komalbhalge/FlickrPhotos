package com.kb.flickrphotos.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kb.flickrphotos.R
import com.kb.flickrphotos.data.model.Photo
import com.kb.flickrphotos.ui.theme.Purple500
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalFoundationApi
@Composable
fun GridDetailCard(data: Photo, placeholderId: Int) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Purple500),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Photo Detail",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.padding(20.dp))

        GlideImage(
            imageModel = generateImageURL(data),
            contentScale = ContentScale.Crop,
            placeHolder = ImageBitmap.imageResource(id = placeholderId),
            error = ImageBitmap.imageResource(placeholderId),
            contentDescription = "Detail Image",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(10.dp))
        )

        Spacer(modifier = Modifier.padding(10.dp))

        //Image tittle
        Text(
            text = context.getString(R.string.tx_image_title).plus(data.title),
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = context.getString(R.string.tx_image_title).plus(data.description?.Content),
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

//Creating photo url using other data params
fun generateImageURL(photo: Photo): String {
    return ("https://live.staticflickr.com/")
        .plus(photo.server)
        .plus("/")
        .plus(photo.photo_url)
}


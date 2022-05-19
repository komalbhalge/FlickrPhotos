package com.kb.flickrphotos.controllers

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kb.flickrphotos.R
import com.kb.flickrphotos.ui.viewmodel.MainActivityViewModel
import com.kb.flickrphotos.utils.ConnectivityUtils
import java.io.File

class Controller(mContext: Context) {
    val context = mContext
    fun searchButtonOnClick(
        searchTerm: String,
        mainActivityViewModel: MainActivityViewModel
    ) {
        mainActivityViewModel.getPhotosFromSearch(searchTerm)
    }

    @SuppressLint("Range")
    fun downloadImage(url: String) {

        var msg: String?
        var lastMsg = ""
        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }, 100)
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }.start()
    }

    private fun statusMessage(url: String, directory: File, status: Int): String {
        var msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
        return msg
    }

    fun showDownloadDialog(context: Context, url: String, imageId: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(context.getString(R.string.message_download, imageId))
        builder.setPositiveButton(context.getString(R.string.label_download)) { _, _ ->
            if (ConnectivityUtils.isNetworkAvailable(context)) {
                downloadImage(url)
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.message_no_internet),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        builder.setNegativeButton(context.getString(R.string.label_cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}


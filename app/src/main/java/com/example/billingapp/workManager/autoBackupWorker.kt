package com.example.billingapp.workManager

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.billingapp.database.BillingDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.File

class autoBackupWorker(context: Context, params: WorkerParameters)
    : Worker(context,params){
    override fun doWork(): Result {
        return runBlocking {
            try {
                val database = BillingDatabase.getDatabase(applicationContext)
                val bills = database.billDao().getFullBillS()
                val jsonString = Json.encodeToString(bills)

                val resolver = applicationContext.contentResolver
                val fileName = "autoBackup.json"
                val relativePath = "Documents/billingApp"

                val publicDir = File("/storage/emulated/0/$relativePath")
                val targetFile = File(publicDir, fileName)
                if (targetFile.exists()) {
                    Log.d("WORKMANAGER_TEST", "Found orphaned file via direct disk check. Deleting it to prevent duplicates...")
                    targetFile.delete()

                    MediaScannerConnection.scanFile(applicationContext, arrayOf(targetFile.absolutePath), null, null)
                }

                val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                } else {
                    MediaStore.Files.getContentUri("external")
                }

                val projection = arrayOf(MediaStore.MediaColumns._ID)
                val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.RELATIVE_PATH} = ?"
                val selectionArgs = arrayOf(fileName, "$relativePath/")

                var existingFileUri: android.net.Uri? = null

                resolver.query(collectionUri, projection, selection, selectionArgs, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                        val id = cursor.getLong(idColumn)
                        existingFileUri = ContentUris.withAppendedId(collectionUri, id)
                    }
                }

                val finalUri = if (existingFileUri != null) {
                    Log.d("WORKMANAGER_TEST", "Existing backup file found. Overwriting...")
                    existingFileUri
                } else {
                    Log.d("WORKMANAGER_TEST", "Creating clean new entry...")
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
                        }
                    }
                    resolver.insert(collectionUri, contentValues)
                }

                if (finalUri != null) {
                    resolver.openOutputStream(finalUri, "wt")?.use { outputStream ->
                        outputStream.write(jsonString.toByteArray())
                    }

                    MediaScannerConnection.scanFile(
                        applicationContext,
                        arrayOf(targetFile.absolutePath),
                        arrayOf("application/json")
                    ) { path, _ ->
                        Log.d("WORKMANAGER_TEST", "Media scanner refreshed: $path")
                    }

                    Log.d("WORKMANAGER_TEST", "Backup successfully updated without duplicates!")
                    Result.success()
                } else {
                    Result.failure()
                }
            } catch (e: Exception) {
                Log.e("WORKMANAGER_TEST", "Overwriting backup worker failed", e)
                Result.retry()
            }
        }
    }
}
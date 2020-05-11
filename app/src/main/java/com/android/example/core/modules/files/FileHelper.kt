package com.android.example.core.modules.files

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.android.example.core.app.BaseApplication
import java.io.File
import java.io.FileOutputStream
import java.util.*

open class FileHelper(private val context: Context) {

    fun savePublicFile(bytes: ByteArray, name: String, type: String): Uri? {
        var uri: Uri? = null
        try {
            val file = File(getBaseFilesPublicDirectory(), formatFileName(name, type))
            uri = saveFile(file, bytes)
        } catch (e: Exception) { BaseApplication.logger.e(e) }
        return uri
    }

    fun savePrivateFile(bytes: ByteArray, name: String, type: String): Uri? {
        var uri: Uri? = null
        try {
            val file = File(getBaseFilesPrivateDirectory(), formatFileName(name, type))
            uri = saveFile(file, bytes)
        } catch (e: Exception) { BaseApplication.logger.e(e) }
        return uri
    }

    fun removeFiles() {
        try {
            val public = getBaseFilesPublicDirectory()
            val private = getBaseFilesPrivateDirectory()
            public?.listFiles()?.forEach { it.delete() }
            private?.listFiles()?.forEach { it.delete() }
        } catch (e: Exception) { BaseApplication.logger.e(e) }
    }

    open fun saveFile(file: File, bytes: ByteArray): Uri? {
        var uri: Uri? = null

        if (file.exists())
            file.delete()

        if (file.createNewFile()) {
            val fOut = FileOutputStream(file)
            fOut.write(bytes)
            fOut.flush()
            fOut.close()
            uri = FileProvider.getUriForFile(context, "com.smartgrid.mobileres04kv.fileprovider", file)
        }

        return uri
    }

    open fun getBaseFilesPublicDirectory(): File? {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "mobileres04kv"
        )
        file.mkdirs()
        return file
    }

    open fun getBaseFilesPrivateDirectory(): File? {
        val file = context.getExternalFilesDir(null)
        file?.mkdirs()
        return file
    }

    open fun formatFileName(title: String?, format: String): String {
        val name = if (title.isNullOrEmpty()) "mobileres04kv"
        else title.toLowerCase(Locale.getDefault()).replace(' ', '_')

        return "$name.$format"
    }
}
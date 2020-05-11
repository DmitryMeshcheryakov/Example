package com.android.example.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

object IntentUtils {

    private const val CONTACT_REQUEST_CODE = 12987
    const val FILE_REQUEST_CODE = 1297

    fun selectImage(context: Activity?, resultCode: Int = -1, mimeType: String?) {
        runCatching {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"))
            intent.action = Intent.ACTION_GET_CONTENT
            mimeType?.let { intent.type = it }
            context?.startActivityForResult(intent, resultCode)
        }
    }

    fun selectContact(context: Activity?, requestCode: Int = CONTACT_REQUEST_CODE) {
        runCatching {
            val intent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"))
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            context?.startActivityForResult(intent, requestCode)
        }
    }

    fun selectFile(context: Activity?, requestCode: Int = FILE_REQUEST_CODE) {
        runCatching {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            context?.startActivityForResult(intent, requestCode)
        }
    }

    fun selectFile(fragment: Fragment, requestCode: Int = FILE_REQUEST_CODE) {
        runCatching {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    fun openDialer(context: Activity?, phoneNumber: String) {
        runCatching {
            var phone = FormatUtils.getDigits(phoneNumber)
            if (phone.startsWith("375")) phone = "+$phone"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context?.startActivity(intent)
        }
    }

    fun openAppInStore(context: Activity?, appId: String) {
        runCatching {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/dev?id=$appId")
            )
            context?.startActivity(intent)
        }
    }

    fun openLink(context: Activity?, url: String) {
        runCatching {
            val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
            context?.startActivity(intent)
        }
    }

    fun openAppSettings(context: Context?) {
        runCatching {
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", context?.packageName, null)
            }
            context?.startActivity(intent)
        }
    }

    fun openFile(context: Activity?, uri: Uri?) {
        runCatching {
            val u = if (uri?.scheme.orEmpty().startsWith("file")) FileProvider.getUriForFile(context!!, "com.smartgrid.mobileres04kv.fileprovider", File(uri?.path.orEmpty()))
            else uri
            val intent = Intent(Intent.ACTION_VIEW).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                data = u
            }
            context?.startActivity(intent)
        }
    }

    fun share(context: Activity?, text: String?, subject: String? = null) {
        context?.let {
            ShareCompat.IntentBuilder.from(it)
                .setSubject(subject)
                .setText(text)
                .setType("text/plain")
                .startChooser()
        }
    }

    fun sendEmail(context: Activity?, email: String, text: String? = null, subject: String? = null) {
        context?.let {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("mailto:" + Uri.encode(email) + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(text))
            }
            it.startActivity(intent)
        }
    }

    fun parseContact(activity: Activity?, data: Intent?, requestCode: Int = CONTACT_REQUEST_CODE): String? {
        var phoneNum: String? = null
        if (requestCode == CONTACT_REQUEST_CODE) {
            data?.data?.let {
                val cursor = activity?.contentResolver?.query(it, null, null, null, null)
                if (cursor?.moveToFirst() == true) {
                    val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    phoneNum = cursor.getString(phoneIndex)
                    cursor.close()
                }
            }
        }
        return phoneNum
    }

    fun parseFile(activity: Activity?, data: Intent?, requestCode: Int = FILE_REQUEST_CODE): File? {
        var file: File? = null
        if (requestCode == FILE_REQUEST_CODE) {
            runCatching {
                data?.data?.let {
                    file = File(getRealPathFromURI(activity, it).orEmpty())
                }
            }
        }
        return file
    }

    private fun getRealPathFromURI(
        context: Activity?,
        contentURI: Uri?
    ): String? {
        var result: String = ""
        val queryUri = MediaStore.Files.getContentUri("external")
        val columnData = MediaStore.Files.FileColumns.DATA
        val columnSize = MediaStore.Files.FileColumns.SIZE

        val projectionData =
            arrayOf(MediaStore.Files.FileColumns.DATA)


        var name: String? = null
        var size: String? = null

        val cursor: Cursor = context?.contentResolver?.query(contentURI!!, null, null, null, null)!!
        if (cursor.count > 0) {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            name = cursor.getString(nameIndex)
            size = cursor.getString(sizeIndex)
            cursor.close()
        }

        if (name != null && size != null) {
            val selectionNS = "$columnData LIKE '%$name' AND $columnSize='$size'"
            val cursorLike: Cursor = context.contentResolver.query(queryUri, projectionData, selectionNS, null, null)!!
            if (cursorLike.count > 0) {
                cursorLike.moveToFirst()
                val indexData = cursorLike.getColumnIndex(columnData)
                if (cursorLike.getString(indexData) != null) {
                    result = cursorLike.getString(indexData)
                }
                cursorLike.close()
            }
        }

        return result
    }
}
package com.rontaxi.util


import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import android.webkit.MimeTypeMap
import com.rontaxi.RonTaxiApp
import android.content.ContentResolver
import android.net.Uri


fun createMultipartBody(fieldName: String, filePath: String): MultipartBody.Part? {
    val file = File(filePath) // -- 1 --
    val requestBody = createRequestBody(file)
    if(requestBody!=null)
        return MultipartBody.Part.createFormData(fieldName, file.getName(), requestBody)


    return null
}

private fun createRequestBody(file: File): RequestBody?  {

    var mime=getMimeType(Uri.fromFile(file))

    if(mime!=null){
        return RequestBody.create(MediaType.parse(mime), file)
    }
    return null
}


fun getMultipartImage(fieldName: String, filePath: String): MultipartBody.Part? {

    val file = File(filePath)

    var mime=getMimeType(Uri.fromFile(file))

    if(mime!=null) {
        val requestFile = RequestBody.create(MediaType.parse(getMimeType(Uri.fromFile(file))), file)
        return MultipartBody.Part.createFormData(fieldName, file.name, requestFile)
    }

    return null
// MultipartBody.Part is used to send also the actual file name

}


fun getRequestBody(field: String): RequestBody {
    return RequestBody.create(MediaType.parse("multipart/form-data"), field)
}

fun getMimeType(uri: Uri): String? {

    var mimeType: String? = null
    if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
        val cr = RonTaxiApp.context.getContentResolver()
        mimeType = cr.getType(uri)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
            uri
                .toString()
        )
        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            fileExtension.toLowerCase()
        )
    }
    return mimeType
}

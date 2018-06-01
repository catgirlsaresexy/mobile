package sexy.catgirlsare.android.ui.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns.DISPLAY_NAME
import android.provider.OpenableColumns.SIZE
import android.util.Log
import androidx.core.database.getLong
import androidx.core.database.getString
import sexy.catgirlsare.android.R
import sexy.catgirlsare.android.api.UploadResponse
import sexy.catgirlsare.android.api.upload
import java.io.File
import java.io.FileNotFoundException
import kotlin.concurrent.thread

class HomeViewModel : ViewModel() {

    val message: LiveData<String> = MutableLiveData()
    private val mutableMessage: MutableLiveData<String>
        get() = message as MutableLiveData<String>

    val file: LiveData<UploadResponse> = MutableLiveData()
    private val mutableFile: MutableLiveData<UploadResponse>
        get() = file as MutableLiveData<UploadResponse>

    fun startUpload(uri: Uri, context: Context) {
        thread {
            val response = when (uri.scheme.toLowerCase()) {
                "content" -> {
                    val cursor = context.contentResolver.query(uri, null, null, null, null)

                    if (!cursor.moveToFirst()) {
                        cursor.close()
                        mutableMessage.postValue(context.getString(R.string.fileInvalid))
                        return@thread
                    }

                    val name = cursor.getString(DISPLAY_NAME)
                    val size = cursor.getLong(SIZE)
                    val stream = context.contentResolver.openInputStream(uri)

                    if (size > 100000000L) {
                        mutableMessage.postValue(context.getString(R.string.fileSizeLimitExceeded))
                        return@thread
                    }

                    upload(name, stream, size)
                }
                "file" -> {
                    val file = try {
                        File(uri.path)
                    } catch (e: FileNotFoundException) {
                        mutableMessage.postValue(context.getString(R.string.fileInvalid))
                        return@thread
                    }

                    if (!file.exists()) {
                        mutableMessage.postValue(context.getString(R.string.fileNotFound))
                        return@thread
                    }

                    if (file.length() > 100000000L) {
                        mutableMessage.postValue(context.getString(R.string.fileSizeLimitExceeded))
                        return@thread
                    }

                    upload(file.name, file.inputStream(), file.length())
                }
                else -> {
                    Log.d("Upload", "Unknown scheme ${uri.scheme}")
                    return@thread
                }
            }

            val body = response.body()

            if (body == null || !body.isSuccessful) {
                mutableMessage.postValue(context.getString(R.string.uploadFailed))
                return@thread
            }

            mutableMessage.postValue(context.getString(R.string.uploadSuccessful))
            mutableFile.postValue(body)
        }
    }
}

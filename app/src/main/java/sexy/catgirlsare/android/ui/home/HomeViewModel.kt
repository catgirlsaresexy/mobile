package sexy.catgirlsare.android.ui.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns.DISPLAY_NAME
import android.provider.OpenableColumns.SIZE
import androidx.core.database.getString
import androidx.core.database.getInt
import sexy.catgirlsare.android.api.UploadResponse
import sexy.catgirlsare.android.api.upload
import kotlin.concurrent.thread

class HomeViewModel : ViewModel() {

    val message: LiveData<String> = MutableLiveData()
    private val mutableMessage: MutableLiveData<String>
        get() = message as MutableLiveData<String>

    val file: LiveData<UploadResponse> = MutableLiveData()
    private val mutableFile: MutableLiveData<UploadResponse>
        get() = file as MutableLiveData<UploadResponse>

    fun startUpload(contentUri: Uri, context: Context) {
        thread {
            val cursor = context.contentResolver.query(contentUri, null, null, null, null)

            if (!cursor.moveToFirst()) {
                cursor.close()
                mutableMessage.postValue("The file is not valid")
                return@thread
            }

            val name = cursor.getString(DISPLAY_NAME)
            val size = cursor.getInt(SIZE)
            val stream = context.contentResolver.openInputStream(contentUri)

            if (size > 100000000) {
                mutableMessage.postValue("The file is too large")
                return@thread
            }

            val response = upload(name, stream, size)
            val body = response.body()

            if (body == null || !body.isSuccessful) {
                mutableMessage.postValue("The did not upload successfully")
                return@thread
            }

            mutableMessage.postValue("File upload completed successfully")
            mutableFile.postValue(body)
        }
    }
}

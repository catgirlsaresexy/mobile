package sexy.catgirlsare.android.ui.uploads

import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import sexy.catgirlsare.android.api.Upload
import sexy.catgirlsare.android.api.UploadList
import sexy.catgirlsare.android.api.getUploads
import kotlin.concurrent.thread

class UploadsDataSource : PageKeyedDataSource<Int, Upload>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Upload>) {
        thread {
            val list = loadItems(1, params.requestedLoadSize)
            callback.onResult(list.uploads, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Upload>) {
        thread {
            val list = loadItems(params.key, params.requestedLoadSize)
            val next = if (list.uploads.isEmpty()) null else params.key + 1
            callback.onResult(list.uploads, next)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Upload>) {
        thread {
            val list = loadItems(params.key, params.requestedLoadSize)
            val prev = if (list.uploads.isEmpty()) null else params.key - 1
            callback.onResult(list.uploads, prev)
        }
    }

    private fun loadItems(page: Int, count: Int): UploadList {
        val response = getUploads(page, count)
        return response.body() ?: UploadList(emptyList(), 0)
    }

    class Factory : DataSource.Factory<Int, Upload>() {
        override fun create(): DataSource<Int, Upload> = UploadsDataSource()
    }
}

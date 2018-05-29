package sexy.catgirlsare.android.ui.uploads

import android.arch.lifecycle.ComputableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import sexy.catgirlsare.android.api.Upload

class UploadsViewModel : ViewModel() {

    private val config = PagedList.Config.Builder()
        .setPageSize(50)
        .setInitialLoadSizeHint(50)
        .setPrefetchDistance(20)
        .setEnablePlaceholders(false)
        .build()

    private val source = UploadsDataSource.Factory()

    var uploads = LivePagedListBuilder<Int, Upload>(source, config)
        .setInitialLoadKey(1)
        .build()

    fun invalidate() {
        uploads.value?.dataSource?.invalidate()
    }
}

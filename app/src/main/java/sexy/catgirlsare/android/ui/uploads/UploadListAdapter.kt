package sexy.catgirlsare.android.ui.uploads

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import sexy.catgirlsare.android.R
import sexy.catgirlsare.android.api.Upload

class UploadListAdapter : PagedListAdapter<Upload, UploadViewHolder>(diffCallback) {

    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadViewHolder {
        if (!this::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }

        val view = inflater.inflate(viewType, parent, false)

        return UploadViewHolder(view)
    }

    override fun onBindViewHolder(holder: UploadViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int) = R.layout.upload_item

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Upload>() {
            override fun areItemsTheSame(oldItem: Upload?, newItem: Upload?) = oldItem?.path == newItem?.path
            override fun areContentsTheSame(oldItem: Upload?, newItem: Upload?) = oldItem == newItem
        }
    }
}

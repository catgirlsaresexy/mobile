package sexy.catgirlsare.android.ui.uploads

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.upload_item.*
import sexy.catgirlsare.android.api.Upload

class UploadViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    override val containerView = view

    fun bind(upload: Upload?) {
        if (upload == null) {
            clear()
            return
        }

        textPath.text = upload.path
        Picasso.get()
            .load("https://catgirlsare.sexy/api/thumb/${upload.path}")
            .into(thumbnail)
    }

    private fun clear() {
        textPath.text = ""
        Picasso.get()
            .load(null as String?)
            .into(thumbnail)
    }
}

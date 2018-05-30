package sexy.catgirlsare.android.ui.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.Intent.CATEGORY_OPENABLE
import android.content.Intent.EXTRA_INITIAL_INTENTS
import android.os.Bundle
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.ACTION_VIDEO_CAPTURE
import android.provider.MediaStore.Audio.Media.RECORD_SOUND_ACTION
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.home_fragment.*
import sexy.catgirlsare.android.R
import sexy.catgirlsare.android.ui.uploads.UploadsViewModel

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.home_fragment, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        uploadButton.setOnClickListener {
            val openIntent = Intent(ACTION_OPEN_DOCUMENT)
            openIntent.addCategory(CATEGORY_OPENABLE)
            openIntent.type = "*/*"
            val imageIntent = Intent(ACTION_IMAGE_CAPTURE)
            val videoIntent = Intent(ACTION_VIDEO_CAPTURE)
            val audioIntent = Intent(RECORD_SOUND_ACTION)
            val picker = Intent.createChooser(Intent(), getString(R.string.select))
            picker.putExtra(EXTRA_INITIAL_INTENTS, arrayOf(openIntent, imageIntent, videoIntent, audioIntent))
            startActivityForResult(picker, 1000)
        }

        viewModel.message.observe(this::getLifecycle) { message ->
            if (message == null || message.isBlank()) return@observe
            context?.toast(message)
        }

        viewModel.file.observe(this::getLifecycle) { file ->
            if (file == null) return@observe

            ViewModelProviders.of(this).get(UploadsViewModel::class.java).invalidate()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1000 && data?.data != null) {
            viewModel.startUpload(data.data, context!!)
        }
    }
}

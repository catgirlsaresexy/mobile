package sexy.catgirlsare.android.ui.home

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.Intent.CATEGORY_OPENABLE
import android.content.Intent.EXTRA_INITIAL_INTENTS
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DCIM
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.ACTION_VIDEO_CAPTURE
import android.provider.MediaStore.Audio.Media.RECORD_SOUND_ACTION
import android.provider.MediaStore.EXTRA_OUTPUT
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider.getUriForFile
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.home_fragment.*
import sexy.catgirlsare.android.R
import sexy.catgirlsare.android.ui.uploads.UploadsViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

            val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val dcim = File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), "catgirls")
            if (!dcim.exists()) dcim.mkdirs()

            val imageIntent = Intent(ACTION_IMAGE_CAPTURE)
            val imageFile = File(dcim, "IMG_$time.jpg")
            imageIntent.putExtra(EXTRA_OUTPUT, getUriForFile(context!!, "sexy.catgirlsare.android.provider", imageFile))

            val videoIntent = Intent(ACTION_VIDEO_CAPTURE)
            val videoFile = File(dcim, "VID_$time.mp4")
            videoIntent.putExtra(EXTRA_OUTPUT, getUriForFile(context!!, "sexy.catgirlsare.android.provider", videoFile))

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
        Log.d("onActivityResult", "req: $requestCode, res: $resultCode, d: $data")
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            if (data.data != null) {
                viewModel.startUpload(data.data, context!!)
            } else {
                val image = File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), "catgirls").walk()
                    .reduce { acc, curr -> return@reduce if (acc.lastModified() > curr.lastModified()) acc else curr }
                viewModel.startUpload(image.toUri(), context!!)
            }
        }
    }
}

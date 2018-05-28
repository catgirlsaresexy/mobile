package sexy.catgirlsare.android.ui.uploads

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.uploads_fragment.*
import sexy.catgirlsare.android.R
import kotlin.math.roundToInt

class UploadsFragment : Fragment() {

    private lateinit var viewModel: UploadsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.uploads_fragment, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UploadsViewModel::class.java)

        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        val screenSize = metrics.widthPixels.toFloat()

        val gridSize = context?.resources?.getDimension(R.dimen.gridSize) ?: Float.NaN
        val realGridSize = if (gridSize.isFinite()) gridSize else screenSize

        val adapter = UploadListAdapter()

        list.adapter = adapter
        list.layoutManager = GridLayoutManager(context, (screenSize / realGridSize).roundToInt())

        viewModel.uploads.observe(this::getLifecycle) { uploads ->
            adapter.submitList(uploads)
        }
    }
}

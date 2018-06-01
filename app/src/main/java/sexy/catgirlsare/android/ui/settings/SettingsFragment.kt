package sexy.catgirlsare.android.ui.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.settings_fragment.*
import sexy.catgirlsare.android.R
import sexy.catgirlsare.android.api.key
import sexy.catgirlsare.android.prefs

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.settings_fragment, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showKeyButton.setOnClickListener {
            AlertDialog.Builder(context!!)
                .setMessage(getString(R.string.showKeyMessage).format(key))
                .setPositiveButton(R.string.showKeyCopy) { _, _ ->
                    val clipboard = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
                    if (clipboard == null) {
                        context?.toast(R.string.showKeyCopyError)
                        return@setPositiveButton
                    }

                    clipboard.primaryClip = ClipData.newPlainText(getString(R.string.showKeyCopyName), key)
                }
                .setNegativeButton(R.string.showKeyDismiss) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        toggleThemeButton.setOnClickListener {
            prefs.edit {
                putBoolean("dark", !prefs.getBoolean("dark", false))
            }
        }
    }
}

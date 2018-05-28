package sexy.catgirlsare.android

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import androidx.core.content.edit
import androidx.core.view.forEach
import androidx.core.view.isGone
import kotlinx.android.synthetic.main.main_activity.*
import sexy.catgirlsare.android.api.setApiKey
import sexy.catgirlsare.android.ui.main.LoginFragment
import sexy.catgirlsare.android.ui.uploads.UploadsFragment

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var loginFragment: LoginFragment
    private lateinit var uploadsFragment: UploadsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initializePrefs(this)
        prefs.registerOnSharedPreferenceChangeListener(this)

        onSharedPreferenceChanged(prefs, "key")

        homeButton.setOnClickListener {
            highlight(it as ImageButton)
            // todo home page
        }
        uploadsButton.setOnClickListener {
            highlight(it as ImageButton)
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.content, uploadsFragment)
                ?.commit()
        }
        logoutButton.setOnClickListener {
            highlight(it as ImageButton)

            // todo confirmation dialog

            prefs.edit {
                putString("key", "")
            }
            // onSharedPreferenceChanged should take care of the rest
        }
    }

    private fun highlight(button: ImageButton) {
        bottomNavigation.forEach { view ->
            view.setBackgroundColor(0xFFDDDDDD.toInt())
        }
        button.setBackgroundColor(0xFFAAAAAA.toInt())
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key != "key") return

        val newKey = prefs.getString("key", "")
        setApiKey(newKey)

        if (newKey.isNullOrBlank()) {
            loginFragment = LoginFragment()
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.content, loginFragment)
                ?.commit()
            bottomNavigation.isGone = true
        } else {
            uploadsFragment = UploadsFragment()
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.content, uploadsFragment)
                ?.commit()
            bottomNavigation.isGone = false
            highlight(homeButton)
        }
    }
}

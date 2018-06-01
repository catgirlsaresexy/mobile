package sexy.catgirlsare.android

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import androidx.core.content.edit
import androidx.core.view.forEach
import androidx.core.view.isGone
import kotlinx.android.synthetic.main.main_activity.*
import sexy.catgirlsare.android.api.isAdmin
import sexy.catgirlsare.android.api.setApiKey
import sexy.catgirlsare.android.ui.home.HomeFragment
import sexy.catgirlsare.android.ui.main.LoginFragment
import sexy.catgirlsare.android.ui.settings.SettingsFragment
import sexy.catgirlsare.android.ui.uploads.UploadsFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var loginFragment: LoginFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var uploadsFragment: UploadsFragment
    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializePrefs(this)
        prefs.registerOnSharedPreferenceChangeListener(this)

        if (prefs.getBoolean("dark", false)) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme)
        }

        setContentView(R.layout.main_activity)

        onSharedPreferenceChanged(prefs, "key")
        onSharedPreferenceChanged(prefs, "admin")

        homeButton.setOnClickListener {
            highlight(it as ImageButton)
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.content, homeFragment)
                ?.commit()
        }
        uploadsButton.setOnClickListener {
            highlight(it as ImageButton)
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.content, uploadsFragment)
                ?.commit()
        }
        adminButton.setOnClickListener {
            highlight(it as ImageButton)

            // todo admin panel
        }
        settingsButton.setOnClickListener {
            highlight(it as ImageButton)
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.content, settingsFragment)
                ?.commit()
        }
        logoutButton.setOnClickListener {
            highlight(it as ImageButton)

            AlertDialog.Builder(this)
                .setMessage(R.string.logoutMessage)
                .setPositiveButton(R.string.logoutConfirm) { dialog, _ ->
                    prefs.edit {
                        putString("key", "")
                    }
                    // onSharedPreferenceChanged should take care of the rest

                    dialog.dismiss()
                }
                .setNegativeButton(R.string.logoutCancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun highlight(button: ImageButton) {

        val attrs = intArrayOf(R.attr.bottomNavigationBackground, R.attr.bottomNavigationSelected)
        val values = theme.obtainStyledAttributes(attrs)

        val unselected = values.getColor(attrs.indexOf(R.attr.bottomNavigationBackground), 0)
        val selected = values.getColor(attrs.indexOf(R.attr.bottomNavigationSelected), 0)

        values.recycle()

        bottomNavigation.forEach { view ->
            if (view.id == button.id) {
                view.setBackgroundColor(selected)
            } else {
                view.setBackgroundColor(unselected)
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            "key" -> {
                val newKey = prefs.getString("key", "")
                setApiKey(newKey)

                if (newKey.isNullOrBlank()) {
                    loginFragment = LoginFragment()
                    supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.content, loginFragment)
                        ?.commit()
                    bottomNavigation.isGone = true
                } else {
                    homeFragment = HomeFragment()
                    uploadsFragment = UploadsFragment()
                    settingsFragment = SettingsFragment()
                    supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.content, homeFragment)
                        ?.commit()
                    bottomNavigation.isGone = false
                    highlight(homeButton)

                    thread {
                        val response = isAdmin()
                        val isAdmin = response.body()?.isAdmin == true
                        prefs.edit {
                            putBoolean("admin", isAdmin)
                        }
                    }
                }
            }
            "admin" -> {
                adminButton.isGone = !prefs.getBoolean("admin", false)
            }
            "dark" -> {
                if (prefs.getBoolean("dark", false)) {
                    setTheme(R.style.AppTheme_Dark)
                } else {
                    setTheme(R.style.AppTheme)
                }

                recreate()
                settingsButton.callOnClick()
            }
        }
    }
}

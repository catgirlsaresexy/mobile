package sexy.catgirlsare.android

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
        } else {
            uploadsFragment = UploadsFragment()
            supportFragmentManager?.beginTransaction()
                ?.replace(R.id.content, uploadsFragment)
                ?.commit()
        }
    }
}

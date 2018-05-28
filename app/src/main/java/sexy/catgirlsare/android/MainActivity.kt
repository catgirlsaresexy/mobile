package sexy.catgirlsare.android

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.main_activity.*
import sexy.catgirlsare.android.api.setApiKey

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initializePrefs(this)
        prefs.registerOnSharedPreferenceChangeListener(this)

        onSharedPreferenceChanged(prefs, "key")
    }

    override fun onSupportNavigateUp() = findNavController(content).navigateUp()

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key != "key") return
        setApiKey(prefs.getString(key, ""))
        findNavController(content).navigate(R.id.uploads)
    }
}

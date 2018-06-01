package sexy.catgirlsare.android

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

lateinit var prefs: SharedPreferences

fun initializePrefs(context: Context) {
    if (::prefs.isInitialized) return
    prefs = PreferenceManager.getDefaultSharedPreferences(context)
}
